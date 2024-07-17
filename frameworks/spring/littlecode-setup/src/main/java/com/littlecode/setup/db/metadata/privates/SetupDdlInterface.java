package com.littlecode.setup.db.metadata.privates;

import com.littlecode.setup.db.metadata.MetaDataClasses;
import com.littlecode.setup.db.metadata.MetaDataUtil;

import java.util.ArrayList;
import java.util.List;

public interface SetupDdlInterface {

    default String fieldSQLType(MetaDataClasses.DataType dataType) {
        var sqlType = MetaDataUtil.getSQLFieldTypeByClass(dataType);
        return sqlType == null
                ? ""
                : sqlType;
    }

    default String fieldDefault(MetaDataClasses.MetaField field) {
        return null;
    }

    default String parserName(String v) {
        return v;
    }


    String fieldScript(MetaDataClasses.MetaField field);

    default boolean isUpperCase() {
        return true;
    }

    default boolean isDefaultBeforeNotNull() {
        return false;
    }

    void makeSources(MetaDataClasses.MetaTable metaTable);

    default List<String> SQL_DEFAULT_CMD() {
        return new ArrayList<>();
    }

    default String SQL_COMMAND_COMMENT() {
        return MetaDataClasses.SQL_COMMAND_COMMENT;
    }

    default String SQL_NOT_NULL() {
        return MetaDataClasses.SQL_NOT_NULL;
    }

    default String SQL_DEFAULT() {
        return MetaDataClasses.SQL_DEFAULT;
    }

    default int SQL_NAME_MAX_LENGTH() {
        return 0;
    }

    default String FORMAT_CREATE_SCHEMA() {
        return MetaDataClasses.FORMAT_CREATE_SCHEMA;
    }

    default String FORMAT_DROP_SCHEMA() {
        return MetaDataClasses.FORMAT_DROP_SCHEMA;
    }

    default String FORMAT_SET_DEFAULT_SCHEMA() {
        return MetaDataClasses.FORMAT_SET_DEFAULT_SCHEMA;
    }

    default String FORMAT_CREATE_TABLE() {
        return MetaDataClasses.FORMAT_CREATE_TABLE;
    }

    default String FORMAT_DROP_TABLE() {
        return MetaDataClasses.FORMAT_DROP_TABLE;
    }

    default String FORMAT_ALTER_TABLE() {
        return MetaDataClasses.FORMAT_ALTER_TABLE;
    }

    default String FORMAT_ALTER_TABLE_ADD_COLUMN() {
        return MetaDataClasses.FORMAT_ALTER_TABLE_ADD_COLUMN;
    }

    default String FORMAT_CONSTRAINT_NAME_FK() {
        return MetaDataClasses.FORMAT_CONSTRAINT_NAME_FK;
    }

    default String FORMAT_CONSTRAINT_NAME_PK() {
        return MetaDataClasses.FORMAT_CONSTRAINT_NAME_PK;
    }

    default String FORMAT_TABLE_FK() {
        return MetaDataClasses.FORMAT_TABLE_FK;
    }

    default String FORMAT_TABLE_PK() {
        return MetaDataClasses.FORMAT_TABLE_PK;
    }

    default String FORMAT_COMMENT_COLUMN() {
        return "";
    }

    default String FORMAT_CREATE_INDEX() {
        return isUpperCase()
                ? MetaDataClasses.FORMAT_CREATE_INDEX.toUpperCase()
                : MetaDataClasses.FORMAT_CREATE_INDEX;
    }

}
