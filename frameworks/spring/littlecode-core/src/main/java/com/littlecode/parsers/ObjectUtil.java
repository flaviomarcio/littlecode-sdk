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

    public static final FileFormat FILE_FORMAT_DEFAULT = UtilCoreConfig.FILE_FORMAT_DEFAULT;

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
        if (o != null) {
            if (o instanceof String)
                return (String) o;
            else if (o instanceof Class<?> aClass)
                return aClass.getName();
            else if (o.getClass().isEnum())
                return o.toString();
        }
        return "";
    }

    private static List<Class<?>> getClassesBySorted(Set<Class<?>> classes) {
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
        update(object, ObjectUtil.create(object.getClass()));
    }

    public static boolean update(Object object, Object newValues) {
        return update(object, newValues, FILE_FORMAT_DEFAULT);
    }

    public static boolean update(Object object, Object newValues, FileFormat fileFormat) {
        if (object != null && newValues!=null){
            try {
                var updateBytes = IOUtil.readAll(newValues).trim();
                var updateValues = updateBytes.isEmpty()
                        ? newValues
                        : ObjectUtil.createFromString(object.getClass(), updateBytes);
                var objectMapper = UtilCoreConfig.newObjectMapper(fileFormat);
                objectMapper.updateValue(object, updateValues);
            } catch (Exception ignored) {
            }
        }
        return false;
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
        if (o != null && !PRIMITIVE_CLASSES.contains(o.getClass().getName()))
            return toFieldsList(o.getClass());
        return new ArrayList<>();
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

    public static <T> T createFromString(Class<T> aClass, String src, FileFormat fileFormat) {
        var mapper = UtilCoreConfig.newObjectMapper(fileFormat);
        try {
            return mapper.readValue(src, aClass);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static <T> T createFromString(Class<T> aClass, String src) {
        return createFromString(aClass, src, FILE_FORMAT_DEFAULT);
    }

    public static <T> T createFromFile(Class<T> aClass, File file) {
        try {
            return createFromStream(aClass, new FileInputStream(file));
        } catch (Exception ignored) {
        }
        return null;
    }

    public static <T> T createFromStream(Class<T> aClass, InputStream stream) {
        try {
            return createFromString(aClass, new String(stream.readAllBytes()), FILE_FORMAT_DEFAULT);
        } catch (Exception ignored) {
        }
        return null;
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
        if (aClass!=null && srcValues != null && !srcValues.isEmpty()) {
            var fieldsNew = toFieldsList(aClass);
            if (!fieldsNew.isEmpty()) {
                Map<String, Field> fieldsWriter = new HashMap<>();
                fieldsNew.forEach(field -> fieldsWriter.put(field.getName().toLowerCase(), field));
                if (!fieldsWriter.isEmpty()) {
                    Map<String, Object> finaMapValues = new HashMap<>();

                    for (Map.Entry<String, Object> entry : srcValues.entrySet()) {
                        String fieldName = entry.getKey();
                        Object fieldValue = entry.getValue();
                        try {
                            var fieldWrite = fieldsWriter.get(fieldName.trim().toLowerCase());
                            if (fieldWrite != null)
                                finaMapValues.put(fieldWrite.getName(), fieldValue);
                        } catch (Exception ignored) {
                        }
                    }
                    return UtilCoreConfig.newModelMapper().map(finaMapValues, aClass);
                }
            }
        }
        return null;
    }

    public static <T> T createFromObject(Class<T> aClass, Object objectSrc) {
        if (aClass != null && objectSrc != null) {
            var objectValues = toMapObject(objectSrc);
            if (!objectValues.isEmpty())
                return createFromValues(aClass, objectValues);
        }
        return null;
    }

    public static String toString(Object o) {
        return toString(o, FILE_FORMAT_DEFAULT);
    }

    public static String toString(Object o, FileFormat fileFormat) {
        if (o == null)
            return "";
        if (o instanceof String)
            return (String) o;

        try {
            var mapper = UtilCoreConfig.newObjectMapper(fileFormat);
            return mapper.writeValueAsString(o);
        } catch (Exception ignored) {
        }
        return "";
    }

    @Deprecated(since = "use function ObjectUtil.toString")
    public static String toJson(Object o) {
        return toString(o);
    }

    public static synchronized Map<String, Object> toMapObject(final Object o) {
        if (o != null) {
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
                                throw new FrameworkException(e.getMessage());
                            }
                        });
                return fieldValues;
            }
        }
        return new HashMap<>();
    }

    public static synchronized Map<String, String> toMapOfString(final Object o) {
        if (o != null) {
            if (o.getClass().equals(String.class)) {
                try {
                    var mapper = UtilCoreConfig.newObjectMapper(FILE_FORMAT_DEFAULT);
                    return mapper.readValue((String) o, Map.class);
                } catch (Exception e) {
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

                        } catch (Exception e) {
                            throw new FrameworkException(e.getMessage());
                        }
                    });
            return fieldValues;
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
