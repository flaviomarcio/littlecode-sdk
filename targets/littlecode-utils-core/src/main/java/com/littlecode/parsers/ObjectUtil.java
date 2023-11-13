package com.littlecode.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.FileFormat;
import com.littlecode.files.IOUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
public class ObjectUtil {

    public static final FileFormat FILE_FORMAT_DEFAULT = FileFormat.JSON;

    private static final List<String> PRIMITIVE_CLASSES = new ArrayList<>();


    static {
        PRIMITIVE_CLASSES.add(String.class.getName());
        PRIMITIVE_CLASSES.add(Byte.class.getName());
        PRIMITIVE_CLASSES.add(UUID.class.getName());
        PRIMITIVE_CLASSES.add(Boolean.class.getName());
        PRIMITIVE_CLASSES.add(Integer.class.getName());
        PRIMITIVE_CLASSES.add(Long.class.getName());
        PRIMITIVE_CLASSES.add(Double.class.getName());
        PRIMITIVE_CLASSES.add(BigDecimal.class.getName());
        PRIMITIVE_CLASSES.add(BigInteger.class.getName());
        PRIMITIVE_CLASSES.add(LocalDate.class.getName());
        PRIMITIVE_CLASSES.add(LocalTime.class.getName());
        PRIMITIVE_CLASSES.add(LocalDateTime.class.getName());
        PRIMITIVE_CLASSES.add("java.lang.Class<?>");
    }

    public static String classToName(Object o) {
        if (o == null)
            return "";
        if (o.getClass().equals(String.class))
            return (String) o;
        if (o.getClass().equals(Class.class)) {
            var aClass = ((Class<?>) o);
            return aClass.getName();
        }
        if (o.getClass().isEnum())
            return o.toString();
        return "";
    }

    public static Class<?> classByName(Object classType) {
        var className = classToName(classType);
        if (className.isEmpty())
            return null;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static void clear(Object object) {
        if (object == null)
            throw new FrameworkException("Invalid object");
        update(object, ObjectUtil.create(object.getClass()));
    }

    public static void update(Object object, Object newValues) {
        update(object, newValues, FILE_FORMAT_DEFAULT);
    }

    public static void update(Object object, Object newValues, FileFormat fileFormat) {
        if (object == null)
            throw new FrameworkException("Invalid object");

        if (newValues == null)
            throw new FrameworkException("Invalid new values");

        try {
            var updateBytes = IOUtil.readAll(newValues).trim();
            var updateValues = updateBytes.isEmpty()
                    ? newValues
                    : ObjectUtil.createFromString(object.getClass(), updateBytes);
            var objectMapper = UtilCoreConfig.newObjectMapper(fileFormat);
            objectMapper.updateValue(object, updateValues);
        } catch (JsonMappingException e) {
            throw new FrameworkException(e);
        }
    }

    public static synchronized Field toFieldByName(Class<?> tClass, String fieldName) {
        if (fieldName == null || tClass == null)
            return null;
        fieldName = fieldName.trim().toLowerCase();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList) {
            if (field.getName().toLowerCase().equals(fieldName)) {
                field.setAccessible(true);
                return field;
            }
        }
        return null;
    }

