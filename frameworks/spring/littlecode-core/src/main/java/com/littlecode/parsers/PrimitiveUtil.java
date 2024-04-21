package com.littlecode.parsers;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class PrimitiveUtil {
    private static final int DECIMAL_DEFAULT_PRECISION = 6;
    private static final LocalDate MIN_LOCALDATE = LocalDate.of(1901, 1, 1);
    private static final LocalTime MIN_LOCALTIME = LocalTime.of(0, 0, 0, 0);
    private static final LocalDateTime MIN_LOCALDATETIME = LocalDateTime.of(MIN_LOCALDATE, MIN_LOCALTIME);

    private static final String FORMAT_DATE = "yyyy-MM-dd";
    private static final String FORMAT_DATE_SHORT = "yy-MM-dd";
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";
    private static final String FORMAT_DATETIME_MS = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS";
    private static final String FORMAT_DATETIME_SHORT = "yyyy-MM-dd'T'HH:mm";
    private static final String FORMAT_DATETIME_SHORT_H = "yyyy-MM-dd'T'HH";
    private static final String FORMAT_TIME = "HH:mm:ss";
    private static final String FORMAT_TIME_MS = "HH:mm:ss.SSSSSSSSS";
    private static final String FORMAT_TIME_SHORT = "HH:mm";
    private static final int DOUBLE_PRECISION = 6;
    private static final List<String> BOOL_VALID_TRUE = List.of("true", "t", "t", "s", "1");

    private static final DateTimeFormatter FORMATTER_DATE = DateTimeFormatter.ofPattern(FORMAT_DATE);
    private static final DateTimeFormatter FORMATTER_DATE_SHORT = DateTimeFormatter.ofPattern(FORMAT_DATE_SHORT);
    private static final DateTimeFormatter FORMATTER_TIME = DateTimeFormatter.ofPattern(FORMAT_TIME);
    private static final DateTimeFormatter FORMATTER_TIME_MS = DateTimeFormatter.ofPattern(FORMAT_TIME_MS);
    private static final DateTimeFormatter FORMATTER_DATETIME_MS = DateTimeFormatter.ofPattern(FORMAT_DATETIME_MS);
    private static final DateTimeFormatter FORMATTER_DATETIME = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);
    private static final DateTimeFormatter FORMATTER_DATETIME_SHORT = DateTimeFormatter.ofPattern(FORMAT_DATETIME_SHORT);
    private static final DateTimeFormatter FORMATTER_DATETIME_SHORT_H = DateTimeFormatter.ofPattern(FORMAT_DATETIME_SHORT_H);

    public static String formatDouble(double v, int precision) {
        var format = precision <= 0
                ? "#"
                : "#." + ("#".repeat(precision));
        var symbols = new DecimalFormatSymbols(Locale.US);
        return (new DecimalFormat(format, symbols)).format(v);
    }

    public static String formatDouble(double d) {
        return PrimitiveUtil.formatDouble(d, DOUBLE_PRECISION);
    }

    public static UUID toUUID(String v) {
        return v != null
                ? HashUtil.toUuid(v)
                : null;
    }

    public static Path toPath(String v) {
        return (v == null || v.trim().isEmpty()) ? null : Path.of(v);
    }

    public static URI toURI(String v) {
        return (v == null || v.trim().isEmpty()) ? null : URI.create(v);
    }

    public static BigDecimal toBigDecimal(String v, int precision) {
        try {
            return new BigDecimal(v).setScale(precision, RoundingMode.HALF_UP);
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    public static BigDecimal toBigDecimal(String v) {
        try {
            return new BigDecimal(v);
        } catch (Exception e) {
            return new BigDecimal(0);
        }
    }

    public static BigDecimal toBigDecimal(double v, int precision) {
        return new BigDecimal(v).setScale(precision, RoundingMode.HALF_UP);
    }

    public static BigDecimal toBigDecimal(double v) {
        return new BigDecimal(v);
    }

    public static BigDecimal toBigDecimal(int v) {
        return new BigDecimal(v).setScale(0, RoundingMode.HALF_UP);
    }

    public static BigDecimal toBigDecimal(long v) {
        return new BigDecimal(v).setScale(0, RoundingMode.HALF_UP);
    }

    public static BigDecimal toBigDecimal(boolean v) {
        return new BigDecimal(v ? 1 : 0);
    }

    public static long toLong(String v) {
        return toBigDecimal(v, 0).longValue();
    }

    public static long toLong(double v) {
        return toBigDecimal(v, 0).longValue();
    }

    public static long toLong(boolean v) {
        return v ? 1 : 0;
    }

    public static int toInt(String v) {
        return toBigDecimal(v, 0).intValue();
    }

    public static int toInt(boolean v) {
        return v ? 1 : 0;
    }

    public static int toInt(double v) {
        return toBigDecimal(v, 0).intValue();
    }

    public static double toDouble(String v) {
        return toBigDecimal(v).doubleValue();
    }

    public static double toDouble(int v) {
        return v;
    }

    public static double toDouble(long v) {
        return v;
    }

    public static double toDouble(BigDecimal v) {
        return (v == null) ? 0 : v.doubleValue();
    }

    public static double toDouble(String v, int precision) {
        return toBigDecimal(v, precision).doubleValue();
    }

    public static double toDouble(double v, int precision) {
        return toBigDecimal(v, precision).doubleValue();
    }

    public static double toDouble(boolean v) {
        return v ? 1 : 0;
    }

    public static LocalDate toDate(LocalDateTime v) {
        return v != null ? v.toLocalDate() : null;
    }

    public static LocalDate toDate(long v) {
        return LocalDate.ofEpochDay(v);
    }

    public static LocalDate toDate(String v) {
        if (v != null && !v.trim().isEmpty()) {
            try {
                var value = v.trim();
                var formatter = switch (value.length()) {
                    case 10 -> FORMATTER_DATE;//2021-01-01
                    case 8 -> FORMATTER_DATE_SHORT;//21-01-01
                    default -> null;
                };
                if (formatter != null)
                    return LocalDate.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static LocalTime toTime(LocalDateTime v) {
        return v != null ? v.toLocalTime() : null;
    }

    public static LocalTime toTime(long v) {
        return LocalTime.ofSecondOfDay(v);
    }

    public static LocalTime toTime(String v) {
        if (v != null && !v.trim().isEmpty()) {
            try {
                var value = v.trim();
                var formatter = switch (value.length()) {
                    case 18, 17, 16, 15, 14, 13, 12 -> FORMATTER_TIME_MS;//00:00:00.000000000
                    case 8 -> FORMATTER_TIME;//00:00:00
                    case 5 -> FORMATTER_DATETIME_SHORT;//00:00
                    default -> null;
                };
                if (formatter != null)
                    return LocalTime.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static LocalDateTime toDateTime(long v) {
        return LocalDateTime.ofEpochSecond(v, 0, ZoneOffset.UTC);
    }

    public static LocalDateTime toDateTime(String v) {
        if (v == null || v.trim().isEmpty()) {
            return null;
        }
        var value = v.trim();
        var formatter = switch (value.length()) {
            case 29, 28, 27, 26, 25, 24, 23, 22, 21, 20 -> FORMATTER_DATETIME_MS;//1901-01-01T00:00:00.999999999
            case 19 -> FORMATTER_DATETIME;//1901-01-01T00:00:00
            case 16 -> FORMATTER_DATETIME_SHORT;//1901-01-01T00:00
            case 13 -> FORMATTER_DATETIME_SHORT_H;//1901-01-01T00
            default -> null;
        };
        if (formatter != null)
            return LocalDateTime.parse(value, formatter);
        return null;
    }

    public static LocalDateTime toDateTime(LocalDate v) {
        return v != null
                ? LocalDateTime.of(v, MIN_LOCALTIME)
                : null;
    }

    public static LocalDateTime toDateTime(LocalTime v) {
        return v != null
                ? LocalDateTime.of(MIN_LOCALDATE, v)
                : null;
    }

    public static boolean toBool(String v) {
        return (v != null) && (BOOL_VALID_TRUE.contains(v.trim().toLowerCase()));
    }

    public static boolean toBool(long v) {
        return v == 1;
    }

    public static boolean toBool(int v) {
        return v == 1;
    }

    public static boolean toBool(double v) {
        return toBigDecimal(v, 0).intValue() == 1;
    }

    public static String toString(String v) {
        return v == null ? "" : v;
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

    public static String toString(UUID v) {
        return v == null ? "" : v.toString();
    }

    public static String toString(URI v) {
        return v == null ? "" : v.toString();
    }

    public static String toString(File v) {
        return v == null ? "" : v.getAbsolutePath();
    }

    public static String toString(Path v) {
        return v == null ? "" : v.toString();
    }

    public static String toString(boolean v) {
        return String.valueOf(v);
    }

    public static String toString(LocalDate v) {
        return (v == null) ? "" : FORMATTER_DATE.format(v);
    }

    public static String toString(LocalTime v) {
        return (v == null) ? "" : FORMATTER_TIME.format(v);
    }

    public static String toString(LocalDateTime v) {
        return (v != null)
                ? FORMATTER_DATETIME_MS.format(v)
                : "";
    }
    public static boolean isEmpty(String v) {
        return (v == null || v.trim().isEmpty());
    }

    public static boolean isEmpty(UUID v) {
        return (v == null);
    }

    public static boolean isEmpty(Path v) {
        return (v == null || v.toString().isEmpty());
    }

    public static boolean isEmpty(URI v) {
        return (v == null || v.toString().isEmpty());
    }

    public static boolean isEmpty(List<?> v) {
        return (v == null || v.isEmpty());
    }

    public static boolean isEmpty(int v) {
        return (v == 0);
    }

    public static boolean isEmpty(long v) {
        return (v == 0);
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