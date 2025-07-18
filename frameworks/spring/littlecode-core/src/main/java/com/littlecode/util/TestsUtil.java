package com.littlecode.util;

import com.littlecode.parsers.PrimitiveUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
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
    @Setter
    private static boolean printLog = false;

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
                    var response = field.get(o);
                    if (response != null && !__return.contains(response))
                        __return.add(response);
                } catch (Exception ignored) {
                }
            }
        }
        return __return;
    }

    private static void checkGetterSetterFields(Object e) {
        if (PrimitiveUtil.isPrimitiveValue(e))
            return;
        if (printLog)
            log.info("Methods from: {}", e.getClass().getName());
        final var methods = getMethods(e.getClass());
        final var valuesFromObject = getObjectValues(e);
        for (var method : methods) {
            try {
                if (printLog)
                    log.info("  Method checking: {}():{}", method.getName(), method.getReturnType());
                method.setAccessible(true);

                if (method.getParameterCount() == 0) {
                    try {
                        if (Modifier.isStatic(method.getModifiers()))
                            method.invoke(null);
                        else
                            method.invoke(e);
                    } catch (Exception ignored) {
                    }
                } else if (method.getParameterCount() == 1) {
                    List<Object> list = new ArrayList<>();
                    var type = method.getParameterTypes()[0];
                    var typeObject = makeValueForClass(type);
                    if (typeObject != null)
                        list.add(typeObject);
                    list.addAll(valuesFromObject);
                    for (var value : list) {
                        try {
                            if (Modifier.isStatic(method.getModifiers()))
                                method.invoke(null, value);
                            else
                                method.invoke(e, value);
                        } catch (Exception ignored) {
                        }
                    }
                } else {
                    var ar = new Object[method.getParameterCount()];
                    for (var i = 0; i < ar.length; i++) {
                        try {
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
            } catch (Exception err) {
                log.error("  Method checking: {}():{}, error: {}", method.getName(), method.getReturnType(), err.getMessage());
            }
        }
        if (printLog)
            log.info("Fields from: {}", e.getClass().getName());
        var fields = getFields(e.getClass());
        for (var field : fields) {
            try {
                var fieldType = field.getType();
                if (printLog)
                    log.info("  Field checking : {}:{}", field.getName(), fieldType);
                field.setAccessible(true);
                var v = field.get(e);
                field.set(e, makeValueForClass(fieldType));
                field.set(e, v);
            } catch (Exception ignored) {
            }
        }
        //log.info("Finished: {}", e.getClass().getName());
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
        if (Object.class.equals(argType))
            return new Object();
        return null;
    }

    public static void checkObject(Object... list) {
        for (var e : list) {
            List<Object> objectList = new ArrayList<>();
            objectList.add(e);
            if (e instanceof List l) {
                objectList.addAll(l);
            } else if (e.getClass().isArray()) {
                int length = Array.getLength(e);
                for (int i = 0; i < length; i++)
                    objectList.add(Array.get(e, i));
            }
            synchronized (classes) {
                for (var o : objectList) {
                    if (!classes.contains(o.getClass()))
                        checkGetterSetterFields(o);
                    classes.add(o.getClass());
                }
            }
        }
    }
}
