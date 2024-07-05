package com.littlecode.parsers;

import com.littlecode.config.CorePublicConsts;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtil {

    public static LocalDateTime toMax(LocalDate v) {
        return toMax(LocalDateTime.of(v == null ? LocalDate.now() : v, LocalTime.now()));
    }

    public static LocalDateTime toMax(LocalTime v) {
        return toMax(LocalDateTime.of(LocalDate.now(), v == null ? CorePublicConsts.MAX_LOCALTIME : v));
    }

    public static LocalDateTime toMax(LocalDateTime v) {
        return v == null
                ? LocalDateTime.of(LocalDate.now(), CorePublicConsts.MAX_LOCALTIME)
                : LocalDateTime.of(v.toLocalDate(), CorePublicConsts.MAX_LOCALTIME);
    }

    public static LocalDateTime toMin(LocalDate v) {
        return toMin(LocalDateTime.of(v == null ? CorePublicConsts.MIN_LOCALDATE : v, LocalTime.now()));
    }

    public static LocalDateTime toMin(LocalTime v) {
        return toMin(LocalDateTime.of(CorePublicConsts.MIN_LOCALDATE, v == null ? CorePublicConsts.MIN_LOCALTIME : v));
    }

    public static LocalDateTime toMin(LocalDateTime v) {
        return v == null
                ? CorePublicConsts.MIN_LOCALDATETIME
                : LocalDateTime.of(v.toLocalDate(), CorePublicConsts.MIN_LOCALTIME);
    }

    public static Duration toDuration(String input) {
        if (input == null)
            return Duration.ZERO;
        input = input.trim();

        if (input.isEmpty())
            return Duration.ZERO;

        var num = PrimitiveUtil.toLong(input);
        if (num > 0)
            return Duration.ofMillis(num);

        String valuePart = input.substring(0, input.length() - 1);
        String unitPart = input.substring(input.length() - 1).toLowerCase();

        var value = PrimitiveUtil.toLong(valuePart);

        long durationMillis = switch (unitPart) {
            case "s" -> value * 1000;
            case "m" -> value * 60 * 1000;
            case "h" -> value * 60 * 60 * 1000;
            case "d" -> value * 24 * 60 * 60 * 1000;
            default -> 0;
        };
        return durationMillis == 0
                ? Duration.ZERO
                : Duration.ofMillis(durationMillis);
    }

}
