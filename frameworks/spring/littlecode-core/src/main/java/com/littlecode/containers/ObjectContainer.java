package com.littlecode.containers;

import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import lombok.*;

import java.beans.Transient;
import java.util.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectContainer {
    private static final Map<String, Class<?>> CLASS_DICTIONARY = new HashMap<>();

    private UUID id;
    private Object type;
    private Object body;

    public static void classDictionaryClear() {
        synchronized (CLASS_DICTIONARY) {
            CLASS_DICTIONARY.clear();
        }
    }

    public static void classDictionaryRegister(Class<?> aClass) {
        if (aClass == null)
            return;
        synchronized (CLASS_DICTIONARY) {
            for (var s : classToListNames(aClass))
                CLASS_DICTIONARY.put(s, aClass);
        }
    }

    public static Class<?> classDictionaryByName(Object classType) {
        if(classType==null)
            return null;
        var className = ObjectUtil.classToName(classType);
        if (className.isEmpty())
            return null;
        synchronized (CLASS_DICTIONARY) {
            var __return = CLASS_DICTIONARY.get(className);
            if (__return != null)
                return __return;
        }
        return null;
    }

    public static String classToName(Object type) {
        return ObjectUtil.classToName(type);
    }

    public static Class<?> classBy(Object classType) {
        var className = ObjectUtil.classToName(classType);
        if (className.isEmpty())
            return null;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return classDictionaryByName(className);
        }
    }


    public static List<String> classToListNames(Object o) {
        if (o == null)
            return new ArrayList<>();

        if (o instanceof String string) {
            List<String> names = List.of(string.split("\\."));
            if (names.size() == 1)
                return List.of(string);
            return List.of(string, names.get(names.size() - 1));
        }

        var aClass=
                (o instanceof Class aClass1)
                        ?aClass1
                        :o.getClass();

        return List.of(aClass.getName(), aClass.getSimpleName(), aClass.toString());
    }

    public static ObjectContainer from(UUID id, Object type, Object body) {
        return ObjectContainer
                .builder()
                .id(id == null ? UUID.randomUUID() : id)
                .type(type)
                .body(body)
                .build();
    }

    public static ObjectContainer of(UUID id, Object type, Object body) {
        return from(
                (id == null ? UUID.randomUUID() : id),
                classToString(type),
                body
        );
    }

    public static ObjectContainer of(UUID id, Object taskObject) {
        var aClass=taskObject==null?null:taskObject.getClass();
        return of(id, aClass, taskObject);
    }

    public static ObjectContainer of(Object type, Object taskObject) {
        return of(null, type, taskObject);
    }

    public static ObjectContainer of(Object taskObject) {
        var aClass=taskObject==null?null:taskObject.getClass();
        return of(null, aClass, taskObject);
    }

    public static String classToString(Object o) {
        if(o==null || (o instanceof String))
            return "";

        if(o instanceof Class aClass){
            if(aClass==String.class)
                return "";
            return aClass.getName();
        }

        return o.getClass().getName();
    }


    public UUID getChecksum() {
        return HashUtil.toMd5Uuid(this.asString());
    }

    public boolean canType(final Object type) {
        if (type == null || this.getType() == null)
            return false;

        var eA = classToString(type).toLowerCase();
        var eB = this.getType().toString().toLowerCase();
        return eA.equals(eB);
    }

    @Transient
    public String getTypeName() {
        return ObjectContainer.classToName(this.type);
    }

    @Transient
    public Class<?> getTypeClass() {
        return ObjectContainer.classBy(this.type);
    }

    @Transient
    public String asString() {
        return ObjectUtil.toString(this.body);
    }

    @Transient
    public <T> T asObject(Class<T> aClass) {
        return ObjectUtil.createFromString(aClass, this.asString());
    }

    @Transient
    public <T> T asObject(String className) {
        if(className==null || className.trim().isEmpty())
            return null;

        Class<?> aClass = ObjectContainer.classBy(className);
        if (aClass == null)
            throw ExceptionBuilder.ofFrameWork("Class not found");
        var o = ObjectUtil.createFromString(aClass, this.asString());
        return (T) o;
    }
}
