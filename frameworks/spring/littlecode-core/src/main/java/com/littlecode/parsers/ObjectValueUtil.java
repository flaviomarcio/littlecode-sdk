package com.littlecode.parsers;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.files.FileFormat;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Getter
@ToString
public class ObjectValueUtil {
    public static final FileFormat FILE_FORMAT_DEFAULT = UtilCoreConfig.FILE_FORMAT_DEFAULT;
    private final List<String> fieldNames = new ArrayList<>();
    private final List<Field> fieldList = new ArrayList<>();
    private final Map<String, Field> fieldMap = new HashMap<>();
    private Object target;
    private Class targetClass;

    public ObjectValueUtil(Object target) {
        if (target == null)
            throw new NullPointerException("Target is null");
        this.target = target;
        this.targetClass = target.getClass();
        this.refresh();
    }

    public static ObjectValueUtil of(Object target) {
        return new ObjectValueUtil(target);
    }

    public static String toString(Object o, FileFormat fileFormat) {
        if (o != null) {
            if (o instanceof String v)
                return v;
            else if (o instanceof UUID v)
                return v.toString();
            else if (o instanceof URI v)
                return v.toString();
            else if (o instanceof Path v)
                return v.toString();
            else if (o instanceof LocalDate v)
                return v.toString();
            else if (o instanceof LocalTime v)
                return v.toString();
            else if (o instanceof LocalDateTime v)
                return v.toString();
            else if (o instanceof Integer v)
                return v.toString();
            else if (o instanceof Long v)
                return v.toString();
            else if (o instanceof Double v)
                return v.toString();
            else if (o instanceof Boolean v)
                return v.toString();
            else {
                try {
                    var mapper = UtilCoreConfig.newObjectMapper(fileFormat);
                    return mapper.writeValueAsString(o);
                } catch (Exception ignored) {
                }
            }
        }
        return "";
    }

    public static String toString(Object o) {
        return ObjectValueUtil.toString(o, FILE_FORMAT_DEFAULT);
    }

    public static Object getFieldValue(Object o, Field field) {
        try {
            field.setAccessible(true);
            return field.get(o);
        } catch (Exception ignored) {
            return null;
        }
    }

    public static List<Field> getFieldList(Class<?> tClass) {
        if (tClass != null) {
            try {
                List<Field> __return = new ArrayList<>();
                Field[] fieldList = tClass.getDeclaredFields();
                for (Field field : fieldList) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        __return.add(field);
                    }
                }
                return __return;
            } catch (Exception ignore) {
            }
        }
        return new ArrayList<>();
    }

    public void setTarget(Object target) {
        if (target == null)
            throw new NullPointerException("Target is null");
        this.target = target;
        this.targetClass = target.getClass();
        this.refresh();
    }

    public void refresh() {
        this.fieldList.clear();
        this.fieldMap.clear();
        this.fieldNames.clear();
        this.fieldList.addAll(getFieldList(this.target.getClass()));
        for (Field field : this.fieldList) {
            this.fieldMap.put(field.getName().toLowerCase(), field);
            this.fieldNames.add(field.getName());
        }
    }

    public ObjectValueUtil target(Object target) {
        if (target == null)
            throw ExceptionBuilder.ofNullPointer("Target is null");
        this.target = target;
        if (targetClass == null || !targetClass.equals(target.getClass())) {
            this.targetClass = target.getClass();
            this.refresh();
        }
        return this;
    }

    public Map<String, Object> asMap() {
        Map<String, Object> __return = new HashMap<>();
        for (Field field : this.fieldList) {
            var value = getFieldValue(this.target, field);
            __return.put(field.getName(), value);
        }
        return __return;
    }

    public Map<String, String> asMapString() {
        Map<String, String> __return = new HashMap<>();
        for (Field field : this.fieldList) {
            var oGet = getFieldValue(this.target, field);
            if (oGet == null)
                __return.put(field.getName(), "");
            else if (oGet.getClass().isEnum() || PrimitiveUtil.isPrimitiveValue(field.getGenericType()))
                __return.put(field.getName(), oGet.toString());
            else
                __return.put(field.getName(), PrimitiveUtil.toString(oGet));
        }
        return __return;
    }

    public boolean contains(String name) {
        return this.fieldMap.containsKey(name.toLowerCase());
    }

    public boolean contains(Field field) {
        return this.fieldMap.containsValue(field);
    }

    public Field getField(String name) {
        if (name == null)
            throw new NullPointerException("invalid fieldName");
        return this.fieldMap.get(name.trim().toLowerCase());
    }

    public Object getFieldValue(Field field) {
        if (field == null)
            throw new NullPointerException("invalid field");
        return getFieldValue(this.target, field);
    }

    public Object getFieldValue(String fieldName) {
        return this.getFieldValue(this.getField(fieldName));
    }

    public String toString() {
        return toString(this.target);
    }

    public Object asValue(Field field) {
        return this.getFieldValue(field);
    }

    public Object asValue(String fieldName) {
        return this.getFieldValue(fieldName);
    }

    public String asString(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toString();
    }

    public String asString(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toString();
    }

    public Integer asInt(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toInt();
    }

    public Integer asInt(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toInt();
    }

    public Long asLong(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toLong();
    }

    public Long asLong(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toLong();
    }

    public Double asDouble(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toDouble();
    }

    public Double asDouble(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toDouble();
    }

    public LocalDate asLocalDate(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toLocalDate();
    }

    public LocalDate asLocalDate(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toLocalDate();
    }

    public LocalTime asLocalTime(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toLocalTime();
    }

    public LocalTime asLocalTime(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toLocalTime();
    }

    public LocalDateTime asLocalDateTime(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toLocalDateTime();
    }

    public LocalDateTime asLocalDateTime(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toLocalDateTime();
    }

    public UUID asUUID(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toUUID();
    }

    public UUID asUUID(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toUUID();
    }

    public URI asURI(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toURI();
    }

    public URI asURI(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toURI();
    }

    public Path asPath(Field field) {
        return ConverterUtil.of(this.getFieldValue(field)).toPath();
    }

    public Path asPath(String fieldName) {
        return ConverterUtil.of(this.getFieldValue(fieldName)).toPath();
    }

}
