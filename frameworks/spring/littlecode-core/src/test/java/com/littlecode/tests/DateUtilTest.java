package com.littlecode.tests;

import com.littlecode.parsers.DateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ExtendWith(MockitoExtension.class)
class DateUtilTest {
    static final LocalDate TEST_MIN_LOCALDATE = LocalDate.of(1901, 1, 1);
    static final LocalTime TEST_MAX_LOCALTIME = LocalTime.of(23, 59, 59, 999);
    static final LocalTime TEST_MIN_LOCALTIME = LocalTime.of(0, 0, 0, 0);
    static final LocalDateTime TEST_MIN_LOCALDATETIME = LocalDateTime.of(TEST_MIN_LOCALDATE, TEST_MIN_LOCALTIME);
    static final LocalDateTime TEST_MAX_LOCALDATETIME = LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME);

    @Test
    @DisplayName("Deve validar toMax_Time")
    void UI_toMax_LocalTime() {
        var value = LocalTime.now();
        Assertions.assertEquals(DateUtil.toMax(value).toString(), LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMax(value = null).toString(), TEST_MAX_LOCALDATETIME.toString());
    }

    @Test
    @DisplayName("Deve validar toMax_Date")
    void UI_toMax_LocalDate() {

        var value = LocalDate.now();
        Assertions.assertEquals(DateUtil.toMax(value).toString(), LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMax(value = null).toString(), TEST_MAX_LOCALDATETIME.toString());
    }

    @Test
    @DisplayName("Deve validar toMax_DateTime")
    void UI_toMax_LocalDateTime() {

        var value = LocalDateTime.now();
        Assertions.assertEquals(DateUtil.toMax(value).toString(), LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMax(value = null), TEST_MAX_LOCALDATETIME);
    }

    @Test
    @DisplayName("Deve validar toMin_Time")
    void UI_toMin_LocalTime() {
        Assertions.assertEquals(DateUtil.toMin(LocalTime.now()).toString(), TEST_MIN_LOCALDATETIME.toString());
        Assertions.assertEquals(DateUtil.toMin((LocalTime) null).toString(), TEST_MIN_LOCALDATETIME.toString());
    }

    @Test
    @DisplayName("Deve validar toMin_Date")
    void UI_toMin_LocalDate() {

        var value = LocalDate.now();
        Assertions.assertEquals(DateUtil.toMin(value).toString(), LocalDateTime.of(value, TEST_MIN_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMin(value = null).toString(), TEST_MIN_LOCALDATETIME.toString());
    }

    @Test
    @DisplayName("Deve validar toMin_DateTime")
    void UI_toMin_LocalDateTime() {

        var value = LocalDateTime.now();
        Assertions.assertEquals(DateUtil.toMin(value).toString(), LocalDateTime.of(LocalDate.now(), TEST_MIN_LOCALTIME).toString());
        Assertions.assertEquals(DateUtil.toMin(value = null).toString(), TEST_MIN_LOCALDATETIME.toString());
    }

    @Test
    @DisplayName("Deve validar toDuration")
    void UI_toDuration() {

        Assertions.assertEquals(DateUtil.toDuration(null), Duration.ZERO);
        Assertions.assertEquals(DateUtil.toDuration(""), Duration.ZERO);
        Assertions.assertEquals(DateUtil.toDuration(" "), Duration.ZERO);

        Assertions.assertEquals(DateUtil.toDuration("1"), Duration.ofMillis(1));
        Assertions.assertEquals(DateUtil.toDuration("1s"), Duration.ofSeconds(1));
        Assertions.assertEquals(DateUtil.toDuration("1m"), Duration.ofMinutes(1));
        Assertions.assertEquals(DateUtil.toDuration("1h"), Duration.ofHours(1));
        Assertions.assertEquals(DateUtil.toDuration("1d"), Duration.ofDays(1));
        Assertions.assertEquals(DateUtil.toDuration("1?"), Duration.ZERO);
    }
}
