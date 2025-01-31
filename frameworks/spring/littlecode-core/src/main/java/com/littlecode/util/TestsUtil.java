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
import java.util.*;

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
        return List.of(aClass.getDeclaredFields());
    }

    private static List<Object> getObjectValues(Object o) {
        List<Object> __return = new ArrayList<>();
        {//methods
            var methods = getMethods(o.getClass());
            for (var method : methods) {
                try {
                    method.setAccessible(true);
                    if (method.getParameterCount() == 0) {
                        if (Modifier.isStatic(method.getModifiers())) {
                            var response = method.invoke(null);
                            if (response != null && !__return.contains(response))
                                __return.add(response);
                        } else {
                            var response = method.invoke(o);
                            if (response != null)
                                __return.add(response);
                        }
                    }
                } catch (Exception ignored) {
                }
            }
        }
        {//methods
            var fields = getFields(o.getClass());
            for (var field : fields) {
                try {
                    field.setAccessible(true);
                    var response=field.get(o);
                    if (response != null && !__return.contains(response))
                        __return.add(response);
                } catch (Exception ignored) {
                }
            }
        }
        return __return;
    }

    private static void checkGetterSetterFields(Object e) {
        log.info("Methods from: {}", e.getClass().getName());
        var methods = getMethods(e.getClass());
        for (var method : methods) {
            try {
                method.setAccessible(true);
                log.info("  Method checking: {}():{}", method.getName(), method.getReturnType());

                if (method.getParameterCount() == 0) {
                    try {
                        if (Modifier.isStatic(method.getModifiers()))
                            method.invoke(null);
                        else
                            method.invoke(e);
                    } catch (Exception ignored) {
                    }
                }
                else if(method.getParameterCount() == 1) {
                    var valuesFromClass = getObjectValues(e);
                    for(var value : valuesFromClass) {
                        try{
                            if (Modifier.isStatic(method.getModifiers()))
                                method.invoke(null, value);
                            else
                                method.invoke(e, value);
                        } catch (Exception ignored) {
                        }
                    }
                }
                else {
                    var ar = new Object[method.getParameterCount()];
                    for (var i = 0; i < ar.length; i++) {
                        try{
                            var type = method.getParameterTypes()[i];
                            ar[i] = makeValueForClass(type);
                            if (Modifier.isStatic(method.getModifiers()))
                                method.invoke(null, ar);
                            else
                                method.invoke(e, ar);
                        } catch (Exception ignored) {
                        }
                    }
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
