package com.littlecode.parsers;

import com.littlecode.config.CorePublicConsts;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormatUtil {

    @Getter
    public static Locale LOCALE = null;
    public static DecimalFormat DECIMAL_FORMAT;
    public static NumberFormat INT_FORMAT;
    public static NumberFormat CURRENCY_FORMAT;
    public static DateTimeFormatter DATETIME_FORMAT;
    public static DateTimeFormatter DATE_FORMAT;
    public static DateTimeFormatter TIME_FORMAT;


    static {
        setLOCALE(null);
    }

    public static void setLOCALE(Locale locale) {
        LOCALE = (locale == null)
                ? new Locale("pt", "BR")
                : locale;

        if (LOCALE.getCountry().equals("BR")) {
            DECIMAL_FORMAT = new DecimalFormat("#,##0.000", new DecimalFormatSymbols(LOCALE));
            INT_FORMAT = NumberFormat.getNumberInstance(LOCALE);
            CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(LOCALE);
            DATETIME_FORMAT = DateTimeFormatter.ofPattern("dd/MM/YYYY HH:mm:ss");
            DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/YYYY");
            TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");
        } else {
            DECIMAL_FORMAT = new DecimalFormat("0.000", new DecimalFormatSymbols(LOCALE));
            INT_FORMAT = NumberFormat.getNumberInstance(LOCALE);
            CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(LOCALE);
            DATETIME_FORMAT = DateTimeFormatter.ofPattern(CorePublicConsts.FORMAT_DATE_TIME);
            DATE_FORMAT = DateTimeFormatter.ofPattern(CorePublicConsts.FORMAT_DATE);
            TIME_FORMAT = DateTimeFormatter.ofPattern(CorePublicConsts.FORMAT_TIME);
        }

    }

    public static String formatDateTime(LocalDateTime value) {
        return value == null ? "" : DATETIME_FORMAT.format(value);
    }

    public static String formatDate(LocalDate value) {
        return value == null ? "" : DATE_FORMAT.format(value);
    }

    public static String formatDate(LocalDateTime value) {
        return value == null ? "" : DATE_FORMAT.format(value.toLocalDate());
    }

    public static String formatTime(LocalTime value) {
        return value == null ? "" : TIME_FORMAT.format(value);
    }

    public static String formatTime(LocalDateTime value) {
        return value == null ? "" : TIME_FORMAT.format(value.toLocalTime());
    }

    public static String formatValue(Object value) {
        if (value == null) return "";
        if (value instanceof String str)
            return str;
        else if (value instanceof LocalDateTime dt)
            return FormatUtil.formatDateTime(dt);
        else if (value instanceof LocalDate date)
            return FormatUtil.formatDate(date);
        else if (value instanceof LocalTime time)
            return FormatUtil.formatTime(time);
        else if (value instanceof Double aDouble)
            return FormatUtil.formatDouble(aDouble);
        else if (value instanceof BigDecimal bigDecimal)
            return FormatUtil.formatDouble(bigDecimal.doubleValue());
        else if (value instanceof Integer integer)
            return FormatUtil.formatInt(integer);
        else if (value instanceof Long aLong)
            return FormatUtil.formatLong(aLong);
        else if (value instanceof Boolean aBoolean)
            return FormatUtil.formatBool(aBoolean);
        else
            return value.toString();
    }


    public static String formatInt(int value) {
        return INT_FORMAT.format(value);
    }

    public static String formatLong(long value) {
        return INT_FORMAT.format(value);
    }

    public static String formatDouble(double value) {
        return DECIMAL_FORMAT.format(value);
    }

    public static String formatCurrency(double value) {
        return CURRENCY_FORMAT.format(value);
    }

    public static String formatBool(boolean value) {
        return value ? "Sim" : "Não";
    }
}
