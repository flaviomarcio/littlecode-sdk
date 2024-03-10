package com.littlecode.setup.db.metadata;

import jakarta.persistence.Table;
import lombok.extern.slf4j.Slf4j;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.UUID;

@Slf4j
public class MetaDataUtil {

    private static final HashMap<String, MetaDataClasses.DataType> DATA_TYPE_VS_CLASS = new HashMap<>();
    private static final HashMap<MetaDataClasses.DataType, String> DATA_TYPE_VS_SQL_TYPE = new HashMap<>();

    static {
        DATA_TYPE_VS_CLASS.put(String.class.getName(), MetaDataClasses.DataType.String);
        DATA_TYPE_VS_CLASS.put(Character.class.getName(), MetaDataClasses.DataType.String);
        DATA_TYPE_VS_CLASS.put("char", MetaDataClasses.DataType.String);
        DATA_TYPE_VS_CLASS.put(Boolean.class.getName(), MetaDataClasses.DataType.Boolean);
        DATA_TYPE_VS_CLASS.put("boolean", MetaDataClasses.DataType.Boolean);
        DATA_TYPE_VS_CLASS.put(Double.class.getName(), MetaDataClasses.DataType.Double);
        DATA_TYPE_VS_CLASS.put("double", MetaDataClasses.DataType.Double);
        DATA_TYPE_VS_CLASS.put(Integer.class.getName(), MetaDataClasses.DataType.Integer);
        DATA_TYPE_VS_CLASS.put("int", MetaDataClasses.DataType.Integer);
        DATA_TYPE_VS_CLASS.put(Long.class.getName(), MetaDataClasses.DataType.BigInt);
        DATA_TYPE_VS_CLASS.put("long", MetaDataClasses.DataType.BigInt);
        DATA_TYPE_VS_CLASS.put(BigInteger.class.getName(), MetaDataClasses.DataType.BigInt);
        DATA_TYPE_VS_CLASS.put(LocalDate.class.getName(), MetaDataClasses.DataType.Date);
        DATA_TYPE_VS_CLASS.put(LocalTime.class.getName(), MetaDataClasses.DataType.Time);
        DATA_TYPE_VS_CLASS.put(LocalDateTime.class.getName(), MetaDataClasses.DataType.DateTime);
        DATA_TYPE_VS_CLASS.put(UUID.class.getName(), MetaDataClasses.DataType.Uuid);

        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.String, "varchar(%d)");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Boolean, "bool");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Double, "numeric(%d,%d)");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Integer, "int");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.BigInt, "bigint");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Date, "date");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Time, "timestamp");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.DateTime, "timestamp");
        DATA_TYPE_VS_SQL_TYPE.put(MetaDataClasses.DataType.Uuid, "uuid");
    }

    public static MetaDataClasses.DataType getMetaTypeByClass(Class<?> aClass) {
        if (aClass.isEnum())
            aClass = Integer.class;
        if (!DATA_TYPE_VS_CLASS.containsKey(aClass.getName())) {
            if (aClass.isAnnotationPresent(Table.class)) {
                var table = new MetaDataClasses.MetaTable(aClass);
                for (var metaField : table.getFields()) {
                    if (metaField.isPrimaryKey())
                        return getMetaTypeByClass(metaField.getFieldMetaType());
                }
            }
            return MetaDataClasses.DataType.Unknown;
        }
        return DATA_TYPE_VS_CLASS.get(aClass.getName());
    }

    public static String getSQLFieldTypeByClass(MetaDataClasses.DataType dataType) {
        if (!DATA_TYPE_VS_SQL_TYPE.containsKey(dataType))
            return "";
        return DATA_TYPE_VS_SQL_TYPE.get(dataType);
    }
}
