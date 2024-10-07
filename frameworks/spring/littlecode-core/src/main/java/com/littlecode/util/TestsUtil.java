package com.littlecode.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private static final String CLASS_NAME = TestsUtil.class.getSimpleName();
    //public static final String TEMP_DIR = System.getProperty("java.io.tmpdir").replace("\\", "/");

    private static Method getM(Class<?> aClass, String name, Class<?> argType) {
        try {
            return aClass.getMethod(name, argType);
        } catch (Exception ignored) {
        }

        try {
            return aClass.getDeclaredMethod(name, argType);
        } catch (Exception ignored) {
        }

        List<Method> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(aClass.getMethods()));
        methods.addAll(Arrays.asList(aClass.getDeclaredMethods()));
        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(name))
                return method;
        }
        return null;
    }

    private static Method getM(Class<?> aClass, String name) {
        return getM(aClass, name, Object.class);
    }

    public static Object methodGetValue(Object e, String methodName) {
        try {
            var method = getM(e.getClass(), methodName);
            if (method != null)
                method.invoke(e);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static void methodSetValue(Object e, String methodName, Object... args) {
        try {
            var method = getM(e.getClass(), methodName);
            if (method != null)
                method.invoke(e, args);
        } catch (InvocationTargetException | IllegalAccessException ignored) {
        }
    }

    public static void checkGetterSetterEnum(Object e) {
        if (!e.getClass().isEnum())
            return;
        List<Object> vJsonValues = new ArrayList<>();
        for (var methodName : List.of("name", "ordinal", "getId", "getValue"))
            vJsonValues.add(methodGetValue(e, methodName));

        for (var methodName : List.of("valueOf", "of", "get"))
            for (var vValue : vJsonValues)
                methodSetValue(e, methodName, vValue);

    }

    public static void checkGetterSetterFields(Object e) {
        final var aClass = e.getClass();
        try {
            for (var field : e.getClass().getDeclaredFields()) {
                {
                    field.setAccessible(true);
                    var v = field.get(e);
                    if (field.getType().equals(Boolean.class))
                        field.set(e, false);
                    else
                        field.set(e, v);
                }

                {
                    var method = getM(aClass, "get" + field.getName(), field.getType());
                    if (method != null)
                        method.invoke(e);
                }

                {
                    field.setAccessible(true);
                    var v = field.get(e);
                    var method = getM(aClass, "set" + field.getName(), field.getType());
                    if (method != null)
                        method.invoke(e, v);
                }
            }
        } catch (InvocationTargetException | IllegalAccessException ignored) {
        }
    }

    public static void checkGetterSetter(Object e) {
        var aClass = e.getClass();
        List<Method> methods = new ArrayList<>();
        methods.addAll(Arrays.asList(aClass.getMethods()));
        methods.addAll(Arrays.asList(aClass.getDeclaredMethods()));
        for (var m : methods) {
            if (m.getName().startsWith("get"))
                methodGetValue(e, m.getName());
            else if (m.getName().startsWith("set"))
                methodSetValue(e, m.getName(), makeValueForSetMethod(m));
        }
    }

    private static Object makeValueForSetMethod(Method m) {
        if (m.getParameterCount() > 0) {
            var argType = m.getParameters()[0].getType();
            if (Integer.class.equals(argType))
                return 0;
            else if (Double.class.equals(argType))
                return 0D;
            else if (LocalDate.class.equals(argType))
                return LocalDate.now();
            else if (LocalTime.class.equals(argType))
                return LocalTime.now();
            else if (LocalDateTime.class.equals(argType))
                return LocalDateTime.now();
            else if (Boolean.class.equals(argType))
                return Boolean.TRUE;
            else if (UUID.class.equals(argType))
                return UUID.randomUUID();
            else if (URI.class.equals(argType))
                return URI.create("http://localhost:8080");
            else if (argType.toString().equals("boolean"))
                return true;
            else if (argType.toString().equals("int"))
                return 0;
            else if (argType.toString().equals("double"))
                return 0D;
        }
        return null;
    }

    public static void checkObject(Object... list) {
        for (var e : list) {
            checkGetterSetterEnum(e);
            checkGetterSetterFields(e);
            checkGetterSetter(e);
        }
    }
}
