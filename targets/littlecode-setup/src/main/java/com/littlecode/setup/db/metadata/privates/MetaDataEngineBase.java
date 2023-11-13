package com.littlecode.setup.db.metadata.privates;

import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.setup.SetupSetting;
import com.littlecode.setup.db.metadata.MetaDataClasses;
import com.littlecode.setup.privates.SetupClassesDB;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class MetaDataEngineBase<T> {
    private final List<MetaDataClasses.MetaTable> sources = new ArrayList<>();
    private final List<SetupClassesDB.StatementItem> statementItemList = new ArrayList<>();

    public final List<MetaDataClasses.MetaTable> getSources() {
        return this.sources;
    }

    public SetupSetting getSetting() {
        throw new RuntimeException("No implemented");
    }

    public final T sources(List<MetaDataClasses.MetaTable> tableList) {
        this.sources.clear();
        this.sources.addAll(tableList);
        //noinspection unchecked
        return (T) this;
    }

    public final List<SetupClassesDB.StatementItem> getStatements() {
        if (!this.statementItemList.isEmpty())
            return this.statementItemList;

        Map<SetupClassesDB.Target, SetupClassesDB.StatementItem> statementsMap = new HashMap<>();

        var defaultSchema = this.getSetting().getConfig().getDefaultSchema();
        List<String> schemaNames = new ArrayList<>();
        if (!PrimitiveUtil.isEmpty(defaultSchema))
            schemaNames.add(defaultSchema);

        this.getSources()
                .forEach(table -> {
                    table.makeSources();
                    var name = table.getSchemaName().trim().toLowerCase();
                    if (!PrimitiveUtil.isEmpty(name) && !schemaNames.contains(name))
                        schemaNames.add(name);

                });


        if (!schemaNames.isEmpty()) {
            Collections.sort(schemaNames);
            deduplicateLines(schemaNames);


            schemaNames.forEach(
                    schemaName -> {
                        var sttIn = statementsMap.get(SetupClassesDB.Target.Schemas);
                        if (sttIn == null) {
                            statementsMap.put(
                                    SetupClassesDB.Target.Schemas,
                                    sttIn = SetupClassesDB.StatementItem
                                            .builder()
                                            .directory(getSetting().getDatabase().getDDL().getExporterDirName())
                                            .target(SetupClassesDB.Target.Schemas)
                                            .build()
                            );
                        }
                        sttIn.getSource().add(MetaDataClasses.SQL_COMMAND_COMMENT);
                        sttIn.getSource().add(MetaDataClasses.SQL_COMMAND_COMMENT + "schema name: " + schemaName);
                        sttIn.getSource().add(MetaDataClasses.SQL_COMMAND_COMMENT);
                        sttIn.getSource().add(String.format(MetaDataClasses.FORMAT_CREATE_SCHEMA, schemaName));
                        sttIn.getSource().add(String.format(MetaDataClasses.FORMAT_SET_DEFAULT_SCHEMA, schemaName));
                        sttIn.getSource().add(MetaDataClasses.FORMAT_EXTENSION_UUID);

                        sttIn = statementsMap.get(SetupClassesDB.Target.Drops);
                        if (sttIn == null) {
                            statementsMap.put(
                                    SetupClassesDB.Target.Drops,
                                    sttIn = SetupClassesDB.StatementItem
                                            .builder()
                                            .directory(getSetting().getDatabase().getDDL().getExporterDirName())
                                            .target(SetupClassesDB.Target.Drops)
                                            .build()
                            );
                        }
                        sttIn.getSource().add(MetaDataClasses.SQL_COMMAND_COMMENT);
                        sttIn.getSource().add(MetaDataClasses.SQL_COMMAND_COMMENT + "schema name: " + schemaName);
                        sttIn.getSource().add(MetaDataClasses.SQL_COMMAND_COMMENT);
                        sttIn.getSource().add(String.format(MetaDataClasses.FORMAT_DROP_SCHEMA, schemaName));
                    });
        }

        this.getSources()
                .forEach(table -> {
                    var sttMap = Map.of(
                            SetupClassesDB.Target.Drops, parserDrops(table.getDrops()),
                            //SetupClassesDB.Target.Schemas,parserStatements(table.getSchemas()),
                            SetupClassesDB.Target.Tables, parserStatements(table.getTable()),
                            SetupClassesDB.Target.ConstraintsPK, parserStatements(table.getConstraintsPK()),
                            SetupClassesDB.Target.ConstraintsFK, parserStatements(table.getConstraintsFK()),
                            SetupClassesDB.Target.Indexes, parserStatements(table.getIndexes()),
                            SetupClassesDB.Target.Triggers, parserStatements(table.getTriggers())
                    );

                    sttMap.forEach((target, lines) -> {
                        if (lines == null || lines.isEmpty())
                            return;
                        var stt = statementsMap.containsKey(target)
                                ? statementsMap.get(target)
                                : SetupClassesDB.StatementItem.builder().target(target).build();
                        stt.getSource().addAll(lines);
                        statementsMap.put(target, stt);
                    });

                });
        statementItemList.clear();
        statementsMap.forEach((target, statementItem) -> {
            if (target.equals(SetupClassesDB.Target.Drops) && this.getSetting().getDatabase().getDDL().isSafeDrops())
                return;
            statementItemList.add(statementItem);
        });
        return this.statementItemList;
    }

    public T clear() {
        this.statementItemList.clear();
        this.sources.clear();
        //noinspection unchecked
        return (T) this;
    }

    public T export() {
        //noinspection unchecked
        return (T) this;
    }

    private void cleanup(List<String> lines) {
        for (var line : lines) {
            if (!PrimitiveUtil.isEmpty(line) && !line.startsWith(MetaDataClasses.SQL_COMMAND_COMMENT))
                return;
        }
        lines.clear();
    }

    private List<String> parserStatements(List<String> statements) {
        if (statements == null || statements.isEmpty())
            return new ArrayList<>();
        cleanup(statements);
        List<String> __return = new ArrayList<>();
        statements.forEach(statement -> {
            if (statement == null || statement.trim().isEmpty())
                return;
            __return.add(statement.trim());
        });
        return __return;
    }

    private List<String> parserDrops(List<String> drops) {
        drops = parserStatements(drops);
        if (drops.isEmpty())
            return drops;
        List<String> __return = new ArrayList<>();
        var ddlSetting = (this.getSetting() == null || this.getSetting().getDatabase() == null)
                ? null
                : this.getSetting().getDatabase().getDDL();
        boolean isSafeDrops = ddlSetting == null || ddlSetting.isSafeDrops();
        drops.forEach(statement -> {
            if (!isSafeDrops) {
                __return.add(statement);
                return;
            }

            if (statement.startsWith(MetaDataClasses.SQL_COMMAND_COMMENT))
                __return.add(statement);
            else
                __return.add(MetaDataClasses.SQL_COMMAND_COMMENT + statement);
        });
        return __return;
    }

    private void deduplicateLines(List<String> lines) {
        HashSet<String> set = new HashSet<>(lines);
        lines.clear();
        lines.addAll(new ArrayList<>(set));
    }

}
