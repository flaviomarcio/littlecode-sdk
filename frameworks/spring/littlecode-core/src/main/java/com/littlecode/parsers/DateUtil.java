package com.littlecode.parsers;

import com.littlecode.config.CorePublicConsts;

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

}
