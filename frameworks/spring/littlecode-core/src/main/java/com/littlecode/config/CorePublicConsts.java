package com.littlecode.config;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CorePublicConsts {
    public CorePublicConsts(){
    }
    public static final LocalDate MIN_LOCALDATE = LocalDate.of(1901, 1, 1);
    public static final LocalTime MAX_LOCALTIME = LocalTime.of(23, 59, 59, 999);
    public static final LocalTime MIN_LOCALTIME = LocalTime.of(0, 0, 0, 0);
    public static final LocalDateTime MIN_LOCALDATETIME = LocalDateTime.of(MIN_LOCALDATE, MIN_LOCALTIME);
    public static final LocalDateTime MIN_LOCALDATETIME_MAX_TIME = LocalDateTime.of(MIN_LOCALDATE, MAX_LOCALTIME);

    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String FORMAT_DATE_TIME_MS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    public static final String FORMAT_DATE_TIME_HH = "yyyy-MM-dd'T'HH";
    public static final String FORMAT_DATE_TIME_HH_MM = "yyyy-MM-dd'T'HH:mm";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_TIME_MS = "HH:mm:ss.SSSSSSSSS";
    public static final String FORMAT_TIME_HH_MM = "HH:mm";
    public static final int DOUBLE_PRECISION = 6;

    public static final ConcurrentHashMap<String,Class<?>> PRIMITIVE_CLASSES=new ConcurrentHashMap<>();


    static {
        var list=List.of(
        String.class,
        Byte.class,
        UUID.class,
        Boolean.class,
        Integer.class,
        Long.class,
        Double.class,
        BigDecimal.class,
        BigInteger.class,
        LocalDate.class,
        LocalTime.class,
        LocalDateTime.class
        );
        list.forEach(aClass -> PRIMITIVE_CLASSES.put(aClass.getName(),aClass));
        PRIMITIVE_CLASSES.put("java.lang.Class<?>",java.lang.Class.class);
    }

}
