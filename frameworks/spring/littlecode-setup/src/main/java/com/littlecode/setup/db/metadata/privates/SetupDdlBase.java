package com.littlecode.setup.db.metadata.privates;

import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.setup.SetupMetaField;
import com.littlecode.setup.db.metadata.MetaDataClasses;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class SetupDdlBase implements SetupDdlInterface {
    private int objectSequence = 0;

    @Override
    public String fieldDefault(MetaDataClasses.MetaField field) {
        var __return = PrimitiveUtil.toString(field.getDefaultValue());
        if (!__return.isEmpty())
            return __return;

        var setupMetaField = field.getField().getType().getAnnotation(SetupMetaField.class);
        if (setupMetaField != null) {
            if (setupMetaField.ignore())
                return null;
            __return = PrimitiveUtil.toString(setupMetaField.defaultValue());
            if (!__return.isEmpty())
                return __return;
        }

        __return = (field.isPrimaryKey())
                ? null
                : (
                switch (field.getDataType()) {
                    case Double, Integer, BigInt -> "0";
                    case String -> "''";
                    case Boolean -> "false";
                    default -> null;
                }
        );
        return __return == null ? null : PrimitiveUtil.toString(__return);
    }



    @Override
    public String fieldScript(MetaDataClasses.MetaField field) {
        var sqlType = this.fieldSQLType(field.getDataType());
        if (sqlType.isEmpty())
            sqlType = String.format("[%s]", field.getFieldMetaType().getName());
        var type = new StringBuilder(field.getName());

        type
                .append(" ")
                .append(
                        switch (field.getDataType()) {
                            case Double -> String.format(sqlType, field.getLength(), field.getPrecision());
                            case String -> String.format(sqlType, field.getLength());
                            default -> sqlType;
                        }
                );
        if (this.isDefaultBeforeNotNull()) {
            var defaultValue = this.fieldDefault(field);
            if (defaultValue != null)
                type.append(SQL_DEFAULT()).append(defaultValue);
            if (!field.isNullable())
                type.append(SQL_NOT_NULL());
        } else {
            if (!field.isNullable())
                type.append(SQL_NOT_NULL());
            var defaultValue = this.fieldDefault(field);
            if (defaultValue != null)
                type.append(SQL_DEFAULT()).append(defaultValue);
        }
        return type.toString();
    }

    @Override
    public String parserName(String v) {
        if (v == null)
            return null;
        var maxLen = this.SQL_NAME_MAX_LENGTH();
        if (maxLen <= 0)
            return v;
        maxLen -= 5;
        var nameNew = v.trim();
        var nameLen = nameNew.length();
        if (nameLen <= maxLen)
            return v;
        nameNew = nameNew.substring(0, maxLen - 1) + "_" + String.format("%04d", ++objectSequence);
        return nameNew;
    }

    @Override
    public void makeSources(MetaDataClasses.MetaTable metaTable) {
        makeTableScript(metaTable);
        makeIndexesScript(metaTable);
    }

    private void makeIndexesScript(MetaDataClasses.MetaTable metaTable) {
        var indexes = metaTable.getMetaIndexes();
        metaTable
                .getIndexes()
                .clear();
        if (indexes == null || indexes.isEmpty())
            return;

        var tableName = metaTable.getTableName();
        var tableFullName = metaTable.getTableFullName();

        for (var index : indexes) {

            var indexFields = new StringBuilder();
            for (var fieldName : index.getFields())
                indexFields
                        .append(fieldName.replace(" ", "_"))
                        .append(" ");

            var indexName = index.getFinalName(tableName);
            indexName = this.parserName(indexName);

            var indexType = (index.getType() == null)
                    ? ""
                    : index.getType().toString();

            var indexUsing = (index.getUsing() == null)
                    ? ""
                    : "using " + index.getUsing().toString();

            var indexTable = tableFullName + " " + indexUsing;
            var indexScript = String.format(
                    this.FORMAT_CREATE_INDEX(),
                    indexType,
                    indexName,
                    indexTable,
                    indexFields.toString().trim().replace(" ", ",")
            );

            metaTable
                    .getIndexes()
                    .add(
                            this.isUpperCase()
                                    ? indexScript.toUpperCase()
                                    : indexScript.toLowerCase()
                    );

        }
    }

    private void makeTableScript(MetaDataClasses.MetaTable metaTable) {
        if (metaTable.getFields() == null)
            return;

        metaTable.clear();

        var tableName = metaTable.getTableName();
        var tableFullName = metaTable.getTableFullName();
        Function<List<String>, List<String>> tableDescAdd = new Function<List<String>, List<String>>() {
            @Override
            public List<String> apply(List<String> strings) {
                if (strings.isEmpty()) {
                    strings.add(SQL_COMMAND_COMMENT());
                    strings.add(SQL_COMMAND_COMMENT() + "table name: " + tableFullName);
                    strings.add(SQL_COMMAND_COMMENT());
                }
                return strings;
            }
        };
        tableDescAdd
                .apply(metaTable.getDrops())
                .add(String.format(FORMAT_DROP_TABLE(), tableFullName));


        List<MetaDataClasses.MetaField> fields = new ArrayList<>();
        Map<MetaDataClasses.MetaField, String> fieldsComment = new HashMap<>();
        for (var field : metaTable.getFields()) {
            if (field.isIgnored())
                continue;
            fields.add(field);
        }

        for (int i = 0; i < fields.size(); i++) {
            MetaDataClasses.MetaField field = fields.get(i);
            var fieldSource = fieldScript(field);
            if (i == 0) {
                tableDescAdd
                        .apply(metaTable.getTable())
                        .add(String.format(FORMAT_CREATE_TABLE(), tableFullName, fieldSource));
            } else {
                metaTable.getTable().add(String.format(FORMAT_ALTER_TABLE_ADD_COLUMN(), tableFullName, fieldSource));
            }

            var comment = field.getComment();
            if (!comment.isEmpty()) {
                fieldsComment.put(field, comment);
            }
        }

        if (!fieldsComment.isEmpty() && !FORMAT_COMMENT_COLUMN().isEmpty()) {
            metaTable.getTable().add(SQL_COMMAND_COMMENT() + "comments");
            fieldsComment.forEach((field, comment) -> {
                metaTable.getTable().add(String.format(FORMAT_COMMENT_COLUMN(), tableFullName, field.getName(), comment));
            });
        }

        var getTablePK = getTablePK(metaTable);
        if (!PrimitiveUtil.isEmpty(getTablePK)) {
            var constraintName = String.format(FORMAT_CONSTRAINT_NAME_PK(), tableName, getTablePK);
            constraintName = this.parserName(constraintName);
            tableDescAdd
                    .apply(metaTable.getConstraintsPK())
                    .add(String.format(FORMAT_TABLE_PK(), tableFullName, constraintName, getTablePK));
        }

        var getFieldsTable = metaTable.getForeignKeyFields();
        if (!getFieldsTable.isEmpty()) {
            if (metaTable.getConstraintsFK().isEmpty())
                tableDescAdd.apply(metaTable.getConstraintsFK());
            for (MetaDataClasses.MetaField field : metaTable.getForeignKeyFields()) {
                var refTable = field.asTable();
                refTable.setSchemaName(metaTable.getSchemaName());//default schema name, no use to set getSchemaName()
                var refTableName = refTable.getTableName();
                var refTableFullName = refTable.getTableFullName();
                var refPKFieldName = getTablePK(refTable);
                var fkFieldName = field.getName().replace(",", "_");
                var constraintName = String.format(FORMAT_CONSTRAINT_NAME_FK(), tableName, fkFieldName, refTableName, refPKFieldName);
                constraintName = this.parserName(constraintName);
                metaTable.getConstraintsFK().add(String.format(FORMAT_TABLE_FK(), tableFullName, constraintName, fkFieldName, refTableFullName, refPKFieldName));
            }
        }

    }

    private String getTablePK(MetaDataClasses.MetaTable metaTable) {
        for (var field : metaTable.getFields())
            if (field.isPrimaryKey())
                return field.getName();
        return "";
    }


}