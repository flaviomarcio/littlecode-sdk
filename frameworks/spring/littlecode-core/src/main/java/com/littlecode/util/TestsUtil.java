package com.littlecode.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
public class TestsUtil {
    private static final List<Class<?>> classes = new ArrayList<>();

    private static List<Method> getMethods(Class<?> aClass) {
        List<Method> list = new ArrayList<>(Arrays.asList(aClass.getDeclaredMethods()));
        for (var method : aClass.getMethods()) {
            if (!list.contains(method))
                list.add(method);
        }
        return list;
    }

    private static List<Field> getFields(Class<?> aClass) {
//        List<Field> list = new ArrayList<>(Arrays.asList(aClass.getDeclaredFields()));
//        for (var field : aClass.getFields()){
//            if(!list.contains(field))
//                list.add(field);
//        }
//        return list;
        return List.of(aClass.getDeclaredFields());
    }


//    private static Method getM(Class<?> aClass, String name, @SuppressWarnings("SameParameterValue") Class<?> argType) {
//        try {
//            return aClass.getMethod(name, argType);
//        } catch (Exception ignored) {
//        }
//
//        try {
//            return aClass.getDeclaredMethod(name, argType);
//        } catch (Exception ignored) {
//        }
//
//        List<Method> methods = new ArrayList<>();
//        methods.addAll(Arrays.asList(aClass.getMethods()));
//        methods.addAll(Arrays.asList(aClass.getDeclaredMethods()));
//        for (Method method : methods) {
//            if (method.getName().equalsIgnoreCase(name))
//                return method;
//        }
//        return null;
//    }

//    private static Method getM(Class<?> aClass, String name) {
//        return getM(aClass, name, Object.class);
//    }
//
//    private static Object methodGetValue(Object e, String methodName) {
//        try {
//            var method = getM(e.getClass(), methodName);
//            return method.invoke(Modifier.isStatic(method.getModifiers())?null:e);
//        } catch (Exception ignored) {
//        }
//        return null;
//    }

//    private static void methodSetValue(Object e, String methodName, Object... args) {
//        try {
//            var method = getM(e.getClass(), methodName);
//            if (method != null){
//                var parent=Modifier.isStatic(method.getModifiers())?null:e;
//                if(method.getParameterCount()>=1){
//                    if(method.getParameterCount()>1)
//                        method.invoke(parent,args);
//                    else{
//                        for(var i:args)
//                            method.invoke(parent,i);
//                    }
//                }
//            }
//        } catch (Exception ignored) {
//        }
//    }
//
//    private static void checkGetterSetterEnum(Object e) {
//        List<Object> vJsonValues = new ArrayList<>();
//        for (var methodName : List.of("name", "ordinal", "getId", "getValue"))
//            vJsonValues.add(methodGetValue(e, methodName));
//
//        for (var methodName : List.of("valueOf", "of", "get"))
//            for (var vValue : vJsonValues)
//                methodSetValue(e, methodName, vValue);
//
//    }

    private static void checkGetterSetterFields(Object e) {
        log.info("Methods from: {}", e.getClass().getName());
        var methods = getMethods(e.getClass());
        for (var method : methods) {
            try {
                method.setAccessible(true);
                log.info("  Method checking: {}():{}", method.getName(), method.getReturnType());

                if (method.getParameterCount() == 0) {
                    if (Modifier.isStatic(method.getModifiers()))
                        method.invoke(null);
                    else
                        method.invoke(e);
                } else {
                    var ar = new Object[method.getParameterCount()];
                    for (var i = 0; i < ar.length; i++) {
                        var type = method.getParameterTypes()[i];
                        ar[i] = makeValueForClass(type);
                    }
                    if (Modifier.isStatic(method.getModifiers()))
                        method.invoke(null, ar);
                    else
                        method.invoke(e, ar);
                }
            } catch (Exception ignored) {
            }
        }
        log.info("Fields from: {}", e.getClass().getName());
        var fields = getFields(e.getClass());
        for (var field : fields) {
            try {
                var fieldType = field.getType();
                log.info("  Field checking : {}:{}", field.getName(), fieldType);
                field.setAccessible(true);
                var v = field.get(e);
                field.set(e, makeValueForClass(fieldType));
                field.set(e, v);
            } catch (Exception ignored) {
            }
        }
        log.info("Finished: {}", e.getClass().getName());
    }

//    private static void checkGetterSetter(Object e) {
//        List<Method> methods = getMethods(e.getClass());
//        for (var m : methods) {
//            if (m.getName().startsWith("get"))
//                methodGetValue(e, m.getName());
//            else if (m.getName().startsWith("is"))
//                methodGetValue(e, m.getName());
//            else if (m.getName().startsWith("set"))
//                methodSetValue(e, m.getName(), makeValueForSetMethod(m));
//        }
//    }

//    private static Object makeValueForSetMethod(Method m) {
//        return makeValueForClass(m.getParameters()[0].getType());
//    }

    private static Object makeValueForClass(Class<?> argType) {
        if (String.class.equals(argType))
            return ".";
        if (Integer.class.equals(argType) || int.class.equals(argType))
            return Integer.MAX_VALUE;
        if (Long.class.equals(argType) || long.class.equals(argType))
            return Long.MAX_VALUE;
        if (Short.class.equals(argType) || short.class.equals(argType))
            return Short.MAX_VALUE;
        if (BigDecimal.class.equals(argType))
            return new BigDecimal("0");
        if (Double.class.equals(argType) || double.class.equals(argType))
            return 0D;
        if (LocalDate.class.equals(argType))
            return LocalDate.now();
        if (LocalTime.class.equals(argType))
            return LocalTime.now();
        if (LocalDateTime.class.equals(argType))
            return LocalDateTime.now();
        if (Boolean.class.equals(argType) || boolean.class.equals(argType))
            return Boolean.TRUE;
        if (UUID.class.equals(argType))
            return UUID.randomUUID();
        if (URI.class.equals(argType))
            return URI.create("http://localhost:8080");
        return null;
    }

    public static void checkObject(Object... list) {
        for (var e : list) {
            synchronized (classes) {
                if (!classes.contains(e.getClass()))
                    checkGetterSetterFields(e);
                classes.add(e.getClass());
            }
        }
    }
}
