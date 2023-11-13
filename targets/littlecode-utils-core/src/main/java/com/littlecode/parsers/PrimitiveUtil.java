package com.littlecode.parsers;

import com.google.common.primitives.Doubles;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;

import java.math.BigDecimal;
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
    private static final LocalDateTime MIN_LOCALDATETIME = LocalDateTime.of(LocalDate.MIN, LocalTime.MIN);
    private static final LocalDate MIN_LOCALDATE = MIN_LOCALDATETIME.toLocalDate();
    private static final LocalTime MIN_LOCALTIME = MIN_LOCALDATETIME.toLocalTime();
    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_DATE_SHORT = "yy-MM-dd";
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String FORMAT_DATE_TIME_SHORT = "yyyy-MM-dd'T'HH:mm";
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_TIME_SHORT = "HH:mm";
    public static UUID toUUID(String v) {
        return HashUtil.toUuid(v);
    }
    public static long toLong(String v) {
        try {
            @SuppressWarnings("UnstableApiUsage")
            Long destination = Longs.tryParse(v);
            return destination == null ? 0 : destination;
        } catch (Exception e) {
            return 0;
        }
    }

    public static int toInt(String v) {
        try {
            @SuppressWarnings("UnstableApiUsage")
            Integer destination = Ints.tryParse(v);
            return destination == null ? 0 : destination;
        } catch (Exception e) {
            return 0;
        }
    }

    public static double toDouble(String v) {
        try {
            @SuppressWarnings("UnstableApiUsage")
            Double destination = Doubles.tryParse(v);
            return destination == null ? 0 : destination;
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



    public static LocalDate toDate(String v) {
        try {
            var value = String.valueOf(v).trim();
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

    public static LocalTime toTime(String v) {
        try {
            var value = String.valueOf(v).trim();
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

    public static LocalDateTime toDateTime(String v) {
        try {
            var value = String.valueOf(v).trim();
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

    public static boolean toBool(String v) {
        if (v == null)
            return false;
        v = v.trim().toLowerCase();
        if (v.equals("0") || v.equals("f"))
            return false;
        if (v.equals("1") || v.equals("t"))
            return true;
        try {
            var value = v.trim().toLowerCase();
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
}
