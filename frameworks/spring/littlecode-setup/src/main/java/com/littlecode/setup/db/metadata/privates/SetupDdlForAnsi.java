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
public class SetupDdlForAnsi extends SetupDdlBase {
    private final HashMap<MetaDataClasses.DataType, String> DATA_TYPE_VS_SQL_TYPE = new HashMap<>();

    public SetupDdlForAnsi() {
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.BigInt, "bigint");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Boolean, "boolean");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Date, "date");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.DateTime, "timestamp(6)");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Double, "numeric(%d,%d)");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Integer, "int");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.String, "varchar(%d)");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Time, "timestamp");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Uuid, "varchar(36)");// UUID after Oracle 12c
    }
    @Override
    public String fieldSQLType(MetaDataClasses.DataType dataType) {
        return
                (!DATA_TYPE_VS_SQL_TYPE.containsKey(dataType))
                        ?DATA_TYPE_VS_SQL_TYPE.get(dataType)
                        :super.fieldSQLType(dataType);
    }
}