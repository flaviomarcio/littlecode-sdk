package com.littlecode.setup.db.metadata.privates;

import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.setup.db.metadata.MetaDataClasses;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

@Slf4j
public class SetupDdlForOracle extends SetupDdlBase {
    private static final String FORMAT_DROP_TABLE = "DROP TABLE %s CASCADE CONSTRAINTS";
    private static final HashMap<MetaDataClasses.DataType, String> DATA_TYPE_VS_SQL_TYPE = new HashMap<>();

    static {
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.BigInt, "number(*, 0)");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Boolean, "number(1)");//COLUMN_NAME NUMBER(1) CHECK (COLUMN_NAME IN (0, 1))
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Date, "date");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.DateTime, "timestamp");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Integer, "number");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Time, "timestamp");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Uuid, "varchar(36)");// UUID after Oracle 12c
    }

    @Override
    public String fieldDefault(MetaDataClasses.MetaField field) {
        final var defaultValue=field.getDefaultValue();
        if(defaultValue instanceof Boolean aBoolean)
            return aBoolean?"1":"0";
        return super.fieldDefault(field);
    }

    @Override
    public String fieldSQLType(MetaDataClasses.DataType dataType) {
        if (!DATA_TYPE_VS_SQL_TYPE.containsKey(dataType))
            return super.fieldSQLType(dataType);
        return DATA_TYPE_VS_SQL_TYPE.get(dataType).toUpperCase();
    }


    @Override
    public boolean isDefaultBeforeNotNull() {
        return true;
    }

    @Override
    public int SQL_NAME_MAX_LENGTH() {
        return 30;
    }

    @Override
    public String FORMAT_CREATE_SCHEMA() {
        return "";//schema no exists in oracle
    }

    @Override
    public String FORMAT_DROP_SCHEMA() {
        return "";//schema no exists in oracle
    }

    @Override
    public String FORMAT_SET_DEFAULT_SCHEMA() {
        return "";//schema no exists in oracle
    }

    @Override
    public String FORMAT_DROP_TABLE() {
        return FORMAT_DROP_TABLE;
    }
}