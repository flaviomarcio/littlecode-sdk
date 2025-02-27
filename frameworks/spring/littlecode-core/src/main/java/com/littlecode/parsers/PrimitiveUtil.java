package com.littlecode.parsers;

import com.littlecode.config.CorePublicConsts;
import com.littlecode.config.UtilCoreConfig;
import com.littlecode.files.FileFormat;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class PrimitiveUtil {

    public static String formatMask(int precision) {
        return precision <= 0
                ? "#"
                : "#." + ("#".repeat(precision));
    }

    public static String formatDouble(double v, int precision) {
        var format = formatMask(precision);
        var symbols = new DecimalFormatSymbols(Locale.US);
        return (new DecimalFormat(format, symbols)).format(v);
    }

    public static String formatDouble(double d) {
        return formatDouble(d, CorePublicConsts.DOUBLE_PRECISION);
    }

    public static boolean isPrimitiveValue(Class<?> aClass) {
        if(aClass!=null){
            if(aClass.isPrimitive()) return true;
            return CorePublicConsts.PRIMITIVE_CLASSES.containsValue(aClass);
        }
        return false;
    }

    public static boolean isPrimitiveValue(Object value) {
        return value != null && CorePublicConsts.PRIMITIVE_CLASSES.containsValue(value.getClass());
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

    public static int toInt(Object v) {
        if (v instanceof Integer value)
            return value;
        else if (v instanceof Long value)
            return value.intValue();
        else if (v instanceof Double value)
            return value.intValue();
        else if (v instanceof String value)
            return Integer.parseInt(value);
        else if (v instanceof LocalDate value)
            return Double.valueOf(value.toEpochDay()).intValue();
        else if (v instanceof LocalTime value)
            return Double.valueOf(value.toSecondOfDay()).intValue();
        else if (v instanceof LocalDateTime value)
            return Double.valueOf(value.toEpochSecond(ZoneOffset.UTC)).intValue();
        else
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

    public static int toInt(LocalDate v) {
        return v == null ? 0 : (int) v.toEpochDay();
    }

    public static int toInt(LocalTime v) {
        return v == null ? 0 : v.toSecondOfDay();
    }

    public static int toInt(LocalDateTime v) {
        return v == null ? 0 : (int) v.toEpochSecond(ZoneOffset.UTC);
    }

    public static long toLong(Object v) {
        if (v instanceof Long value)
            return value;
        else if (v instanceof Integer value)
            return value.longValue();
        else if (v instanceof Double value)
            return value.longValue();
        else if (v instanceof String value)
            return Long.parseLong(value);
        else if (v instanceof LocalDate value)
            return Double.valueOf(value.toEpochDay()).longValue();
        else if (v instanceof LocalTime value)
            return Double.valueOf(value.toSecondOfDay()).longValue();
        else if (v instanceof LocalDateTime value)
            return Double.valueOf(value.toEpochSecond(ZoneOffset.UTC)).longValue();
        else
            return 0;
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

    public static long toLong(LocalDate v) {
        return v == null ? 0 : v.toEpochDay();
    }

    public static long toLong(LocalTime v) {
        return v == null ? 0 : v.toSecondOfDay();
    }

    public static long toLong(LocalDateTime v) {
        return v == null ? 0 : v.toEpochSecond(ZoneOffset.UTC);
    }

    public static double toDouble(Object v) {
        if (v instanceof Double value)
            return value;
        else if (v instanceof Integer value)
            return value.doubleValue();
        else if (v instanceof Long value)
            return value.doubleValue();
        else if (v instanceof String value)
            return Double.parseDouble(value);
        else if (v instanceof LocalDate value)
            return Double.valueOf(value.toEpochDay());
        else if (v instanceof LocalTime value)
            return Double.valueOf(value.toSecondOfDay());
        else if (v instanceof LocalDateTime value)
            return Double.valueOf(value.toEpochSecond(ZoneOffset.UTC));
        else
            return 0;
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
        if (v != null && !v.equals(0D)) {
            var decimal = new BigDecimal(v.toString());
            decimal = decimal.setScale(precision, RoundingMode.HALF_UP);
            return decimal.doubleValue();
        }
        return 0;
    }

    public static double toDouble(String v, int precision) {
        var d = toDouble(v);
        return toDouble(d, precision);
    }

    public static double toDouble(LocalDate v) {
        return v == null ? 0 : v.toEpochDay();
    }

    public static double toDouble(LocalTime v) {
        return v == null ? 0 : v.toSecondOfDay();
    }

    public static double toDouble(LocalDateTime v) {
        return v == null ? 0 : v.toEpochSecond(ZoneOffset.UTC);
    }

    public static LocalDate toDate(Object v) {
        if (v != null) {
            if (v instanceof String value)
                return toDate(value);
            else if (v instanceof Integer value)
                return toDate(value.intValue());
            else if (v instanceof Long value)
                return toDate(value.longValue());
            else if (v instanceof Double value)
                return toDate(value.doubleValue());
            else if (v instanceof LocalDate value)
                return value;
            else if (v instanceof LocalDateTime value)
                return value.toLocalDate();
        }
        return null;
    }

    public static LocalDate toDate(String v) {
        if (v != null) {
            var dt = PrimitiveUtil.toDateTime(v);
            return dt == null ? null : dt.toLocalDate();
        }
        return null;
    }

    public static LocalDate toDate(int v) {
        return LocalDate.ofEpochDay(v);
    }

    public static LocalDate toDate(long v) {
        return LocalDate.ofEpochDay(v);
    }

    public static LocalDate toDate(double v) {
        return LocalDate.ofEpochDay((long) v);
    }

    public static LocalTime toTime(Object v) {
        if (v != null) {
            if (v instanceof String value)
                return toTime(value);
            else if (v instanceof Integer value)
                return toTime(value.intValue());
            else if (v instanceof Long value)
                return toTime(value.intValue());
            else if (v instanceof Double value)
                return toTime(value.doubleValue());
            else if (v instanceof LocalTime value)
                return value;
            else if (v instanceof LocalDateTime value)
                return value.toLocalTime();
        }
        return null;
    }

    public static LocalTime toTime(String v) {
        if (v != null) {
            try {
                var value = v.trim();
                String format;
                if (value.length() == CorePublicConsts.FORMAT_TIME_HH_MM.length())
                    format = CorePublicConsts.FORMAT_TIME_HH_MM;
                else if (value.length() == CorePublicConsts.FORMAT_TIME.length())
                    format = CorePublicConsts.FORMAT_TIME;
                else if (value.length() == CorePublicConsts.FORMAT_TIME_MS.length())
                    format = CorePublicConsts.FORMAT_TIME_MS;
                else
                    format = CorePublicConsts.FORMAT_TIME;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalTime.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static LocalTime toTime(int v) {
        return LocalTime.ofSecondOfDay(v);
    }

    public static LocalTime toTime(long v) {
        return LocalTime.ofSecondOfDay(v);
    }

    public static LocalTime toTime(double v) {
        return LocalTime.ofSecondOfDay((long) v);
    }

    public static LocalDateTime toDateTime(Object v) {
        if (v != null) {
            if (v instanceof String value)
                return toDateTime(value);
            else if (v instanceof Integer value)
                return toDateTime(value.intValue());
            else if (v instanceof Long value)
                return toDateTime(value.longValue());
            else if (v instanceof Double value)
                return toDateTime(value.doubleValue());
            else if (v instanceof LocalDate value)
                return LocalDateTime.of(value, CorePublicConsts.MIN_LOCALTIME);
            else if (v instanceof LocalTime value)
                return LocalDateTime.of(CorePublicConsts.MIN_LOCALDATE, value);
            else if (v instanceof LocalDateTime value)
                return value;
        }
        return null;
    }

    public static LocalDateTime toDateTime(String v) {
        if (v != null && !v.trim().isEmpty()) {
            var value = v.trim();
            try {
                String format;
                if (value.length() == CorePublicConsts.FORMAT_DATE_TIME.length() - 2) {
                    format = CorePublicConsts.FORMAT_DATE_TIME;
                } else if (value.length() == CorePublicConsts.FORMAT_DATE_TIME_HH.length() - 2) {
                    format = CorePublicConsts.FORMAT_DATE_TIME;
                    value = value + ":00:00";
                } else if (value.length() == CorePublicConsts.FORMAT_DATE_TIME_HH_MM.length() - 2) {
                    format = CorePublicConsts.FORMAT_DATE_TIME;
                    value = value + ":00";
                } else if (value.length() == CorePublicConsts.FORMAT_DATE.length()) {
                    format = CorePublicConsts.FORMAT_DATE_TIME;
                    value = value + "T00:00:00";
                } else if (value.length() == CorePublicConsts.FORMAT_DATE_TIME_MS.length() - 2) {
                    format = CorePublicConsts.FORMAT_DATE_TIME_MS;
                } else {
                    format = CorePublicConsts.FORMAT_DATE_TIME_MS;
                    var valueS = value.split("\\.");
                    var valueA = valueS.length > 0 ? valueS[0] : "";
                    var valueB = new StringBuilder(valueS.length > 1 ? valueS[1] : "");
                    while (valueB.length() < 9)// .SSSSSSSSS
                        valueB.insert(0, "0");
                    value = valueA + "." + valueB;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
                return LocalDateTime.parse(value, formatter);

            } catch (Exception ignored) {
            }

            return toDateTime(toDouble(value));
        }
        return null;
    }

    public static LocalDateTime toDateTime(long v) {
        return v == 0 ? null : LocalDateTime.ofEpochSecond(v, 0, ZoneOffset.UTC);
    }

    public static LocalDateTime toDateTime(double v) {
        Instant instant = Instant.ofEpochSecond((long) v);
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
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
        return (v == null || v.equals(CorePublicConsts.MIN_LOCALDATE));
    }

    public static boolean isEmpty(LocalTime v) {
        return (v == null || v.equals(CorePublicConsts.MIN_LOCALTIME));
    }

    public static boolean isEmpty(LocalDateTime v) {
        return (v == null || v.equals(CorePublicConsts.MIN_LOCALDATETIME));
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

    public static Optional<String> getString(Object v) {
        if (v == null)
            return Optional.empty();
        var out = toString(v).trim();
        if (out.isEmpty())
            return Optional.empty();
        return Optional.of(out);
    }

    public static String toString(Object v) {
        if (v != null) {
            if (v instanceof String value)
                return value;
            else if (v instanceof Character value)
                return value.toString();
            else if (v instanceof Integer value)
                return value.toString();
            else if (v instanceof Long value)
                return value.toString();
            else if (v instanceof Double value)
                return value.toString();
            else if (v instanceof LocalDate value)
                return value.toString();
            else if (v instanceof LocalTime value)
                return value.toString();
            else if (v instanceof LocalDateTime value)
                return value.toString();
            else if (v instanceof UUID value)
                return value.toString();
            else if (v instanceof URI value)
                return value.toString();
            else if (v instanceof Boolean value)
                return value.toString();
            else if (v instanceof File value)
                return value.getAbsolutePath();
            else
                return ObjectUtil.toString(v);
        }
        return "";
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CorePublicConsts.FORMAT_DATE);
        return formatter.format(v);
    }

    public static String toString(LocalTime v) {
        if (v != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CorePublicConsts.FORMAT_TIME);
            return formatter.format(v);
        }
        return "";
    }

    public static String toString(LocalDateTime v) {
        if (v != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(CorePublicConsts.FORMAT_DATE_TIME_MS);
            return formatter.format(v);
        }
        return "";
    }

    public static UUID toUUID(String v) {
        return (v == null)
                ? null
                : HashUtil.toUuid(v);
    }

    public static URI toURI(String v) {
        return (v == null)
                ? null
                : URI.create(v);
    }

    public static Path toPath(String v) {
        return (v == null)
                ? null
                : Path.of(v);
    }

    public static File toFile(String v) {
        return (v == null)
                ? null
                : new File(v);
    }

    public static Map<String, Object> toMap(String v) {
        var mapper = UtilCoreConfig.newObjectMapper(FileFormat.JSON);
        Object o = null;
        try {
            o = mapper.readValue(v, Object.class);
        } catch (Exception ignored) {
        }
        Map<String, Object> __return = new HashMap<>();
        if (o instanceof Map<?, ?> map)
            map.forEach((key, value) -> __return.put(toString(key), value));
        return __return;
    }

    public static List<Object> toList(String v) {
        if (v == null || v.isEmpty())
            return new ArrayList<>();
        var mapper = UtilCoreConfig.newObjectMapper(FileFormat.JSON);
        Object o = null;
        try {
            o = mapper.readValue(v, Object.class);
        } catch (Exception ignored) {
        }
        List<Object> __return = new ArrayList<>();
        if (o instanceof List<?> list) {
            for (var i : list)
                __return.add(i);
        }
        return __return;
    }

    public static List<String> toStringList(String v) {
        if (v != null && !v.isEmpty()){
            var mapper = UtilCoreConfig.newObjectMapper(FileFormat.JSON);
            Object o = null;
            try {
                o = mapper.readValue(v, Object.class);
            } catch (Exception ignored) {
            }
            List<String> __return = new ArrayList<>();
            if (o instanceof List<?> list) {
                for (var i : list) {
                    var s = toString(i);
                    if (!PrimitiveUtil.isEmpty(s))
                        __return.add(s);
                }
            } else {
                __return.add(v);
            }
            return __return;
        }
        return new ArrayList<>();

    }

}