    public static synchronized Field toFieldByAnnotation(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        if (tClass == null || annotationClass==null)
            return null;
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList)
            if(field.isAnnotationPresent(annotationClass))
                return field;
        return null;
    }

    public static synchronized List<Field> toFieldsByAnnotation(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        if (tClass == null || annotationClass==null)
            return null;
        List<Field> __return=new ArrayList<>();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList)
            if(field.isAnnotationPresent(annotationClass))
                __return.add(field);
        return __return;
    }

    public static synchronized Field toFieldByType(Class<?> tClass, Class<?> typeClass) {
        if (tClass == null || typeClass==null)
            return null;
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList){
            if(field.getType().equals(typeClass))
                return field;
        }
        return null;
    }

    public static synchronized List<Field> toFieldsByType(Class<?> tClass, Class<?> typeClass) {
        if (tClass == null || typeClass==null)
            return null;
        List<Field> __return=new ArrayList<>();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList){
            if(field.getType().equals(typeClass))
                __return.add(field);
        }
        return __return;
    }

    public static synchronized <T> List<Field> toFieldsList(Class<T> tClass) {
        List<Field> __return = new ArrayList<>();
        Field[] fieldList = tClass.getDeclaredFields();
        for (Field field : fieldList) {
            if (Modifier.isStatic(field.getModifiers()))
                continue;
            field.setAccessible(true);
            __return.add(field);
        }
        return __return;
    }

    public static synchronized List<Field> toFieldsList(Object o) {
        if (o == null)
            return new ArrayList<>();
        if (PRIMITIVE_CLASSES.contains(o.getClass().getName()))
            return new ArrayList<>();
        return toFieldsList(o.getClass());
    }

    public static synchronized Map<String, Field> toFieldsMap(Class<?> tClass) {
        Map<String, Field> __return = new HashMap<>();
        toFieldsList(tClass)
                .forEach(field -> __return.put(field.getName(), field));
        return __return;
    }

    public static synchronized Map<String, Field> toFieldsMap(final Object o) {
        if (o == null)
            return new HashMap<>();
        return toFieldsMap(o.getClass());
    }

    public static synchronized boolean equal(final Object a, final Object b) {
        if (a == b)
            return true;
        if (a == null || b == null)
            return false;
        var md5A = toMd5Uuid(a);
        var md5B = toMd5Uuid(b);
        if (md5A == md5B)
            return true;
        if (md5A == null || md5B == null)
            return false;
        return md5A.toString().equals(md5B.toString());
    }

    public static <T> T create(Class<?> classType) {
        try {
            var c = classType.getConstructor();
            c.setAccessible(true);
            //noinspection unchecked
            return (T) c.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static <T> T createWithArgsConstructor(Class<?> aClass, Object... initArgs) {
        if (initArgs == null || initArgs.length == 0)
            return create(aClass);
        try {
            for (var constructor : aClass.getConstructors()) {
                final var types = constructor.getParameterTypes();
                if (types.length != initArgs.length)
                    continue;
                int iArg = 0;
                for (var type : types) {
                    var arg = initArgs[iArg++];
                    if (!arg.getClass().equals(type)) {
                        constructor = null;
                        break;
                    }
                }

                if (constructor == null)
                    continue;
                constructor.setAccessible(true);
                //noinspection unchecked
                return (T) constructor.newInstance(initArgs);
            }

        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
            return null;
        }
        return null;
    }

    public static <T> T createFromString(Class<T> aClass, String src, FileFormat fileFormat) {
        var mapper = UtilCoreConfig.newObjectMapper(fileFormat);
        try {
            return mapper.readValue(src, aClass);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public static <T> T createFromString(Class<T> aClass, String src) {
        return createFromString(aClass, src, FILE_FORMAT_DEFAULT);
    }

    public static <T> T createFromFile(Class<T> aClass, File file) {
        try{
            return createFromStream(aClass, new FileInputStream(file));
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    public static <T> T createFromStream(Class<T> aClass, InputStream stream) {
        try{
            return createFromString(aClass, new String(stream.readAllBytes()), FILE_FORMAT_DEFAULT);
        } catch (IOException e) {
            return null;
        }
    }

    public static <T> T createFromJSON(Class<T> claClass, String values) {
        return createFromString(claClass, values);
    }

    public static <T> T createFromYML(Class<T> claClass, String values) {
        return createFromString(claClass, values);
    }

    public static <T> T createFromXML(Class<T> claClass, String values) {
        return createFromString(claClass, values);
    }

    public static <T> T createFromPROPS(Class<T> aClass, String values) {
        return createFromString(aClass, values);
    }

    public static <T> T createFromValues(Class<T> aClass, final Map<String, Object> srcValues) {
        if (aClass == null) {
            log.debug("No aClass");
            return null;
        }
        if (srcValues == null || srcValues.isEmpty()) {
            log.debug("No source values to write class [{}]", aClass.getName());
            return null;
        }
        var fieldsNew = toFieldsList(aClass);
        if (fieldsNew.isEmpty()) {
            log.debug("No fields from [{}]", aClass.getName());
            return null;
        }

        Map<String, Field> fieldsWriter = new HashMap<>();
        fieldsNew.forEach(field -> fieldsWriter.put(field.getName().toLowerCase(), field));
        if (fieldsWriter.isEmpty()) {
            log.debug("No fields to write from class [{}]", aClass.getName());
            return null;
        }

        Map<String, Object> finaMapValues = new HashMap<>();

        srcValues.forEach((fieldName, fieldValue) -> {
            try {
                var fieldWrite = fieldsWriter.get(fieldName.trim().toLowerCase());
                if (fieldWrite == null)
                    return;
                finaMapValues.put(fieldWrite.getName(), fieldValue);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
        return UtilCoreConfig.newModelMapper().map(finaMapValues, aClass);
    }

    public static <T> T createFromObject(Class<T> aClass, Object objectSrc) {
        if (aClass == null) {
            log.debug("No aClass");
            return null;
        }
        if (objectSrc == null) {
            log.debug("No object source to write class [{}]", aClass.getName());
            return null;
        }
        var objectValues = toMapObject(objectSrc);
        if (objectValues.isEmpty())
            return null;
        return createFromValues(aClass, objectValues);
    }

    public static String toString(Object o) {
        return toString(o, FILE_FORMAT_DEFAULT);
    }

    public static String toString(Object o, FileFormat fileFormat) {
        if (o == null)
            return "";
        if (o.getClass().equals(String.class))
            return (String) o;
        try {
            var mapper = UtilCoreConfig.newObjectMapper(fileFormat);
            return mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    @Deprecated(since = "use function ObjectUtil.toString")
    public static String toJson(Object o) {
        return toString(o);
    }

    public static synchronized Map<String, Object> toMapObject(final Object o) {
        if (o == null)
            return new HashMap<>();

        if (o.getClass().equals(String.class)) {
            Map<String, Object> fieldValues = new HashMap<>();
            var mapValues = toMapOfString(o.toString());
            //noinspection CollectionAddAllCanBeReplacedWithConstructor
            fieldValues.putAll(mapValues);
            return fieldValues;
        } else if (PRIMITIVE_CLASSES.contains(o.getClass().getName())) {
            return new HashMap<>();
        } else {
            Map<String, Object> fieldValues = new HashMap<>();
            toFieldsList(o.getClass())
                    .forEach(field -> {
                        field.setAccessible(true);
                        try {
                            fieldValues.put(field.getName(), field.get(o));
                        } catch (IllegalAccessException e) {
                            throw new FrameworkException(e);
                        }
                    });
            return fieldValues;
        }
    }

    public static synchronized Map<String, String> toMapOfString(final Object o) {
        if (o == null)
            return new HashMap<>();

        if (o.getClass().equals(String.class)) {
            try {
                var mapper = UtilCoreConfig.newObjectMapper(FILE_FORMAT_DEFAULT);
                //noinspection unchecked
                return mapper.readValue((String) o, Map.class);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage());
            }
            return new HashMap<>();
        }


        Map<String, String> fieldValues = new HashMap<>();
        toFieldsList(o.getClass())
                .forEach(field -> {
                    field.setAccessible(true);
                    try {
                        var oGet = field.get(o);
                        if (oGet == null) {
                            fieldValues.put(field.getName(), "");
                            return;
                        }

                        if (oGet.getClass().isPrimitive() || oGet.getClass().isEnum() || PRIMITIVE_CLASSES.contains(field.getGenericType().getTypeName())) {
                            fieldValues.put(field.getName(), oGet.toString());
                            return;
                        }

                        if (oGet.getClass().isLocalClass())
                            fieldValues.put(field.getName(), toString(oGet));

                    } catch (IllegalAccessException e) {
                        throw new FrameworkException(e);
                    }
                });
        return fieldValues;
    }



    public static String toMd5(Object o) {
        return HashUtil.toMd5(o);
    }

    public static UUID toMd5Uuid(Object o) {
        return HashUtil.toMd5Uuid(o);
    }

}
