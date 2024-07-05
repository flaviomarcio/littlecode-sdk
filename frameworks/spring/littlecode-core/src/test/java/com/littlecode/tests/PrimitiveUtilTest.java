package com.littlecode.tests;

import com.littlecode.config.CorePublicConsts;
import com.littlecode.parsers.PrimitiveUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PrimitiveUtilTest {
    public static final LocalDate TEST_MIN_LOCALDATE = LocalDate.of(1901, 1, 1);
    //    public static final LocalTime TEST_MAX_LOCALTIME = LocalTime.of(23, 59, 59, 999);
    public static final LocalTime TEST_MIN_LOCALTIME = LocalTime.of(0, 0, 0, 0);
    public static final LocalDateTime TEST_MIN_LOCALDATETIME = LocalDateTime.of(TEST_MIN_LOCALDATE, TEST_MIN_LOCALTIME);
//    public static final LocalDateTime TEST_MAX_LOCALDATETIME = LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME);

    @Test
    @DisplayName("Deve validar format")
    public void UI_CHECK_CHECK_FORMAT() {

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.formatMask(0));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.formatMask(1));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.formatMask(-11));

        Assertions.assertEquals(PrimitiveUtil.formatMask(-1), "#");
        Assertions.assertEquals(PrimitiveUtil.formatMask(0), "#");
        Assertions.assertEquals(PrimitiveUtil.formatMask(1), "#.#");
        Assertions.assertEquals(PrimitiveUtil.formatMask(2), "#.##");

        Assertions.assertEquals(PrimitiveUtil.formatDouble(0.123, 0), "0");
        Assertions.assertEquals(PrimitiveUtil.formatDouble(0.123, 1), "0.1");
        Assertions.assertEquals(PrimitiveUtil.formatDouble(0.123, 2), "0.12");
        Assertions.assertEquals(PrimitiveUtil.formatDouble(0.123, 3), "0.123");

    }

    @Test
    @DisplayName("Deve validar isPrimitive")
    public void UI_CHECK_CHECK_isPrimitive() {
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.isPrimitiveValue(null));
        Assertions.assertFalse(PrimitiveUtil.isPrimitiveValue(null));
        Assertions.assertFalse(PrimitiveUtil.isPrimitiveValue(new Object()));

        Assertions.assertFalse(PrimitiveUtil.isPrimitiveValue(new Object()));

        var primitives = List.of(
                String.class.getName(),
                Byte.class.getName(),
                UUID.class.getName(),
                Boolean.class.getName(),
                Integer.class.getName(),
                Long.class.getName(),
                Double.class.getName(),
                BigDecimal.class.getName(),
                BigInteger.class.getName(),
                LocalDate.class.getName(),
                LocalTime.class.getName(),
                LocalDateTime.class.getName()
        );
        Assertions.assertFalse(PrimitiveUtil.isPrimitiveValue(null));
        for (String c : primitives) {
            Assertions.assertTrue(PrimitiveUtil.isPrimitiveValue(c));
        }

    }

    @Test
    @DisplayName("Deve validar toString")
    public void UI_CHECK_toString() {
        List.of("0", 12D, 1L, 1, LocalDate.now(), LocalTime.now(), LocalDateTime.now(), UUID.randomUUID())
                .forEach(value -> {
                    Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(value));
                });

        var dt = LocalDate.of(2000, 1, 3);
        var tm = LocalTime.of(22, 37, 12);
        Assertions.assertDoesNotThrow(() -> new PrimitiveUtil());

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) LocalDate.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) LocalTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(new Object()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) true));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) false));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) 10L));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) 10.1D));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) 10));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) ""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) new File("/tmp/test")));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) UUID.randomUUID()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) URI.create("http://localhost:8080")));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) new URL("http://localhost:8080")));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) Path.of("http://localhost:8080")));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) Object.class));

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(LocalDate.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(LocalTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(new Object()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(true));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(false));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(10L));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(10.1D));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(10));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((String) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(new File("/tmp/test")));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(URI.create("http://localhost:8080")));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(new URL("http://localhost:8080")));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(Path.of("http://localhost:8080")));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(Object.class));

        Assertions.assertEquals(PrimitiveUtil.toString(LocalDateTime.of(dt, tm)), "2000-01-03T22:37:12.000000000");
        Assertions.assertEquals(PrimitiveUtil.toString(dt), "2000-01-03");
        Assertions.assertEquals(PrimitiveUtil.toString(tm), "22:37:12");
        Assertions.assertEquals(PrimitiveUtil.toString(10.125D), "10.125");
        Assertions.assertEquals(PrimitiveUtil.toString(10.125D), "10.125");
        Assertions.assertEquals(PrimitiveUtil.toString(10.12D), "10.12");
        Assertions.assertEquals(PrimitiveUtil.toString(10.125), "10.125");
        Assertions.assertEquals(PrimitiveUtil.toString(10.125), "10.125");
        Assertions.assertEquals(PrimitiveUtil.toString(10.12), "10.12");
        Assertions.assertEquals(PrimitiveUtil.toString(1), "1");
        Assertions.assertEquals(PrimitiveUtil.toString(1000), "1000");
        Assertions.assertEquals(PrimitiveUtil.toString(1L), "1");
        Assertions.assertEquals(PrimitiveUtil.toString(1000L), "1000");
        Assertions.assertEquals(PrimitiveUtil.toString(true), "true");
        Assertions.assertEquals(PrimitiveUtil.toString(false), "false");
        Assertions.assertEquals(PrimitiveUtil.toString((String) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((LocalTime) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((LocalDateTime) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((LocalDate) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((File) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((UUID) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((Path) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((URI) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((URL) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((Class<?>) null), "");

    }

    @Test
    @DisplayName("Deve validar toLong")
    public void UI_CHECK_toLong() {

        List.of("0", 12D, 1L, 1, LocalDate.now(), LocalTime.now(), LocalDateTime.now(), UUID.randomUUID())
                .forEach(value -> {
                    Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(value));
                });

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong((String) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong("100"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong("100.1"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(true));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(false));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(1.5));

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong((LocalDate) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong((LocalTime) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong((LocalDateTime) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(CorePublicConsts.MIN_LOCALDATE));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(CorePublicConsts.MIN_LOCALTIME));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(CorePublicConsts.MIN_LOCALDATETIME));

        Assertions.assertEquals(PrimitiveUtil.toLong(""), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong((String) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong("100"), 100);
        Assertions.assertEquals(PrimitiveUtil.toLong("100.1"), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong(true), 1);
        Assertions.assertEquals(PrimitiveUtil.toLong(false), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong(1.5), 1);

        Assertions.assertEquals(PrimitiveUtil.toLong((LocalDate) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong((LocalTime) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong((LocalDateTime) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong(CorePublicConsts.MIN_LOCALDATE), CorePublicConsts.MIN_LOCALDATE.toEpochDay());
        Assertions.assertEquals(PrimitiveUtil.toLong(CorePublicConsts.MIN_LOCALTIME), CorePublicConsts.MIN_LOCALTIME.toSecondOfDay());
        Assertions.assertEquals(PrimitiveUtil.toLong(CorePublicConsts.MIN_LOCALDATETIME), CorePublicConsts.MIN_LOCALDATETIME.toEpochSecond(ZoneOffset.UTC));
    }

    @Test
    @DisplayName("Deve validar toInt")
    public void UI_CHECK_toInt() {

        List.of("0", 12D, 1L, 1, LocalDate.now(), LocalTime.now(), LocalDateTime.now(), UUID.randomUUID())
                .forEach(value -> {
                    Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt(value));
                });

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt((String) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt("100"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt("100.1"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt("9999999999999999999999999"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt(100));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt(100.1));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt(true));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt(false));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt((LocalDate) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt((LocalTime) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt((LocalDateTime) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt(CorePublicConsts.MIN_LOCALDATE));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt(CorePublicConsts.MIN_LOCALTIME));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toInt(CorePublicConsts.MIN_LOCALDATETIME));

        Assertions.assertEquals(PrimitiveUtil.toInt(""), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt("100"), 100);
        Assertions.assertEquals(PrimitiveUtil.toInt("100.1"), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt("9999999999999999999999999"), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt(100), 100);
        Assertions.assertEquals(PrimitiveUtil.toInt(100.1), 100);
        Assertions.assertEquals(PrimitiveUtil.toInt(true), 1);
        Assertions.assertEquals(PrimitiveUtil.toInt(false), 0);

        Assertions.assertEquals(PrimitiveUtil.toInt((LocalDate) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt((LocalTime) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt((LocalDateTime) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt(CorePublicConsts.MIN_LOCALDATE), (int) CorePublicConsts.MIN_LOCALDATE.toEpochDay());
        Assertions.assertEquals(PrimitiveUtil.toInt(CorePublicConsts.MIN_LOCALTIME), CorePublicConsts.MIN_LOCALTIME.toSecondOfDay());
        Assertions.assertEquals(PrimitiveUtil.toInt(CorePublicConsts.MIN_LOCALDATETIME), (int) CorePublicConsts.MIN_LOCALDATETIME.toEpochSecond(ZoneOffset.UTC));
    }

    @Test
    @DisplayName("Deve validar toDouble")
    public void UI_CHECK_toDouble() {

        List.of("0", 12D, 1L, 1, LocalDate.now(), LocalTime.now(), LocalDateTime.now(), UUID.randomUUID())
                .forEach(value -> {
                    Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble(value));
                });

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble((String) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble(100.11001D, 2));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble(0D, 0));

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble((LocalDate) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble((LocalTime) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble((LocalDateTime) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble(CorePublicConsts.MIN_LOCALDATE));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble(CorePublicConsts.MIN_LOCALTIME));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDouble(CorePublicConsts.MIN_LOCALDATETIME));


        Assertions.assertEquals(PrimitiveUtil.toDouble(""), 0);
        Assertions.assertEquals(PrimitiveUtil.toDouble("0"), 0);
        Assertions.assertEquals(PrimitiveUtil.toDouble("-100"), -100);
        Assertions.assertEquals(PrimitiveUtil.toDouble("100"), 100);
        Assertions.assertEquals(PrimitiveUtil.toDouble("100.00"), 100);
        Assertions.assertEquals(PrimitiveUtil.toDouble("0.01"), 0.01);
        Assertions.assertEquals(PrimitiveUtil.toDouble("100.1"), 100.1);
        Assertions.assertEquals(PrimitiveUtil.toDouble("9999999999999999999999999"), 9999999999999999999999999D);
        Assertions.assertEquals(PrimitiveUtil.toDouble("100.11001", 0), 100);
        Assertions.assertEquals(PrimitiveUtil.toDouble("100.11001", 1), 100.1);
        Assertions.assertEquals(PrimitiveUtil.toDouble("100.12001", 2), 100.120);
        Assertions.assertEquals(PrimitiveUtil.toDouble("100.12301", 3), 100.123);
        Assertions.assertEquals(PrimitiveUtil.toDouble("100.12341", 4), 100.1234);
        Assertions.assertEquals(PrimitiveUtil.toDouble("100.12341", 5), 100.12341);
        Assertions.assertEquals(PrimitiveUtil.toDouble("240.225", 5), 240.225);
        Assertions.assertEquals(PrimitiveUtil.toDouble("240.225", 3), 240.225);
        Assertions.assertEquals(PrimitiveUtil.toDouble("240.225", 2), 240.23);
        Assertions.assertEquals(PrimitiveUtil.toDouble("240.23", 2), 240.23);


        Assertions.assertEquals(PrimitiveUtil.toDouble(100.11001D, 0), 100);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.11001D, 1), 100.1);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.12001D, 2), 100.120);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.12301D, 3), 100.123);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.12341D, 4), 100.1234);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.12341D, 5), 100.12341);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240.225, 5), 240.225);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240.225, 3), 240.225);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240.225, 2), 240.23);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240.23, 2), 240.23);
        Assertions.assertEquals(PrimitiveUtil.toDouble(true), 1);
        Assertions.assertEquals(PrimitiveUtil.toDouble(false), 0);

        Assertions.assertEquals(PrimitiveUtil.toDouble((LocalDate) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toDouble((LocalTime) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toDouble((LocalDateTime) null), 0);
        Assertions.assertEquals(PrimitiveUtil.toDouble(CorePublicConsts.MIN_LOCALDATE), (double) CorePublicConsts.MIN_LOCALDATE.toEpochDay());
        Assertions.assertEquals(PrimitiveUtil.toDouble(CorePublicConsts.MIN_LOCALTIME), (double) CorePublicConsts.MIN_LOCALTIME.toSecondOfDay());
        Assertions.assertEquals(PrimitiveUtil.toDouble(CorePublicConsts.MIN_LOCALDATETIME), (double) CorePublicConsts.MIN_LOCALDATETIME.toEpochSecond(ZoneOffset.UTC));
    }

    @Test
    @DisplayName("Deve validar toDate")
    public void UI_CHECK_toDate() {
        var dt19010101 = LocalDate.of(1901, 1, 1);

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate((Object) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate("(Object)"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate((Object) dt19010101.toString()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate(LocalDate.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate(LocalTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate((Object) "1901-01-01"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate((Object) 0));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate((Object) 0L));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate((Object) 0D));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate((Object) 0.01));

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate((String) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate(dt19010101.toString()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate("1901-01-01"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate(0));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate(0L));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDate(0D));

        Assertions.assertNull(PrimitiveUtil.toDate(""));
        Assertions.assertEquals(PrimitiveUtil.toDate(dt19010101.toString()), dt19010101);
        Assertions.assertEquals(PrimitiveUtil.toDate("1901-01-01"), dt19010101);
        Assertions.assertEquals(PrimitiveUtil.toDate((long) CorePublicConsts.MIN_LOCALDATE.toEpochDay()), CorePublicConsts.MIN_LOCALDATE);
        Assertions.assertEquals(PrimitiveUtil.toDate((int) CorePublicConsts.MIN_LOCALDATE.toEpochDay()), CorePublicConsts.MIN_LOCALDATE);
        Assertions.assertEquals(PrimitiveUtil.toDate((double) CorePublicConsts.MIN_LOCALDATE.toEpochDay()), CorePublicConsts.MIN_LOCALDATE);
    }

    @Test
    @DisplayName("Deve validar toTime")
    public void UI_CHECK_toTime() {
        var tm010203 = LocalTime.of(1, 2, 3);

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) ""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(LocalDate.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(LocalTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(tm010203));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) tm010203.toString()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) "01:02:03"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) "23:59:59.111222333"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) "23:59:59"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) "23:59:00"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) "23:59"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) 0));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) 0L));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) 0D));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((Object) 0.00));


        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime((String) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(tm010203.toString()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime("01:02:03"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime("23:59:59.111222333"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime("23:59:59"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime("23:59:00"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime("23:59"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(0));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(0L));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(0D));

        Assertions.assertNull(PrimitiveUtil.toTime(""));
        Assertions.assertEquals(PrimitiveUtil.toTime(tm010203.toString()), tm010203);
        Assertions.assertEquals(PrimitiveUtil.toTime("01:02:03"), tm010203);
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toTime(null));
        Assertions.assertEquals(PrimitiveUtil.toTime("23:59:59"), LocalTime.of(23, 59, 59));
        Assertions.assertEquals(PrimitiveUtil.toTime("23:59:00"), LocalTime.of(23, 59, 00));
        Assertions.assertEquals(PrimitiveUtil.toTime("23:59"), LocalTime.of(23, 59, 00));

        Assertions.assertEquals(PrimitiveUtil.toTime((long) CorePublicConsts.MIN_LOCALTIME.toSecondOfDay()), CorePublicConsts.MIN_LOCALTIME);
        Assertions.assertEquals(PrimitiveUtil.toTime((int) CorePublicConsts.MIN_LOCALTIME.toSecondOfDay()), CorePublicConsts.MIN_LOCALTIME);
        Assertions.assertEquals(PrimitiveUtil.toTime((double) CorePublicConsts.MIN_LOCALTIME.toSecondOfDay()), CorePublicConsts.MIN_LOCALTIME);

    }

    @Test
    @DisplayName("Deve validar toDateTime")
    public void UI_CHECK_toDateTime() {
        var dt19010101 = LocalDateTime.of(LocalDate.of(1901, 1, 1), LocalTime.of(1, 2, 3));

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(LocalDate.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(LocalTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(LocalDateTime.now()));

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime((Object) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime((Object) ""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(dt19010101));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime((Object) "1901-01-01T01:02:03"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime((Object) 0));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime((Object) 1.715027295E9));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime((Object) 0L));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime((Object) 0D));

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime((String) null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(dt19010101.toString()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01T01:02:03"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(0));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(1.715027295E9));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(0L));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(0D));

        Assertions.assertNull(PrimitiveUtil.toDateTime(""));
        Assertions.assertNull(PrimitiveUtil.toDateTime(0));
        Assertions.assertEquals(PrimitiveUtil.toDateTime(dt19010101.toString()), dt19010101);
        Assertions.assertEquals(PrimitiveUtil.toDateTime("1901-01-01T01:02:03"), dt19010101);

//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((LocalDateTime) null).toLocalTime());
        Assertions.assertEquals(PrimitiveUtil.toDateTime(1.715027295E9), LocalDateTime.of(LocalDate.of(2024, 5, 6), LocalTime.of(20, 28, 15)));
        Assertions.assertEquals(PrimitiveUtil.toDateTime("1901-01-01"), CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertEquals(PrimitiveUtil.toDateTime("1901-01-01T00"), CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertEquals(PrimitiveUtil.toDateTime("1901-01-01T00:00"), CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertEquals(PrimitiveUtil.toDateTime("1901-01-01T00:00:00"), CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertEquals(PrimitiveUtil.toDateTime("1901-01-01T23:59:59.999"), CorePublicConsts.MIN_LOCALDATETIME_MAX_TIME);

        Assertions.assertEquals(PrimitiveUtil.toDateTime(CorePublicConsts.MIN_LOCALDATETIME.toEpochSecond(ZoneOffset.UTC)), CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertEquals(PrimitiveUtil.toDateTime((double) CorePublicConsts.MIN_LOCALDATETIME.toEpochSecond(ZoneOffset.UTC)), CorePublicConsts.MIN_LOCALDATETIME);
    }

    @Test
    @DisplayName("Deve validar toBool")
    public void UI_CHECK_toBool() {
        Assertions.assertTrue(PrimitiveUtil.toBool("true"));
        Assertions.assertFalse(PrimitiveUtil.toBool("false"));
        Assertions.assertTrue(PrimitiveUtil.toBool("1"));
        Assertions.assertFalse(PrimitiveUtil.toBool("0"));
        Assertions.assertTrue(PrimitiveUtil.toBool(1));
        Assertions.assertFalse(PrimitiveUtil.toBool(0));
        Assertions.assertTrue(PrimitiveUtil.toBool(1D));
        Assertions.assertFalse(PrimitiveUtil.toBool(0D));
        Assertions.assertTrue(PrimitiveUtil.toBool(1L));
        Assertions.assertFalse(PrimitiveUtil.toBool(0L));
        Assertions.assertTrue(PrimitiveUtil.toBool(1.00));
        Assertions.assertFalse(PrimitiveUtil.toBool(0.00));
    }

    @Test
    @DisplayName("Deve validar isEmpty")
    public void UI_CHECK_isEmpty() {
        Object object = new Object();

        var d_tNull = LocalDateTime.now();
        var dtNull = LocalDate.now();
        var tmNull = LocalTime.now();
        var list = new ArrayList<>();
        var map = new HashMap<>();

        //noinspection ConstantValue
        Assertions.assertFalse(PrimitiveUtil.isEmpty(object));
        Assertions.assertFalse(PrimitiveUtil.isEmpty("A"));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(1));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(2L));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(3D));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(d_tNull));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(dtNull));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(tmNull));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(List.of(UUID.randomUUID())));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(Map.of(UUID.randomUUID().toString(), UUID.randomUUID().toString())));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(list));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(map));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((List) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((Map) null));

        //noinspection ConstantValue
        Assertions.assertTrue(PrimitiveUtil.isEmpty((Object) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((String) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(""));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(0));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(0L));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(0D));
        //noinspection ConstantValue
        Assertions.assertTrue(PrimitiveUtil.isEmpty((LocalDate) null) && PrimitiveUtil.isEmpty(TEST_MIN_LOCALDATETIME));
        //noinspection ConstantValue
        Assertions.assertTrue(PrimitiveUtil.isEmpty((LocalTime) null) && PrimitiveUtil.isEmpty(TEST_MIN_LOCALDATE));
        //noinspection ConstantValue
        Assertions.assertTrue(PrimitiveUtil.isEmpty((LocalDateTime) null) && PrimitiveUtil.isEmpty(TEST_MIN_LOCALDATE));
    }

    @Test
    @DisplayName("Deve validar inRange")
    public void UI_CHECK_InRange() {

        class Check<T> {
            final T v;
            final T begin;
            final T end;
            final boolean r;

            public Check(T v, T begin, T end, boolean r) {
                this.v = v;
                this.begin = begin;
                this.end = end;
                this.r = r;
            }

        }
        ;
        var curDate = LocalDate.now();

        List<Check<LocalDate>> listDate = List.of(
                new Check<>(curDate, curDate, curDate, true),
                new Check<>(curDate, curDate.minusDays(1), curDate, true),
                new Check<>(curDate, curDate, curDate.plusDays(1), true),
                new Check<>(curDate, null, curDate, false),
                new Check<>(curDate, curDate, null, false),
                new Check<>(null, null, null, false),
                new Check<>(curDate, curDate.plusDays(1), curDate, false),
                new Check<>(curDate, curDate, curDate.minusDays(1), false)
        );
        for (var check : listDate) {
            Assertions.assertDoesNotThrow(() -> PrimitiveUtil.inRange(check.v, check.begin, check.end));
            Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);
        }

        var curTime = LocalTime.now();
        List<Check<LocalTime>> listTime = List.of(
                new Check<>(curTime, curTime, curTime, true),
                new Check<>(curTime, curTime.minusMinutes(1), curTime, true),
                new Check<>(curTime, curTime, curTime.plusSeconds(1), true),
                new Check<>(curTime, null, curTime, false),
                new Check<>(curTime, curTime, null, false),
                new Check<>(null, null, null, false),
                new Check<>(curTime, curTime.plusMinutes(1), curTime, false),
                new Check<>(curTime, curTime, curTime.minusMinutes(1), false)
        );
        for (var check : listTime)
            Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);

        var curDateTime = LocalDateTime.of(curDate, curTime);
        List<Check<LocalDateTime>> listDateTime = List.of(
                new Check<>(curDateTime, curDateTime, curDateTime, true),
                new Check<>(curDateTime, curDateTime.minusSeconds(1), curDateTime, true),
                new Check<>(curDateTime, curDateTime, curDateTime.plusSeconds(1), true),
                new Check<>(curDateTime, null, curDateTime, false),
                new Check<>(curDateTime, curDateTime, null, false),
                new Check<>(null, null, null, false),
                new Check<>(curDateTime, curDateTime.plusDays(1), curDateTime, false),
                new Check<>(curDateTime, curDateTime, curDateTime.minusDays(1), false)
        );
        for (var check : listDateTime)
            Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);


        List<Check<Double>> listDouble = List.of(
                new Check<>(0D, 0D, 0D, true),
                new Check<>(1D, 0D, 1D, true),
                new Check<>(1D, 0D, 2D, true),
                new Check<>(0D, 0D, 2D, true),
                new Check<>(0D, -1D, 2D, true),
                new Check<>(0D, 1D, 2D, false),
                new Check<>(-1D, 1D, 2D, false)
        );
        for (var check : listDouble)
            Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);

    }

    @Test
    @DisplayName("Deve validar toUUID")
    public void UI_CHECK_toUUID() {
        var uuid = UUID.fromString("c4ca4238-a0b9-2382-0dcc-509a6f75849b");
        var uuid_str = "c4ca4238a0b923820dcc509a6f75849b";

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toUUID(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toUUID(null));

        Assertions.assertNull(PrimitiveUtil.toUUID(""));
        Assertions.assertEquals(PrimitiveUtil.toUUID(uuid.toString()), uuid);
        Assertions.assertEquals(PrimitiveUtil.toUUID(uuid_str), uuid);
    }

    @Test
    @DisplayName("Deve validar toURI")
    public void UI_CHECK_toURI() {
        Assertions.assertEquals(PrimitiveUtil.toURI(null), null);
        Assertions.assertEquals(PrimitiveUtil.toURI("/tmp/tmp"), URI.create("/tmp/tmp"));
    }

    @Test
    @DisplayName("Deve validar toFile")
    public void UI_CHECK_toFile() {
        Assertions.assertEquals(PrimitiveUtil.toFile(null), null);
        Assertions.assertEquals(PrimitiveUtil.toFile("/tmp/tmp").getAbsolutePath(), new File("/tmp/tmp").getAbsolutePath());
        Assertions.assertEquals(PrimitiveUtil.toPath("/tmp/tmp"), Path.of("/tmp/tmp"));
    }


    @Test
    @DisplayName("Deve validar toMap")
    public void UI_CHECK_toMap() {
        Assertions.assertTrue(PrimitiveUtil.toMap(null).isEmpty());
        Assertions.assertTrue(PrimitiveUtil.toMap("").isEmpty());
        Assertions.assertTrue(PrimitiveUtil.toMap("...").isEmpty());
        Assertions.assertTrue(PrimitiveUtil.toMap("{}").isEmpty());
        {
            var map = PrimitiveUtil.toMap("{\"a1\":\"v1\"}");
            Assertions.assertEquals(map.size(), 1);
            map = PrimitiveUtil.toMap("{\"a1\":\"v1\",\"a2\":\"v2\"}");
            Assertions.assertEquals(map.size(), 2);
            map = PrimitiveUtil.toMap("{\"a1\":\"v1\",\"a2\":\"v2\",\"a3\":\"v3\"}");
            Assertions.assertEquals(map.size(), 3);
            map = PrimitiveUtil.toMap("{\"a1\":\"v1\",\"a2\":\"v2\",\"a3\":\"v3\",\"a4\":\"v4\"}");
            Assertions.assertEquals(map.size(), 4);
        }
    }

    @Test
    @DisplayName("Deve validar toList")
    public void UI_CHECK_toList() {
        Assertions.assertTrue(PrimitiveUtil.toList(null).isEmpty());
        Assertions.assertTrue(PrimitiveUtil.toList("...").isEmpty());
        Assertions.assertTrue(PrimitiveUtil.toList("").isEmpty());
        Assertions.assertTrue(PrimitiveUtil.toList("[]").isEmpty());

        {
            var list = PrimitiveUtil.toList("[1,2,3,4]");
            Assertions.assertEquals(list.size(), 4);
            list = PrimitiveUtil.toList("[\"1\"]");
            Assertions.assertEquals(list.size(), 1);
            list = PrimitiveUtil.toList("[\"1\",\"2\"]");
            Assertions.assertEquals(list.size(), 2);
            list = PrimitiveUtil.toList("[\"1\",\"2\",\"3\"]");
            Assertions.assertEquals(list.size(), 3);
            list = PrimitiveUtil.toList("[\"1\",\"2\",\"3\",\"4\"]");
            Assertions.assertEquals(list.size(), 4);
        }
    }

    @Test
    @DisplayName("Deve validar toStringList")
    public void UI_CHECK_toStringList() {
        Assertions.assertTrue(PrimitiveUtil.toStringList(null).isEmpty());
        Assertions.assertEquals(PrimitiveUtil.toStringList("...").size(), 1);
        Assertions.assertTrue(PrimitiveUtil.toStringList("").isEmpty());
        Assertions.assertTrue(PrimitiveUtil.toStringList("[]").isEmpty());

        {
            var list = PrimitiveUtil.toStringList("[1,2,3,4]");
            Assertions.assertEquals(list.size(), 4);
            list = PrimitiveUtil.toStringList("[\"1\"]");
            Assertions.assertEquals(list.size(), 1);
            list = PrimitiveUtil.toStringList("[\"1\",\"2\"]");
            Assertions.assertEquals(list.size(), 2);
            list = PrimitiveUtil.toStringList("[\"1\",\"2\",\"3\"]");
            Assertions.assertEquals(list.size(), 3);
            list = PrimitiveUtil.toStringList("[\"1\",\"2\",\"3\",\"4\"]");
            Assertions.assertEquals(list.size(), 4);
        }

    }

    @Test
    @DisplayName("Deve validar toPath")
    public void UI_CHECK_toPath() {
        Assertions.assertEquals(PrimitiveUtil.toPath(null), null);
        Assertions.assertEquals(PrimitiveUtil.toPath("/tmp/tmp"), Path.of("/tmp/tmp"));
    }


}
