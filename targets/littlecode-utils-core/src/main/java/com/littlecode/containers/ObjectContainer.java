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
            for(var s:classToListNames(aClass))
                CLASS_DICTIONARY.put(s, aClass);
        }
    }

    public static Class<?> classDictionaryByName(Object classType) {
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

    public static String classToName(Object type){
        return ObjectUtil.classToName(type);
    }

    public static Class<?> classByName(Object classType) {
        var className = ObjectUtil.classToName(classType);
        if (className.isEmpty())
            return null;
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return classDictionaryByName(className);
        }
    }



    private static List<String> classToListNames(Object o) {
        if (o == null)
            return new ArrayList<>();

        if (o.getClass().equals(String.class)) {
            var name = (String) o;
            List<String> names = List.of(name.split("\\."));
            if (names.size() == 1)
                return List.of(name);
            return List.of(name, names.get(names.size() - 1));
        }

        var aClass = o.getClass().equals(Class.class)
                ? ((Class<?>) o)
                : o.getClass();
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
        return of(id, taskObject.getClass(), taskObject);
    }

    public static ObjectContainer of(Object type, Object taskObject) {
        return of(null, type, taskObject);
    }

    public static ObjectContainer of(Object taskObject) {
        return of(null, taskObject.getClass(), taskObject);
    }

    private static String classToString(Object o) {
        var typeName = (o == null)
                ? "" :
                o.getClass().equals(String.class)
                        ? ""
                        :
                        o.getClass().equals(Class.class)
                                ? ((Class<?>) o).getName()
                                : o.toString();
        if (typeName.equals(String.class.getName()))
            return "";
        return typeName;
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
    public String getTypeName(){
        return ObjectContainer.classToName(this.type);
    }

    @Transient
    public Class<?> getTypeClass(){
        return ObjectContainer.classByName(this.type);
    }

    @Transient
    public String asString() {
        return ObjectUtil.toString(this.body);
    }
    @Transient
    public <T> T asObject(Class<T> valueType) {
        return ObjectUtil.createFromString(valueType, this.asString());
    }
    @Transient
    public <T> T asObject(Object valueType) {
        Class<?> aClass = ObjectContainer.classByName(valueType);
        if (aClass == null)
            throw ExceptionBuilder.ofFrameWork("Class not found");
        var o = ObjectUtil.createFromString(aClass, this.asString());
        //noinspection unchecked
        return (T) o;
    }
}
