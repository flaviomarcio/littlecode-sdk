package com.littlecode.parsers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class DateUtil {
    public static final LocalDate MIN_LOCALDATE = LocalDate.of(1901, 1, 1);
    public static final LocalTime MAX_LOCALTIME = LocalTime.of(23, 59, 59, 999);
    public static final LocalTime MIN_LOCALTIME = LocalTime.of(0, 0, 0, 0);
    public static final LocalDateTime MIN_LOCALDATETIME = LocalDateTime.of(MIN_LOCALDATE, MIN_LOCALTIME);

    public static LocalDateTime toMax(LocalDate v) {
        return toMax(LocalDateTime.of(v == null ? LocalDate.now() : v, LocalTime.now()));
    }

    public static LocalDateTime toMax(LocalTime v) {
        return toMax(LocalDateTime.of(LocalDate.now(), v == null ? MAX_LOCALTIME : v));
    }

    public static LocalDateTime toMax(LocalDateTime v) {
        return v == null
                ? LocalDateTime.of(LocalDate.now(), MAX_LOCALTIME)
                : LocalDateTime.of(v.toLocalDate(), MAX_LOCALTIME);
    }

    public static LocalDateTime toMin(LocalDate v) {
        return toMin(LocalDateTime.of(v == null ? MIN_LOCALDATE : v, LocalTime.now()));
    }

    public static LocalDateTime toMin(LocalTime v) {
        return toMin(LocalDateTime.of(MIN_LOCALDATE, v == null ? MIN_LOCALTIME : v));
    }

    public static LocalDateTime toMin(LocalDateTime v) {
        return v == null
                ? MIN_LOCALDATETIME
                : LocalDateTime.of(v.toLocalDate(), MIN_LOCALTIME);
    }

}
