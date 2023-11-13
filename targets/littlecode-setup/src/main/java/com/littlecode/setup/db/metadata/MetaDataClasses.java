package com.littlecode.setup.db.metadata;

import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.parsers.StringUtil;
import com.littlecode.setup.SetupDescription;
import com.littlecode.setup.privates.SetupClassesDB;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MetaDataClasses {

    public static final String SQL_COMMAND_COMMENT = "--";
    public static final String SQL_COMMAND_DELIMITER = ";";
    public static final String UNKNOWN = "UNKNOWN";
    public static final String FORMAT_EXTENSION_UUID = "create extension if not exists \"uuid-ossp\"";
    public static final String FORMAT_SET_DEFAULT_SCHEMA = "set search_path to %s";
    public static final String FORMAT_CREATE_SCHEMA = "create schema if not exists %s";
    public static final String FORMAT_CREATE_TABLE = "create table if not exists %s(%s)";
    public static final String FORMAT_ALTER_TABLE = "alter table if exists %s ";
    public static final String FORMAT_ALTER_TABLE_ADD_COLUMN = FORMAT_ALTER_TABLE + "add if not exists %s";
    @SuppressWarnings("unused")
    public static final String FORMAT_DROP_SCHEMA = "drop schema if exists %s cascade";
    public static final String FORMAT_DROP_TABLE = "drop table if exists %s cascade";
    //public static final String FORMAT_DROP_CONSTRAINT ="drop constraint if exists %s";
    public static final String FORMAT_CONSTRAINT_NAME_PK = "pk__%s_%s";
    public static final String FORMAT_CONSTRAINT_NAME_FK = "fk__%s_on_%s_%s";
    public static final String FORMAT_TABLE_PK = "alter table if exists %s add constraint %s primary key (%s)";
    public static final String FORMAT_TABLE_FK = "alter table if exists %s add constraint %s foreign key (%s) references %s(%s)";

    public enum DataType {
        Unknown, String, Boolean, Double, Integer, BigInt, Uuid, Date, Time, DateTime
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetaField {
        Boolean nullable;
        Boolean unique;
        Boolean primaryKey;
        Object defaultValue;
        Boolean ignored;
        private Class<?> fieldMetaType;
        private Field field;
        private DataType dataType;
        private String name;
        private Integer length;
        private Integer precision;

        public MetaField(Field field) {
            if (field == null)
                throw new RuntimeException("Invalid field type");
            this.field = field;
        }

        public Class<?> getFieldMetaType() {
            if (fieldMetaType != null)
                return this.fieldMetaType;
            if (this.isTable()) {
                var table = this.asTable();
                var pkField = table.getTablePKField();
                if (pkField != null)
                    this.fieldMetaType = pkField.getFieldMetaType();
            }
            if (this.fieldMetaType == null)
                this.fieldMetaType = this.field.getType();
            return this.fieldMetaType;
        }

        private Column column() {

            if (!field.isAnnotationPresent(Column.class))
                return null;
            return field.getAnnotation(Column.class);
        }

        private JoinColumn joinColumn() {

            if (!field.isAnnotationPresent(JoinColumn.class))
                return null;
            return field.getAnnotation(JoinColumn.class);
        }

        public String getComment() {
            if (!field.isAnnotationPresent(SetupDescription.class))
                return "";
            var value = field.getAnnotation(SetupDescription.class).value();
            return value == null ? "" : value.trim();
        }

        boolean isValid() {
            return !this.getName().isEmpty();
        }

        public String getName() {
            if (this.name != null)
                return this.name;

            if (this.isTable()) {
                var column = this.column();
                if (this.column() != null)
                    this.name = (column == null) ? "" : column.name();
                else {
                    var joinColumn = this.joinColumn();
                    this.name = (joinColumn == null) ? "" : joinColumn.name();
                }
            } else if (this.name == null) {
                this.name = field.getName();
            }

            if (this.name == null || this.name.trim().isEmpty())
                this.name = UNKNOWN;
            else {
                this.name = StringUtil
                        .target(this.name)
                        .toSnakeCase();
            }
            return this.name;
        }

        public DataType getDataType() {
            if (this.dataType != null)
                return this.dataType;
            return (this.dataType = MetaDataUtil.getMetaTypeByClass(this.getFieldMetaType()));
        }

        public Boolean isNullable() {
            if (this.nullable != null)
                return this.nullable;
            var column = this.column();
            if (column != null)
                this.nullable = column.nullable();
            else {
                var joinColumn = this.joinColumn();
                if (joinColumn != null)
                    this.nullable = joinColumn.nullable();
            }
            return this.nullable == null || Boolean.TRUE.equals(this.nullable);
        }

        public Boolean isUnique() {
            if (this.unique == null) {
                var column = this.column();
                if (column != null)
                    this.unique = column.unique();
                else {
                    var joinColumn = this.joinColumn();
                    if (joinColumn != null)
                        this.unique = joinColumn.unique();
                }
            }
            return this.unique != null && this.unique;
        }

        public Boolean isPrimaryKey() {
            if (this.primaryKey == null)
                this.primaryKey = this.field.isAnnotationPresent(Id.class) || this.isUnique();
            return this.primaryKey;
        }

        public Boolean isTable() {
            return this.field.getType().isAnnotationPresent(Table.class);
        }

        public MetaTable asTable() {
            if (!this.isTable())
                return null;
            return new MetaTable(this.field.getType());
        }

        public int getLength() {
            if (this.length == null || this.length <= 0) {
                var column = this.column();
                if (column != null)
                    this.length = column.length();
            }
            this.length = this.length == null ? 0 : this.length;
            if (this.getDataType() == DataType.Double && this.length == 0)
                this.length = 15;
            else if (this.getDataType() == DataType.String && this.length == 0)
                this.length = -1;
            return this.length == null ? 0 : this.length;
        }

        public Integer getPrecision() {
            if (this.precision == null) {
                var column = this.column();
                if (column != null)
                    this.precision = column.precision();
            }
            this.precision = this.precision == null ? 0 : this.precision;
            if (this.getDataType() == DataType.Double && this.precision == 0)
                this.precision = 5;
            return this.precision;
        }

        public boolean isIgnored() {
            if (this.ignored == null)
                this.ignored = this.field.isAnnotationPresent(Transient.class);
            ;
            return this.ignored;
        }

        public String getDefaultValue() {
            var __return =
                    this.defaultValue == null
                            ? null
                            : defaultValue.toString();
            if (this.isPrimaryKey())
                return __return;

            if (__return == null) {
                __return = switch (this.getDataType()) {
                    case Double, Integer, BigInt -> "0";
                    case String -> "''";
                    case Boolean -> "false";
//                    case Date -> null;
//                    case DateTime -> null;
//                    case Time -> null;
//                    case Uuid -> null;
//                    case Undefined -> null;
                    default -> null;
                };
            }
            return __return;
        }

        public String asSQLScript() {
            var sqlType = MetaDataUtil.getSQLFieldTypeByClass(this.getDataType());
            if (sqlType.isEmpty())
                sqlType = String.format("[%s]", this.getFieldMetaType().getName());
            var type = new StringBuilder(this.getName());

            type
                    .append(" ")
                    .append(
                            switch (this.getDataType()) {
                                case Double -> String.format(sqlType, this.getLength(), this.getPrecision());
                                case String -> String.format(sqlType, this.getLength());
                                default -> sqlType;
                            }
                    );
            if (!this.isNullable())
                type.append(" not null");
            var defaultValue = this.getDefaultValue();
            if (defaultValue != null)
                type.append(" default ").append(defaultValue);
            return type.toString();
        }
    }

    public static class MetaTable implements SetupClassesDB.ObjectBase {
        private final Class<?> tableMetaType;
        private final List<MetaField> fields = new ArrayList<>();
        @Getter
        private final List<String> drops = new ArrayList<>();
        @Getter
        private final List<String> schemas = new ArrayList<>();
        @Getter
        private final List<String> table = new ArrayList<>();
        @Getter
        private final List<String> indexes = new ArrayList<>();
        @Getter
        private final List<String> triggers = new ArrayList<>();
        @Getter
        private final List<String> constraintsPK = new ArrayList<>();
        @Getter
        private final List<String> constraintsFK = new ArrayList<>();
        private String schemaName;

        public MetaTable(Class<?> tableMetaType) {
            this.tableMetaType = tableMetaType;
        }

        private MetaField getTablePKField() {
            for (var field : this.getFields())
                if (field.isPrimaryKey())
                    return field;
            return null;
        }

        private String getTablePK() {
            for (var field : this.getFields())
                if (field.isPrimaryKey())
                    return field.getName();
            return "";
        }

        private Table getAnTable() {
            var anTable = (this.tableMetaType.isAnnotationPresent(Table.class))
                    ? this.tableMetaType.getAnnotation(Table.class)
                    : null;
            if (anTable == null)
                throw new RuntimeException("Invalid annotation: " + Table.class + " from model: " + this.tableMetaType);
            return anTable;
        }

        public String getSchemaName() {
            var anTable = getAnTable();
            var name = anTable.schema();
            if (!PrimitiveUtil.isEmpty(name))
                return name;
            return (this.schemaName == null) ? "" : this.schemaName;
        }

        public void setSchemaName(String newValue) {
            this.schemaName = newValue;
        }

        public String getTableName() {
            return getAnTable().name();
        }

        public String getTableFullName() {
            var anTable = getAnTable();
            var schemaName = getSchemaName();
            var tableName = StringUtil.target(anTable.name()).toSnakeCase();
            schemaName = (schemaName == null || schemaName.trim().isEmpty()) ? null : schemaName;
            return (schemaName == null)
                    ? tableName
                    : String.format("%s.%s", schemaName, tableName).toLowerCase();
        }

        public List<MetaField> getFieldsTable() {
            List<MetaField> fields = new ArrayList<>();
            this.getFields()
                    .forEach(metaField -> {
                        if (metaField.isTable())
                            fields.add(metaField);
                    });
            return fields;
        }

        public List<MetaField> getFields() {
            if (!fields.isEmpty())
                return this.fields;
            var modelFields = ObjectUtil.toFieldsList(this.tableMetaType);
            if (modelFields.isEmpty())
                return new ArrayList<>();
            List<MetaDataClasses.MetaField> metaFieldList = new ArrayList<>();
            modelFields.forEach(
                    field ->
                    {
                        var metaField = new MetaDataClasses.MetaField(field);
                        if (metaField.isValid())
                            metaFieldList.add(metaField);
                    });
            return metaFieldList;
        }

        public List<List<String>> listOfObject() {
            return List.of(
                    this.schemas,
                    this.table,
                    this.constraintsPK,
                    this.constraintsFK,
                    this.indexes,
                    this.triggers
            );
        }

        @Override
        public void clear() {
            listOfObject()
                    .forEach(List::clear);
        }

        @Override
        public SetupClassesDB.ObjectBase makeSources() {
            if (this.getFields() == null)
                return this;

            this.clear();

            var tableName = this.getTableName();
            var tableFullName = this.getTableFullName();
            Function<List<String>, List<String>> tableDescAdd = new Function<List<String>, List<String>>() {
                @Override
                public List<String> apply(List<String> strings) {
                    if (strings.isEmpty()) {
                        strings.add(SQL_COMMAND_COMMENT);
                        strings.add(SQL_COMMAND_COMMENT + "table name: " + tableFullName);
                        strings.add(SQL_COMMAND_COMMENT);
                    }
                    return strings;
                }
            };
            tableDescAdd
                    .apply(this.drops)
                    .add(String.format(FORMAT_DROP_TABLE, tableFullName));


            List<MetaField> fields = new ArrayList<>();
            Map<MetaField, String> fieldsComment = new HashMap<>();
            for (var field : this.getFields()) {
                if (field.isIgnored())
                    continue;
                fields.add(field);
            }

            for (int i = 0; i < fields.size(); i++) {
                MetaField field = fields.get(i);
                var fieldSource = field.asSQLScript();
                if (i == 0) {
                    tableDescAdd
                            .apply(this.table)
                            .add(String.format(FORMAT_CREATE_TABLE, tableFullName, fieldSource));
                }
                this.table.add(String.format(FORMAT_ALTER_TABLE_ADD_COLUMN, tableFullName, fieldSource));

                var comment = field.getComment();
                if (!comment.isEmpty()) {
                    fieldsComment.put(field, comment);
                }
            }

            if (!fieldsComment.isEmpty()) {
                this.table.add(SQL_COMMAND_COMMENT + "comments");
                fieldsComment.forEach((field, comment) -> {
                    this.table.add(String.format("comment on column %s.%s is '%s';", tableFullName, field.getName(), comment));
                });
            }


            var getTablePK = this.getTablePK();
            if (!PrimitiveUtil.isEmpty(getTablePK)) {
                var constraintName = String.format(FORMAT_CONSTRAINT_NAME_PK, tableName, getTablePK);
                tableDescAdd
                        .apply(this.constraintsPK)
                        .add(String.format(FORMAT_TABLE_PK, tableFullName, constraintName, getTablePK));
            }

            var getFieldsTable = this.getFieldsTable();
            if (!getFieldsTable.isEmpty()) {
                if (this.constraintsFK.isEmpty())
                    tableDescAdd.apply(this.constraintsFK);
                this.getFieldsTable()
                        .forEach(field -> {
                            var refTable = field.asTable();
                            refTable.setSchemaName(this.schemaName);//default schema name, no use to set getSchemaName()
                            var refTableName = refTable.getTableName();
                            var refTableFullName = refTable.getTableFullName();
                            var refPKFieldName = refTable.getTablePK();
                            var fkFieldName = field.getName().replace(",", "_");
                            var constraintName = String.format(FORMAT_CONSTRAINT_NAME_FK, fkFieldName, refTableName, refPKFieldName);
                            this.constraintsFK.add(String.format(FORMAT_TABLE_FK, tableFullName, constraintName, fkFieldName, refTableFullName, refPKFieldName));
                        });
            }


            return this;
        }
    }

}
