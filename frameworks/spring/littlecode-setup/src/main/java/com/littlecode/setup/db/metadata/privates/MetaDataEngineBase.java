package com.littlecode.setup.db.metadata.privates;

import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.setup.SetupSetting;
import com.littlecode.setup.db.metadata.MetaDataClasses;
import com.littlecode.setup.privates.SetupClassesDB;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.jpa.vendor.Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

@Slf4j
public class MetaDataEngineBase<T> {
    private final List<MetaDataClasses.MetaTable> sources = new ArrayList<>();

//    public static Database databaseOf(Connection connection) {
//        if (connection == null)
//            return null;
//        try {
//            return databaseOf(connection.getMetaData().getDatabaseProductName());
//        } catch (SQLException e) {
//            return Database.H2;
//        }
//    }

    public static Database databaseOf(String databaseEnumName) {
        if (databaseEnumName == null || databaseEnumName.trim().isEmpty())
            return null;
        try {
            return Database.valueOf(databaseEnumName);
        } catch (Exception ex) {
            for (var e : Database.values()) {
                if (e.name().equalsIgnoreCase(databaseEnumName))
                    return e;
            }
        }
        return null;
    }

    public final List<MetaDataClasses.MetaTable> getSources() {
        return this.sources;
    }

    public Connection getConnection() {
        throw ExceptionBuilder.ofNoImplemented(this.getClass());
    }

    public Database databaseOf() {
        if (getConnection() == null)
            return null;
        try {
            return databaseOf(getConnection().getMetaData().getDatabaseProductName());
        } catch (SQLException e) {
            return Database.H2;
        }
    }

    public SetupDdlInterface ddlInterface(Database database) {
        if (database != null) {
            if (database.equals(Database.H2))
                return new SetupDdlForH2();
            else if (database.equals(Database.POSTGRESQL))
                return new SetupDdlForPostgres();
            else if (database.equals(Database.ORACLE))
                return new SetupDdlForOracle();
        }
        return new SetupDdlForAnsi();
    }

//    public SetupDdlInterface ddlInterface(String databaseEnumName) {
//        return ddlInterface(databaseOf(databaseEnumName));
//    }

//    public List<SetupDdlInterface> ddlInterface(Database[] databases) {
//        if (databases == null || databases.length == 0)
//            return new ArrayList<>();
//        List<SetupDdlInterface> __return = new ArrayList<>();
//        for (var e : databases) {
//            var i = this.ddlInterface(e.name());
//            if (i != null)
//                __return.add(i);
//        }
//        return __return;
//    }

//    public List<SetupDdlInterface> ddlInterface(List<Database> databases) {
//        if (databases == null || databases.isEmpty())
//            return new ArrayList<>();
//        List<SetupDdlInterface> __return = new ArrayList<>();
//        for (var e : databases) {
//            var i = this.ddlInterface(e.name());
//            if (i != null)
//                __return.add(i);
//        }
//        return __return;
//    }

