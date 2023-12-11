package com.littlecode.parsers;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PrimitiveUtil {
    private static final LocalDate MIN_LOCALDATE = LocalDate.of(1901,1,1);
    private static final LocalTime MAX_LOCALTIME = LocalTime.of(23,59,59,999);
    private static final LocalTime MIN_LOCALTIME = LocalTime.of(0,0,0,0);
    private static final LocalDateTime MIN_LOCALDATETIME = LocalDateTime.of(MIN_LOCALDATE, MIN_LOCALTIME);

    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_DATE_SHORT = "yy-MM-dd";
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String FORMAT_DATE_TIME_SHORT = "yyyy-MM-dd'T'HH:mm";
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_TIME_SHORT = "HH:mm";
    private static final int DOUBLE_PRECISION=6;
    public static UUID toUUID(String v) {
        return HashUtil.toUuid(v);
    }

    private static String formatDouble(Double d) {
        return formatDouble(d,DOUBLE_PRECISION);
    }
    private static String formatDouble(Double v, @SuppressWarnings("SameParameterValue") int precision) {
        var d=(v==null)?0:v;
        var format=precision<=0
                ?"#"
                :"#."+("#".repeat(precision));
        var symbols=new DecimalFormatSymbols(Locale.US);
        return (new DecimalFormat(format,symbols)).format(d);
    }

    public static long toLong(Object v) {
        try {
            if(v==null)
                return 0;
            if(v.getClass().equals(Boolean.class))
                return Boolean.parseBoolean(String.valueOf(v))?1:0;
            var in=toString(v);
            @SuppressWarnings("UnstableApiUsage")
            Long destination = Longs.tryParse(in);
            return destination == null ? 0 : destination;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int toInt(Object v) {
        try {
            if(v==null)
                return 0;
            if(v.getClass().equals(Boolean.class))
                return Boolean.parseBoolean(String.valueOf(v))?1:0;
            var in=toString(v);
            @SuppressWarnings("UnstableApiUsage")
            Integer destination = Ints.tryParse(in);
            return destination == null ? 0 : destination;
        } catch (Exception e) {
            return 0;
        }
    }

    public static double toDouble(Object v) {
        try {
            if(v==null)
                return 0;
            if(v.getClass().equals(Boolean.class))
                return Boolean.parseBoolean(String.valueOf(v))?1:0;
            else if(v.getClass().equals(Double.class))
                return (double) v;
            else{
                var in=toString(v);
                return Double.parseDouble(in);
            }
        } catch (Exception e) {
            return 0;
        }
    }

    public static double toDouble(Double v, int precision) {
        if(v==null || v.equals(0D))
            return 0;
        var decimal = new BigDecimal(v.toString());
        decimal = decimal.setScale(precision, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    public static double toDouble(String v, int precision) {
        var d=toDouble(v);
        return toDouble(d,precision);
    }

    public static LocalDate toDate(Object v) {
        try {
            var in=toString(v);
            var value = String.valueOf(in).trim();
            String format;
            if (value.length() == 10)
                format = FORMAT_DATE;
            else if (value.length() == 8)
                format = FORMAT_TIME_SHORT;
            else
                format = FORMAT_DATE;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalTime toTime(Object v) {
        try {
            var in=toString(v);
            var value = String.valueOf(in).trim();
            String format;
            if (value.length() == 8)
                format = FORMAT_TIME;
            else if (value.length() == 5)
                format = FORMAT_TIME_SHORT;
            else
                format = FORMAT_TIME;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalTime.parse(value, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static LocalDateTime toDateTime(Object v) {
        try {
            var in=toString(v);
            var value = String.valueOf(in).trim();
            String format;
            if (value.length() == 19)
                format = FORMAT_DATE_TIME;
            else if (value.length() == 16)//1901-01-01T00:00:00
                format = FORMAT_DATE_TIME_SHORT;
            else if (value.length() == 10)
                format = FORMAT_DATE;
            else if (value.length() == 8)
                format = FORMAT_DATE_SHORT;
            else
                format = FORMAT_DATE_TIME;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return LocalDateTime.parse(value, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean toBool(Object v) {
        var in = toString(v);
        if (in == null)
            return false;
        in = in.trim().toLowerCase();
        if (in.equals("0") || in.equals("f"))
            return false;
        if (in.equals("1") || in.equals("t"))
            return true;
        try {
            var value = in.trim().toLowerCase();
            return Boolean.parseBoolean(value);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isEmpty(String v) {
        return (v == null || v.trim().isEmpty());
    }

    public static boolean isEmpty(List<String> v) {
        return (v == null || v.isEmpty());
    }

    public static boolean isEmpty(Integer v) {
        return (v == null || v == 0);
    }

    public static boolean isEmpty(Long v) {
        return (v == null || v == 0);
    }

    public static boolean isEmpty(Double v) {
        return (v == null || v == 0);
    }

    public static boolean isEmpty(LocalDate v) {
        return (v == null || v.equals(MIN_LOCALDATE));
    }

    public static boolean isEmpty(LocalTime v) {
        return (v == null || v.equals(MIN_LOCALTIME));
    }

    public static boolean isEmpty(LocalDateTime v) {
        return (v == null || v.equals(MIN_LOCALDATETIME));
    }

    public static boolean isEmpty(Object v) {
        return (v == null);
    }

    public static boolean inRange(final double value, final double begin, final double end) {
        if (value == begin && begin == end)
            return true;

        return (value >= begin && value <= end);
    }

    public static boolean inRange(final LocalDate value, final LocalDate begin, final LocalDate end) {
        if ((value == null) || (begin == null && end == null))
            return false;

        var valueA = begin;
        var valueB = end;

        if (valueA == null)
            valueA = valueB;
        else if (valueB == null)
            valueB = valueA;

        return
                (value.equals(valueA) || value.isAfter(valueA)) &&
                        (value.equals(valueB) || value.isBefore(valueB))
                ;
    }

    public static boolean inRange(final LocalTime value, final LocalTime begin, final LocalTime end) {
        if ((value == null) || (begin == null && end == null))
            return false;

        var valueA = begin;
        var valueB = end;

        if (valueA == null)
            valueA = valueB;
        else if (valueB == null)
            valueB = valueA;

        return
                (value.equals(valueA) || value.isAfter(valueA)) &&
                        (value.equals(valueB) || value.isBefore(valueB))
                ;
    }

    public static boolean inRange(final LocalDateTime value, final LocalDateTime begin, final LocalDateTime end) {
        if ((value == null) || (begin == null && end == null))
            return false;

        var valueA = begin;
        var valueB = end;

        if (valueA == null)
            valueA = valueB;
        else if (valueB == null)
            valueB = valueA;


        return
                (value.equals(valueA) || value.isAfter(valueA)) &&
                        (value.equals(valueB) || value.isBefore(valueB))
                ;
    }

    public static String toString(Object v){
        if(v==null)
            return "";
        else if(v.getClass().equals(String.class))
            return ((String)v).trim();
        else if(v.getClass().equals(Class.class))
            return ((Class<?>)v).getName();
        else if(v.getClass().equals(Boolean.class))
            return ((Boolean)v).toString();
        else if(v.getClass().equals(Double.class))
            return formatDouble((Double)v);
        else if(v.getClass().equals(Integer.class) || v.getClass().equals(Long.class) || v.getClass().equals(BigInteger.class))
            return String.valueOf(v);
        else if(v.getClass().equals(LocalDateTime.class)){
            var d =(LocalDateTime)v;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);
            return formatter.format(d);
        }
        else if(v.getClass().equals(LocalDate.class)){
            var d =(LocalDate)v;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
            return formatter.format(d);
        }
        else if(v.getClass().equals(LocalTime.class)){
            var d =(LocalTime)v;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_TIME);
            return formatter.format(d);
        }
        return v.toString().trim();
    }

    public static String toString(int v){
        return String.valueOf(v);
    }

    public static String toString(long v){
        return String.valueOf(v);
    }

    public static String toString(double v){
        return formatDouble(v);
    }

    public static String toString(boolean v){
        return String.valueOf(v);
    }



}
