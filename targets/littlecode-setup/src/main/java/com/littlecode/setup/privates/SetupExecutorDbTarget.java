package com.littlecode.setup.privates;

import com.littlecode.files.IOUtil;
import com.littlecode.setup.Setup;
import com.littlecode.setup.SetupSetting;
import com.littlecode.setup.db.metadata.MetaDataClasses;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
@Builder
public class SetupExecutorDbTarget {
    private SetupSetting setting;
    private Connection connection;
    private Setup.ExecutorDataBase executorDataBase;

    private List<SetupClassesDB.StatementItem> createStatementItemList() {

        if (executorDataBase == null)
            return new ArrayList<>();

        var extractFileNames = new Function<List<String>, List<File>>() {
            @Override
            public List<File> apply(List<String> fileNames) {
                List<File> response = new ArrayList<>();
                fileNames
                        .forEach(
                                fileName ->
                                {
                                    var directory = new File(fileName);
                                    if (directory.isFile()) {
                                        response.add(directory);
                                        return;
                                    }
                                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                                    URL resourceDir = classLoader.getResource(directory.toPath().toString());
                                    if (resourceDir == null)
                                        return;

                                    try (var list = Files.list(Paths.get(resourceDir.toURI()))) {
                                        list
                                                .forEach(path -> {
                                                    var file = path.toFile();
                                                    var check = fileName + "/" + file.getName();
                                                    if (!check.endsWith(SetupClassesDB.SQL_EXTENSION))
                                                        return;
                                                    if (!check.startsWith(fileName))
                                                        return;
                                                    if (!file.isFile())
                                                        return;
                                                    log.info("source file: {}", file.toPath());
                                                    response.add(file);
                                                });
                                    } catch (URISyntaxException | IOException e) {
                                        log.error(e.getMessage());
                                    }
                                });

                return response;
            }
        };
        var parseDir = new Function<List<File>, List<SetupClassesDB.StatementItem>>() {
            @Override
            public List<SetupClassesDB.StatementItem> apply(List<File> fileList) {
                List<SetupClassesDB.StatementItem> response = new ArrayList<>();
                fileList
                        .forEach(
                                targetFile ->
                                        response.add
                                                (
                                                        SetupClassesDB.StatementItem
                                                                .builder()
                                                                .fileName(targetFile)
                                                                .build()
                                                )
                        );

                return response;
            }
        };

        List<SetupClassesDB.StatementItem> statementItems = new ArrayList<>();

        if (executorDataBase.sourceList != null) {
            var sourceList = executorDataBase.sourceList.call();
            var fileList = extractFileNames.apply(sourceList);
            statementItems.addAll(parseDir.apply(fileList));
        }
        if (setting.getDatabase().isAutoStart()) {
            var easySetupExecutorDbDDL = SetupExecutorDbDDL
                    .builder()
                    .executorDataBase(this.executorDataBase)
                    .setting(this.setting)
                    .build()
                    .execute();
            if (setting.getDatabase().isAutoApply())
                statementItems.addAll(easySetupExecutorDbDDL.getStatementItems());
        }
        return statementItems;
    }

    private void executeSaveSources() {
        var ddlSetting = setting.getDatabase().getDDL();
        if (!ddlSetting.isAutoSave())
            return;
        List<SetupClassesDB.StatementItem> statementItems = new ArrayList<>();
        var dropSafeOld = ddlSetting.isSafeDrops();
        try {
            ddlSetting.setSafeDrops(false);
            var easySetupExecutorDbDDL = SetupExecutorDbDDL
                    .builder()
                    .executorDataBase(this.executorDataBase)
                    .setting(this.setting)
                    .build()
                    .execute();
            statementItems.addAll(easySetupExecutorDbDDL.getStatementItems());
        } finally {
            ddlSetting.setSafeDrops(dropSafeOld);
        }

        var dbType="";
        try {
            dbType=connection.getMetaData().getDatabaseProductName().toLowerCase();
        } catch (SQLException e) {
            log.error(e.getMessage());
            dbType="unknown";
        }

        var exportDir = this.setting.getDatabase().getDDL().getExporterDirName(dbType);
        IOUtil
                .target(exportDir)
                .delete();

        IOUtil.createDir(exportDir);
        statementItems
                .forEach(
                        statementItem ->
                                log.debug("out file: {}", statementItem.save(exportDir).getFileName())
                );
    }

    private boolean executeExecSources() {

        //noinspection Convert2Lambda,Convert2Diamond
        BiConsumer<Connection, SetupClassesDB.StatementItem> execSQL = new BiConsumer<Connection, SetupClassesDB.StatementItem>() {
            @Override
            public void accept(Connection connection, SetupClassesDB.StatementItem statementItem) {
                statementItem
                        .getSource()
                        .forEach(sourceScript -> {
                            if (sourceScript == null || sourceScript.trim().isEmpty() || sourceScript.trim().startsWith(MetaDataClasses.SQL_COMMAND_COMMENT))
                                return;
                            var script = sourceScript;
                            if (executorDataBase.sourceParser != null) {
                                script = executorDataBase.sourceParser.apply(sourceScript);
                                if (script == null || script.trim().isEmpty() || script.trim().startsWith(MetaDataClasses.SQL_COMMAND_COMMENT)) {
                                    log.debug("command line skipped after [sourceParser]: {}", sourceScript);
                                    return;
                                }
                            } else {
                                if (script.trim().startsWith(MetaDataClasses.SQL_COMMAND_COMMENT)) {
                                    log.debug("command line skipped: {}", sourceScript);
                                    return;
                                }
                            }

                            try {
                                var statement = connection.createStatement();
                                statement.execute(script);
                            } catch (SQLException e) {
                                log.error(e.getMessage());
                            }
                        });
                if (executorDataBase.sourceExecute != null)
                    executorDataBase.sourceExecute.call(statementItem);
            }
        };

        try {
            if (connection == null)
                return false;

            var statementItems = createStatementItemList();
            if (statementItems.isEmpty())
                return true;

            var targetTags = this.setting.getDatabase().getTarget().getTags();
            targetTags
                    .forEach(
                            targetName ->
                            {
                                var targetEnum = SetupClassesDB.Target.of(targetName);
                                if (targetEnum == null)
                                    throw new RuntimeException("Invalid target name: " + targetName);

                                var listFilter = statementItems
                                        .stream()
                                        .filter(s -> {
                                            var baseName = IOUtil
                                                    .target(s.getFileName())
                                                    .baseName();
                                            if (!baseName.startsWith(targetEnum.getValue()) && baseName.endsWith(SetupClassesDB.SQL_EXTENSION))
                                                return false;
                                            log.debug("selected {}", s.getFileName());
                                            return true;
                                        })
                                        .toList();

                                if (listFilter.isEmpty())
                                    return;

                                listFilter
                                        .forEach(
                                                statementItem ->
                                                {
                                                    statementItem.setTarget(targetEnum);
                                                    execSQL.accept(connection, statementItem);
                                                });

                            }
                    );
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return true;
    }

    public boolean execute() {

        if (this.executorDataBase == null) {
            log.debug("this.executorDataBase is null: skipped ");
            return true;
        }
        executeSaveSources();
        if (this.executorDataBase.checker == null) {
            log.debug("this.executorDataBase.checker is null: skipped ");
            return true;
        }
        if (!this.executorDataBase.checker.call()) {
            log.info("this.executorDataBase.checker.call() is false: skipped ");
            return true;
        }

        return executeExecSources();
    }
}
