package com.littlecode.setup.db.metadata;

import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.parsers.StringUtil;
import com.littlecode.setup.SetupDescription;
import com.littlecode.setup.SetupMetaField;
import com.littlecode.setup.privates.SetupClassesDB;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MetaDataClasses {
    public static final String UNKNOWN = "UNKNOWN";
    public static final String SQL_COMMAND_COMMENT = "--";
    public static final String SQL_NOT_NULL = " not null ";
    public static final String SQL_DEFAULT = " default ";
    public static final String SQL_COMMAND_DELIMITER = ";";
    public static final String FORMAT_CONSTRAINT_NAME_FK = "fk__%s_%s_on_%s_%s";
    public static final String FORMAT_CONSTRAINT_NAME_PK = "pk__%s_%s";
    public static final String FORMAT_TABLE_FK = "alter table %s add constraint %s foreign key (%s) references %s(%s)";
    public static final String FORMAT_TABLE_PK = "alter table %s add constraint %s primary key (%s)";
    public static final String FORMAT_CREATE_SCHEMA = "create schema %s";
    public static final String FORMAT_CREATE_TABLE = "create table %s(%s)".toUpperCase();
    public static final String FORMAT_DROP_SCHEMA = "drop schema %s cascade".toUpperCase();
    public static final String FORMAT_DROP_TABLE = "drop table %s cascade".toUpperCase();
    public static final String FORMAT_SET_DEFAULT_SCHEMA = "set schema %s;";
    public static final String FORMAT_ALTER_TABLE = "alter table %s ".toUpperCase();
    public static final String FORMAT_ALTER_TABLE_ADD_COLUMN = FORMAT_ALTER_TABLE + "add %s".toUpperCase();

    public enum DataType {
        Unknown, String, Boolean, Double, Integer, BigInt, Uuid, Date, Time, DateTime
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetaField {
        private Boolean nullable;
        private Boolean unique;
        private Boolean primaryKey;
        private Boolean foreignKey;
        private Object defaultValue;
        private Boolean ignored;
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

        private Id idColumn() {
            if (!field.isAnnotationPresent(Id.class))
                return null;
            return field.getAnnotation(Id.class);
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
                return (this.nullable = column.nullable());
            else {
                var joinColumn = this.joinColumn();
                if (joinColumn != null)
                    return (this.nullable = joinColumn.nullable());
                else {
                    var id = this.idColumn();
                    if (id != null)
                        return (this.nullable = false);
                }
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
            if (this.primaryKey != null)
                return this.primaryKey;
            if (!(this.field.isAnnotationPresent(Id.class) || this.isUnique()))
                return (this.primaryKey = false);

            var setupMetaField = this.field.getType().getAnnotation(SetupMetaField.class);
            if (setupMetaField != null) {
                if (setupMetaField.ignore())
                    return (this.primaryKey = false);
                return (this.primaryKey = setupMetaField.primaryKeyIgnore());
            }
            return (this.primaryKey = true);
        }

        public Boolean isForeignKey() {
            if (!this.field.getType().isAnnotationPresent(Table.class))
                return (foreignKey = false);
            var setupMetaField = this.field.getType().getAnnotation(SetupMetaField.class);
            if (setupMetaField != null) {
                if (setupMetaField.ignore())
                    return (this.foreignKey = false);
                return (this.foreignKey = setupMetaField.foreignKeyIgnore());
            }
            return (this.foreignKey = true);
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
            if (this.getDataType() == DataType.Double && (this.length == 0 || this.length == 255))
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

        public List<MetaField> getForeignKeyFields() {
            List<MetaField> fields = new ArrayList<>();
            this.getFields()
                    .forEach(field -> {
                        if (field.isForeignKey())
                            fields.add(field);
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
    }

}
