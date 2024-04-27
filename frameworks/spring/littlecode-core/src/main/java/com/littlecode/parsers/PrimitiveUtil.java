package com.littlecode.parsers;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class PrimitiveUtil {
    private static final LocalDate MIN_LOCALDATE = LocalDate.of(1901, 1, 1);
    private static final LocalTime MIN_LOCALTIME = LocalTime.of(0, 0, 0, 0);
    private static final LocalDateTime MIN_LOCALDATETIME = LocalDateTime.of(MIN_LOCALDATE, MIN_LOCALTIME);

    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_DATE_SHORT = "yy-MM-dd";
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String FORMAT_DATE_TIME_MS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    private static final String FORMAT_DATE_TIME_SHORT = "yyyy-MM-dd'T'HH:mm";
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_TIME_MS = "HH:mm:ss.SSSSSSSSS";
    private static final String FORMAT_TIME_SHORT = "HH:mm";
    private static final int DOUBLE_PRECISION = 6;

    public static UUID toUUID(String v) {
        return HashUtil.toUuid(v);
    }

    private static String formatDouble(Double v, int precision) {
        var d = (v == null) ? 0 : v;
        var format = precision <= 0
                ? "#"
                : "#." + ("#".repeat(precision));
        var symbols = new DecimalFormatSymbols(Locale.US);
        return (new DecimalFormat(format, symbols)).format(d);
    }

    private static String formatDouble(Double d) {
        return formatDouble(d, DOUBLE_PRECISION);
    }

    public static long toLong(String v) {
        if (v != null) {
            try {
                return Long.parseLong(v);
            } catch (Exception ignored) {
            }
        }
        return 0;
    }

    public static long toLong(boolean v) {
        return v ? 1 : 0;
    }

    public static long toLong(double v) {
        return (long) v;
    }

    public static int toInt(String v) {
        if (v != null) {
            try {
                return Integer.parseInt(v);
            } catch (Exception ignored) {
            }
        }
        return 0;
    }

    public static int toInt(double v) {
        return (int) v;
    }

    public static int toInt(long v) {
        return (int) v;
    }

    public static int toInt(boolean v) {
        return v ? 1 : 0;
    }

    public static double toDouble(String v) {
        try {
            return Double.parseDouble(v);
        } catch (Exception ignored) {
        }
        return 0;
    }

    public static int toDouble(boolean v) {
        return v ? 1 : 0;
    }

    public static double toDouble(Double v, int precision) {
        if (v == null || v.equals(0D))
            return 0;
        var decimal = new BigDecimal(v.toString());
        decimal = decimal.setScale(precision, RoundingMode.HALF_UP);
        return decimal.doubleValue();
    }

    public static double toDouble(String v, int precision) {
        var d = toDouble(v);
        return toDouble(d, precision);
    }

    public static LocalDate toDate(String v) {
        if (v != null) {
            try {
                var value = v.trim();
                String format;
                if (value.length() == 10)
                    format = FORMAT_DATE;
                else if (value.length() == 8)
                    format = FORMAT_TIME_SHORT;
                else
                    format = FORMAT_DATE;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDate.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static LocalTime toTime(String v) {
        if (v != null) {
            try {
                var value = v.trim();
                String format;
                if (value.length() == 5)
                    format = FORMAT_TIME_SHORT;
                else if (value.length() == 8)
                    format = FORMAT_TIME;
                else if (value.length() == 18)
                    format = FORMAT_TIME_MS;
                else
                    format = FORMAT_TIME;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalTime.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static LocalDateTime toDateTime(String v) {
        if (v != null) {
            try {
                var value = v.trim();
                String format;
                if (value.length() == 19)
                    format = FORMAT_DATE_TIME;
                else if (value.length() == 16)//1901-01-01T00:00:00
                    format = FORMAT_DATE_TIME_SHORT;
                else if (value.length() == 29)//1901-01-01T00:00:00
                    format = FORMAT_DATE_TIME_MS;
                else if (value.length() == 10)
                    format = FORMAT_DATE;
                else if (value.length() == 8)
                    format = FORMAT_DATE_SHORT;
                else
                    format = FORMAT_DATE_TIME;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDateTime.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static boolean toBool(String v) {
        if (v != null) {
            var in = v.trim().toLowerCase();
            return in.equals("true") || in.equals("1") || in.equals("t");
        }
        return false;
    }

    public static boolean toBool(double v) {
        return v == 1;
    }

    public static boolean isEmpty(String v) {
        return (v == null || v.trim().isEmpty());
    }

    public static boolean isEmpty(List<?> v) {
        return (v == null || v.isEmpty());
    }

    public static boolean isEmpty(Map<?, ?> v) {
        return (v == null || v.isEmpty());
    }

    public static boolean isEmpty(double v) {
        return (v == 0);
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
        return (value >= begin && value <= end);
    }

    public static boolean inRange(final LocalDate value, final LocalDate begin, final LocalDate end) {
        return ((value != null) && (begin != null && end != null)) && (!value.isBefore(begin) && !value.isAfter(end));
    }

    public static boolean inRange(final LocalTime value, final LocalTime begin, final LocalTime end) {
        return ((value != null) && (begin != null && end != null)) && (!value.isBefore(begin) && !value.isAfter(end));
    }

    public static boolean inRange(final LocalDateTime value, final LocalDateTime begin, final LocalDateTime end) {
        return ((value != null) && (begin != null && end != null)) && (!value.isBefore(begin) && !value.isAfter(end));
    }

    public static String toString(Object v) {
        return ObjectUtil.toString(v);
    }

    public static String toString(String v) {
        return v == null ? "" : v;
    }

    public static String toString(Class<?> v) {
        return v == null ? "" : v.getName();
    }

    public static String toString(UUID v) {
        return v == null ? "" : v.toString();
    }

    public static String toString(Path v) {
        return v == null ? "" : v.toString();
    }

    public static String toString(URI v) {
        return v == null ? "" : v.toString();
    }

    public static String toString(URL v) {
        return v == null ? "" : v.toString();
    }

    public static String toString(File v) {
        return v == null ? "" : v.getAbsolutePath();
    }

    public static String toString(int v) {
        return String.valueOf(v);
    }

    public static String toString(long v) {
        return String.valueOf(v);
    }

    public static String toString(double v) {
        return formatDouble(v);
    }

    public static String toString(boolean v) {
        return v ? "true" : "false";
    }

    public static String toString(LocalDate v) {
        if (v == null)
            return "";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE);
        return formatter.format(v);
    }

    public static String toString(LocalTime v) {
        if(v!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_TIME);
            return formatter.format(v);
        }
        return "";
    }

    public static String toString(LocalDateTime v) {
        if(v!=null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME_MS);
            return formatter.format(v);
        }
        return "";
    }


}