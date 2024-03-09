package com.littlecode.setup.db.metadata.privates;

import com.littlecode.setup.db.metadata.MetaDataClasses;

import java.util.HashMap;
import java.util.List;

public class SetupDdlForPostgres extends SetupDdlForAnsi {
    public static final String FORMAT_CREATE_INDEX = "create %s index if exists %s on %s(%s)";
    private static final String FORMAT_ALTER_TABLE = "alter table if exists %s ";
    private static final String FORMAT_ALTER_TABLE_ADD_COLUMN = FORMAT_ALTER_TABLE + "add if not exists %s";
    private static final String FORMAT_CREATE_SCHEMA = "create schema if not exists %s";
    private static final String FORMAT_CREATE_TABLE = "create table if not exists %s(%s)";
    private static final String FORMAT_DROP_SCHEMA = "drop schema if exists %s cascade";
    private static final String FORMAT_DROP_TABLE = "drop table if exists %s cascade";
    private static final String FORMAT_EXTENSION_UUID = "create extension if not exists \"uuid-ossp\"";
    private static final String FORMAT_SET_DEFAULT_SCHEMA = "set search_path to %s";
    private static final String FORMAT_TABLE_FK = "alter table if exists %s add constraint %s foreign key (%s) references %s(%s)";
    private static final String FORMAT_TABLE_PK = "alter table if exists %s add constraint %s primary key (%s)";
    private static final String FORMAT_COMMENT_COLUMN = "comment on column %s.%s is '%s';";
    private static final HashMap<MetaDataClasses.DataType, String> DATA_TYPE_VS_SQL_TYPE = new HashMap<>();

    static {
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Boolean, "bool");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Date, "date");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.DateTime, "timestamp");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Time, "time");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Uuid, "uuid");
    }

    @Override
    public String fieldSQLType(MetaDataClasses.DataType dataType) {
        if (!DATA_TYPE_VS_SQL_TYPE.containsKey(dataType))
            return super.fieldSQLType(dataType);
        return DATA_TYPE_VS_SQL_TYPE.get(dataType);
    }

    @Override
    public List<String> SQL_DEFAULT_CMD() {
        return List.of(FORMAT_EXTENSION_UUID);
    }

    @Override
    public boolean isUpperCase() {
        return false;
    }

    @Override
    public String FORMAT_CREATE_SCHEMA() {
        return FORMAT_CREATE_SCHEMA;
    }

    @Override
    public String FORMAT_DROP_SCHEMA() {
        return FORMAT_DROP_SCHEMA;
    }

    @Override
    public String FORMAT_SET_DEFAULT_SCHEMA() {
        return FORMAT_SET_DEFAULT_SCHEMA;
    }

    @Override
    public String FORMAT_CREATE_TABLE() {
        return FORMAT_CREATE_TABLE;
    }

    @Override
    public String FORMAT_DROP_TABLE() {
        return FORMAT_DROP_TABLE;
    }

    @Override
    public String FORMAT_ALTER_TABLE() {
        return FORMAT_ALTER_TABLE;
    }

    @Override
    public String FORMAT_ALTER_TABLE_ADD_COLUMN() {
        return FORMAT_ALTER_TABLE_ADD_COLUMN;
    }

//    @Override
//    public String FORMAT_CONSTRAINT_NAME_FK() {
//        return SetupDdlInterface.super.FORMAT_CONSTRAINT_NAME_FK();
//    }

//    @Override
//    public String FORMAT_CONSTRAINT_NAME_PK(){
//        return SetupDdlInterface.super.FORMAT_CONSTRAINT_NAME_PK();
//    }

    @Override
    public String FORMAT_TABLE_FK() {
        return FORMAT_TABLE_FK;
    }

    @Override
    public String FORMAT_TABLE_PK() {
        return FORMAT_TABLE_PK;
    }

    @Override
    public String FORMAT_COMMENT_COLUMN() {
        return FORMAT_COMMENT_COLUMN;
    }

    @Override
    public String FORMAT_CREATE_INDEX() {
        return FORMAT_CREATE_INDEX;
    }


}