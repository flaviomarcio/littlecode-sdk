package com.littlecode.parsers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.littlecode.config.CorePublicConsts;
import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.FileFormat;
import com.littlecode.files.IOUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
public class ObjectUtil {



    public static String classToName(Object o) {
        if (o != null) {
            if (o instanceof String string)
                return string;
            else if (o instanceof Class<?> aClass )
                return aClass.getName();
            else if (o.getClass().isEnum())
                return o.toString();
        }
        return "";
    }

    public static List<Class<?>> getClassesBySorted(Set<Class<?>> classes) {
        if (classes != null && !classes.isEmpty()) {
            List<Class<?>> __return = new ArrayList<>(classes);
            __return
                    .sort(new Comparator<Class<?>>() {
                        @Override
                        public int compare(Class<?> aClass1, Class<?> aClass2) {
                            return aClass1.getName().toLowerCase().compareTo(aClass2.getName().toLowerCase());
                        }
                    });
        }
        return new ArrayList<>();
    }

    public static Class<?> getClassByName(Object classType) {
        var className = classToName(classType);
        if (!className.isEmpty()) {
            try {
                return Class.forName(className);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static List<Class<?>> getClassesByInherits(Class<?> classType) {
        return new ArrayList<>();
    }

    public static List<Class<?>> getClassesByAnnotation(Class<? extends Annotation> annotationClass) {
        return new ArrayList<>();
    }

    public static void clear(Object object) {
        update(object, "{}");
    }

    public static boolean update(Object object, Object newValues) {
        if (object != null && newValues!=null){
            try {
                var objectMapper = UtilCoreConfig.newObjectMapper(UtilCoreConfig.FILE_FORMAT_DEFAULT);
                objectMapper.updateValue(object, newValues);
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public static boolean update(Object object, File newValues) {
        if (object != null && newValues!=null){
            try {
                var updateBytes = IOUtil.readAll(newValues).trim();
                var objectMapper = UtilCoreConfig.newObjectMapper(UtilCoreConfig.FILE_FORMAT_DEFAULT);
                objectMapper.updateValue(object, updateBytes);
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public static boolean update(Object object, Path newValues) {
        return update(object,newValues.toFile());
    }

    public static synchronized Field toFieldByName(Class<?> tClass, String fieldName) {
        if (fieldName != null && tClass != null) {
            fieldName = fieldName.trim().toLowerCase();
            Field[] fieldList = tClass.getDeclaredFields();
            for (Field field : fieldList) {
                if (field.getName().toLowerCase().equals(fieldName)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        return null;
    }

    public static synchronized Field toFieldByAnnotation(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        if (tClass != null && annotationClass != null) {
            Field[] fieldList = tClass.getDeclaredFields();
            for (Field field : fieldList)
                if (field.isAnnotationPresent(annotationClass))
                    return field;
        }
        return null;
    }

    public static synchronized List<Field> toFieldsByAnnotation(Class<?> tClass, Class<? extends Annotation> annotationClass) {
        if (tClass != null && annotationClass != null) {
            List<Field> __return = new ArrayList<>();
            Field[] fieldList = tClass.getDeclaredFields();
            for (Field field : fieldList)
                if (field.isAnnotationPresent(annotationClass))
                    __return.add(field);
            return __return;
        }
        return new ArrayList<>();
    }

    public static synchronized Field toFieldByType(Class<?> tClass, Class<?> typeClass) {
        if (tClass != null && typeClass != null) {
            Field[] fieldList = tClass.getDeclaredFields();
            for (Field field : fieldList) {
                if (field.getType().equals(typeClass))
                    return field;
            }
        }
        return null;
    }

    public static synchronized List<Field> toFieldsByType(Class<?> tClass, Class<?> typeClass) {
        if (tClass != null && typeClass != null) {
            List<Field> __return = new ArrayList<>();
            Field[] fieldList = tClass.getDeclaredFields();
            for (Field field : fieldList) {
                if (field.getType().equals(typeClass))
                    __return.add(field);
            }
            return __return;
        }
        return new ArrayList<>();
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
        return
                (o != null && !PrimitiveUtil.isPrimitiveValue(o))
                        ?toFieldsList(o.getClass())
                        :new ArrayList<>();
    }

    public static synchronized Map<String, Field> toFieldsMap(Class<?> tClass) {
        Map<String, Field> __return = new HashMap<>();
        toFieldsList(tClass)
                .forEach(field -> __return.put(field.getName(), field));
        return __return;
    }

    public static synchronized Map<String, Field> toFieldsMap(final Object o) {
        if (o != null)
            return toFieldsMap(o.getClass());
        return new HashMap<>();
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
            return (T) c.newInstance();
        } catch (Exception e) {
        }
        return null;
    }

    public static <T> T createWithArgsConstructor(Class<?> aClass, Object... initArgs) {
        if (initArgs != null && initArgs.length > 0) {
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

                    return (T) constructor.newInstance(initArgs);
                }

            } catch (Exception ignored) {
            }
            return null;
        }
        return create(aClass);
    }

    public static <T> T createFromString(Class<T> aClass, String source, FileFormat fileFormat) {
        var mapper = UtilCoreConfig.newObjectMapper(fileFormat);
        try {
            return mapper.readValue(source, aClass);
        } catch (Exception ignored) {
            log.error(ignored.getMessage());
        }
        return null;
    }

    public static <T> T createFromString(Class<T> aClass, String source) {
        return createFromString(aClass, source, ObjectValueUtil.FILE_FORMAT_DEFAULT);
    }

    public static <T> T createFromFile(Class<T> aClass, File source) {
        if(aClass!=null && source!=null){
            try {
                return createFromStream(aClass, new FileInputStream(source));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static <T> T createFromStream(Class<T> aClass, InputStream source) {
        if(aClass!=null && source!=null){
            try {
                return createFromString(aClass, new String(source.readAllBytes()), ObjectValueUtil.FILE_FORMAT_DEFAULT);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static <T> T createFromJSON(Class<T> claClass, String source) {
        return createFromString(claClass, source);
    }

    public static <T> T createFromYML(Class<T> claClass, String source) {
        return createFromString(claClass, source);
    }

    public static <T> T createFromXML(Class<T> claClass, String source) {
        return createFromString(claClass, source);
    }

    public static <T> T createFromPROPS(Class<T> aClass, String source) {
        return createFromString(aClass, source);
    }

    public static <T> T createFromValues(Class<T> aClass, final Map<String, Object> source) {
        if (aClass!=null && source != null && !source.isEmpty()) {
            var fieldsNew = toFieldsList(aClass);
            if (!fieldsNew.isEmpty()) {
                Map<String, Field> fieldsWriter = new HashMap<>();
                fieldsNew.forEach(field -> fieldsWriter.put(field.getName().toLowerCase(), field));
                if (!fieldsWriter.isEmpty()) {
                    Map<String, Object> finaMapValues = new HashMap<>();
                    for (Map.Entry<String, Object> entry : source.entrySet()) {
                        String fieldName = entry.getKey();
                        Object fieldValue = entry.getValue();
                        var fieldWrite = fieldsWriter.get(fieldName.trim().toLowerCase());
                        if (fieldWrite != null)
                            finaMapValues.put(fieldWrite.getName(), fieldValue);
                    }
                    return UtilCoreConfig.newModelMapper().map(finaMapValues, aClass);
                }
            }
        }
        return null;
    }

    public static <T> T createFromObject(Class<T> aClass, Object source) {
        if (aClass != null && source != null) {
            var objectValues = toMapObject(source);
            if (!objectValues.isEmpty())
                return createFromValues(aClass, objectValues);
        }
        return null;
    }

    public static String toString(Object o) {
        return ObjectValueUtil.toString(o, ObjectValueUtil.FILE_FORMAT_DEFAULT);
    }

    public static String toString(Object o, FileFormat fileFormat) {
        return ObjectValueUtil.toString(o,fileFormat);
    }

    public static synchronized Map<String, Object> toMapObject(final Object o) {
        if (o != null) {
            if (o instanceof String string) {
                var mapValues = toMapOfString(string);
                Map<String, Object> fieldValues = new HashMap<>();
                fieldValues.putAll(mapValues);
                return fieldValues;
            }
            else if(o instanceof Map map){
                return map;
            }
            else if(!PrimitiveUtil.isPrimitiveValue(o.getClass())){
                return ObjectValueUtil.of(o).asMap();
            }
        }
        return new HashMap<>();
    }

    public static synchronized Map<String, String> toMapOfString(final Object o) {
        if (o != null) {
            if(o instanceof String string) {
                var mapper = UtilCoreConfig.newObjectMapper(UtilCoreConfig.FILE_FORMAT_DEFAULT);
                try {
                    return mapper.readValue(string, Map.class);
                } catch (Exception e) {
                }
            }
            else if(o instanceof Map map){
                return map;
            }
            else{
                return ObjectValueUtil
                        .of(o)
                        .asMapString();
            }

        }
        return new HashMap<>();
    }


    public static String toMd5(Object o) {
        return HashUtil.toMd5(o);
    }

    public static UUID toMd5Uuid(Object o) {
        return HashUtil.toMd5Uuid(o);
    }

}