    public SetupDdlInterface ddlInterface() {
        return this.ddlInterface(databaseOf());
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

//    public final Map<Database, List<SetupClassesDB.StatementItem>> getStatements(List<Database> database) {
//        if (database == null || database.isEmpty())
//            return new HashMap<>();
//
//        Map<Database, List<SetupClassesDB.StatementItem>> __return = new HashMap<>();
//
//        for (var e : database)
//            __return.put(e, getStatements(e));
//
//        return __return;
//    }

    public final List<SetupClassesDB.StatementItem> getStatements() {
        return getStatements(databaseOf());
    }

    public final List<SetupClassesDB.StatementItem> getStatements(Database database) {

        Map<SetupClassesDB.Target, SetupClassesDB.StatementItem> statementsMap = new HashMap<>();

        var defaultSchema = this.getSetting().getConfig().getDefaultSchema();
        List<String> schemaNames = new ArrayList<>();
        if (!PrimitiveUtil.isEmpty(defaultSchema))
            schemaNames.add(defaultSchema);

        if (database == null)
            return new ArrayList<>();

        var ddlInterface = this.ddlInterface(database);

        if (ddlInterface == null)
            throw ExceptionBuilder.ofFrameWork("Invalid database, %s", database);

        for (MetaDataClasses.MetaTable metaTable : this.getSources()) {
            ddlInterface
                    .makeSources(metaTable);
            var name = metaTable.getSchemaName().trim().toLowerCase();
            if (!PrimitiveUtil.isEmpty(name) && !schemaNames.contains(name))
                schemaNames.add(name);
        }

        if (!schemaNames.isEmpty()) {
            Collections.sort(schemaNames);
            deduplicateLines(schemaNames);

            for (String schemaName : schemaNames) {
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
                if (!ddlInterface.FORMAT_CREATE_SCHEMA().isEmpty()) {
                    sttIn.getSource().add(ddlInterface.SQL_COMMAND_COMMENT());
                    sttIn.getSource().add(ddlInterface.SQL_COMMAND_COMMENT() + "schema name: " + schemaName);
                    sttIn.getSource().add(ddlInterface.SQL_COMMAND_COMMENT());
                    sttIn.getSource().add(String.format(ddlInterface.FORMAT_CREATE_SCHEMA(), schemaName));
                    sttIn.getSource().add(String.format(ddlInterface.FORMAT_SET_DEFAULT_SCHEMA(), schemaName));
                }
                if (!ddlInterface.SQL_DEFAULT_CMD().isEmpty()) {
                    sttIn.getSource().add(ddlInterface.SQL_COMMAND_COMMENT());
                    sttIn.getSource().add(ddlInterface.SQL_COMMAND_COMMENT() + "default command: ");
                    sttIn.getSource().add(ddlInterface.SQL_COMMAND_COMMENT());
                    sttIn.getSource().addAll(ddlInterface.SQL_DEFAULT_CMD());
                }

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

                if (!ddlInterface.FORMAT_DROP_SCHEMA().isEmpty()) {
                    sttIn.getSource().add(ddlInterface.SQL_COMMAND_COMMENT());
                    sttIn.getSource().add(ddlInterface.SQL_COMMAND_COMMENT() + "schema name: " + schemaName);
                    sttIn.getSource().add(ddlInterface.SQL_COMMAND_COMMENT());
                    sttIn.getSource().add(String.format(ddlInterface.FORMAT_DROP_SCHEMA(), schemaName));
                }
            }
        }

        for (MetaDataClasses.MetaTable table : this.getSources()) {
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

        }

        List<SetupClassesDB.StatementItem> statementItemOut = new ArrayList<>();
        statementsMap.forEach((target, statementItem) -> {
            if (target.equals(SetupClassesDB.Target.Drops) && this.getSetting().getDatabase().getDDL().isSafeDrops())
                return;
            statementItemOut.add(statementItem);
        });
        return statementItemOut;
    }

    public T clear() {
        this.sources.clear();
        //noinspection unchecked
        return (T) this;
    }

    public T export() {
        //noinspection unchecked
        return (T) this;
    }

    private void cleanup(List<String> lines) {
        var ddlInterface = this.ddlInterface();
        for (var line : lines) {
            if (!PrimitiveUtil.isEmpty(line) && !line.startsWith(ddlInterface.SQL_COMMAND_COMMENT()))
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

        var ddlInterface = this.ddlInterface();

        drops.forEach(statement -> {
            if (!isSafeDrops) {
                __return.add(statement);
                return;
            }

            if (statement.startsWith(ddlInterface.SQL_COMMAND_COMMENT()))
                __return.add(statement);
            else
                __return.add(ddlInterface.SQL_COMMAND_COMMENT() + statement);
        });
        return __return;
    }

    private void deduplicateLines(List<String> lines) {
        HashSet<String> set = new HashSet<>(lines);
        lines.clear();
        lines.addAll(new ArrayList<>(set));
    }

}
