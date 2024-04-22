package com.littlecode.tests;

import com.littlecode.parsers.PrimitiveUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.math.BigDecimal;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class PrimitiveUtilTest {
    public static final int INT_MAX = 1241513983;
    public static final LocalDate TEST_MIN_LOCALDATE = LocalDate.of(1901, 1, 1);
    public static final LocalTime TEST_MAX_LOCALTIME = LocalTime.of(23, 59, 59, 999);
    public static final LocalTime TEST_MIN_LOCALTIME = LocalTime.of(0, 0, 0, 0);
    public static final LocalDateTime TEST_MIN_LOCALDATETIME = LocalDateTime.of(TEST_MIN_LOCALDATE, TEST_MIN_LOCALTIME);
    public static final LocalDateTime TEST_MAX_LOCALDATETIME = LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME);

    @Test
    public void UI_formatDouble() {
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.formatDouble(100.123, 0));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.formatDouble(100.123, 1));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.formatDouble(100.123, 2));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.formatDouble(100.123456));

        Assertions.assertEquals(PrimitiveUtil.formatDouble(987.123, 0), "987");
        Assertions.assertEquals(PrimitiveUtil.formatDouble(987.123, 1), "987.1");
        Assertions.assertEquals(PrimitiveUtil.formatDouble(987.123, 2), "987.12");
        Assertions.assertEquals(PrimitiveUtil.formatDouble(987.123, 3), "987.123");
        Assertions.assertEquals(PrimitiveUtil.formatDouble(987.123456), "987.123456");

    }

    @Test
    public void UI_toString() {

        Assertions.assertEquals(PrimitiveUtil.toString((LocalDateTime) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((LocalDate) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((LocalTime) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((UUID) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((URI) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((Path) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString((File) null), "");

        Assertions.assertEquals(PrimitiveUtil.toString(Boolean.TRUE), "true");
        Assertions.assertEquals(PrimitiveUtil.toString(Boolean.FALSE), "false");
        var dt = LocalDate.of(2000, 1, 3);
        var tm = LocalTime.of(22, 37, 12);
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

        Assertions.assertEquals(PrimitiveUtil.toString((String) null), "");
        Assertions.assertEquals(PrimitiveUtil.toString("a"), "a");
        Assertions.assertEquals(PrimitiveUtil.toString("test"), "test");
        var uuid = UUID.randomUUID();
        Assertions.assertEquals(PrimitiveUtil.toString(uuid), uuid.toString());

        var path = Path.of("http://localhost:8080");
        Assertions.assertEquals(PrimitiveUtil.toString(path), path.toString());
        Assertions.assertEquals(PrimitiveUtil.toString(path.toUri()), path.toUri().toString());
        Assertions.assertEquals(PrimitiveUtil.toString(path.toFile()), path.toFile().getAbsolutePath());
    }

    @Test
    public void UI_toUUID() {
        var uuid = UUID.fromString("c4ca4238-a0b9-2382-0dcc-509a6f75849b");
        var uuid_str = "c4ca4238a0b923820dcc509a6f75849b";
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toUUID(null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toUUID(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toUUID(uuid.toString()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toUUID(uuid_str));

        Assertions.assertNull(PrimitiveUtil.toUUID(null));
        Assertions.assertNull(PrimitiveUtil.toUUID(""));
        Assertions.assertEquals(PrimitiveUtil.toUUID(uuid.toString()), uuid);
        Assertions.assertEquals(PrimitiveUtil.toUUID(uuid_str), uuid);
    }

    @Test
    public void UI_toPath() {
        Assertions.assertNull(PrimitiveUtil.toPath(null));
        Assertions.assertNull(PrimitiveUtil.toPath(""));
        Assertions.assertNotNull(PrimitiveUtil.toPath("/temp/file"));

        Assertions.assertNull(PrimitiveUtil.toURI(null));
        Assertions.assertNull(PrimitiveUtil.toURI(""));
        Assertions.assertNotNull(PrimitiveUtil.toURI("http://localhost"));
    }

    @Test
    public void UI_toLong() {
        Assertions.assertEquals(PrimitiveUtil.toLong(null), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong(""), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong("100"), 100);
        Assertions.assertEquals(PrimitiveUtil.toLong(100), 100);

        Assertions.assertEquals(PrimitiveUtil.toLong("100.1"), 100);
        Assertions.assertEquals(PrimitiveUtil.toLong("100.6"), 101);
        Assertions.assertEquals(PrimitiveUtil.toLong(100.1), 100);
        Assertions.assertEquals(PrimitiveUtil.toLong(100.6), 101);

        Assertions.assertEquals(PrimitiveUtil.toLong("999999999"), 999999999);
        Assertions.assertEquals(PrimitiveUtil.toLong(999999999), 999999999);
        Assertions.assertEquals(PrimitiveUtil.toLong(true), 1);
        Assertions.assertEquals(PrimitiveUtil.toLong(false), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong(Boolean.TRUE), 1);
        Assertions.assertEquals(PrimitiveUtil.toLong(Boolean.FALSE), 0);
    }

    @Test
    public void UI_toInt() {
        Assertions.assertEquals(PrimitiveUtil.toInt(null), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt(""), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt("100"), 100);
        Assertions.assertEquals(PrimitiveUtil.toInt(100), 100);

        Assertions.assertEquals(PrimitiveUtil.toInt("100.1"), 100);
        Assertions.assertEquals(PrimitiveUtil.toInt("100.6"), 101);
        Assertions.assertEquals(PrimitiveUtil.toInt(100.1), 100);
        Assertions.assertEquals(PrimitiveUtil.toInt(100.6), 101);

        Assertions.assertEquals(PrimitiveUtil.toInt("999999999"), 999999999);
        Assertions.assertEquals(PrimitiveUtil.toInt(999999999), 999999999);
        Assertions.assertEquals(PrimitiveUtil.toInt(true), 1);
        Assertions.assertEquals(PrimitiveUtil.toInt(false), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt(Boolean.TRUE), 1);
        Assertions.assertEquals(PrimitiveUtil.toInt(Boolean.FALSE), 0);
    }

    @Test
    public void UI_toBigDecimal() {
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("12").doubleValue(), 12);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("12.13").doubleValue(), 12.13D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("12.13", 0).doubleValue(), 12D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("").doubleValue(), 0D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("").doubleValue(), 0D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("0").doubleValue(), 0D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("-100").doubleValue(), -100D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("100").doubleValue(), 100D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("100.00").doubleValue(), 100D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("0.01").doubleValue(), 0.01D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("100.1").doubleValue(), 100.1D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("9999999999999999999999999").doubleValue(), 9999999999999999999999999D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("100.11001", 0).doubleValue(), 100D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("100.11001", 1).doubleValue(), 100.1D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("100.12001", 2).doubleValue(), 100.120D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("100.12301", 3).doubleValue(), 100.123D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("100.12341", 4).doubleValue(), 100.1234D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("100.12341", 5).doubleValue(), 100.12341D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("240.225", 5).doubleValue(), 240.225D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("240.225", 3).doubleValue(), 240.225D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("240.225", 2).doubleValue(), 240.23D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal("240.23", 2).doubleValue(), 240.23D);

        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(12).doubleValue(), 12);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(12D).doubleValue(), 12);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(12L).doubleValue(), 12);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(12.13).doubleValue(), 12.13D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(12.13D).doubleValue(), 12.13D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(12.13D, 0).doubleValue(), 12D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(12.13D, 0).doubleValue(), 12D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(0).doubleValue(), 0D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(0D).doubleValue(), 0D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(0L).doubleValue(), 0D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(-100).doubleValue(), -100D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(100).doubleValue(), 100D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(100.00).doubleValue(), 100D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(0.01).doubleValue(), 0.01D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(100.1).doubleValue(), 100.1D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(9999999999999999999999999D).doubleValue(), 9999999999999999999999999D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(100.11001, 0).doubleValue(), 100D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(100.11001, 1).doubleValue(), 100.1D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(100.12001, 2).doubleValue(), 100.120D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(100.12301, 3).doubleValue(), 100.123D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(100.12341, 4).doubleValue(), 100.1234D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(100.12341, 5).doubleValue(), 100.12341D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(240.225D, 5).doubleValue(), 240.225D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(240.225, 3).doubleValue(), 240.225D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(240.225, 2).doubleValue(), 240.22D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(240.23, 2).doubleValue(), 240.23D);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(240.23, 1).doubleValue(), 240.2D);

        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(false).intValue(), 0);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(true).intValue(), 1);

        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(Boolean.FALSE).intValue(), 0);
        Assertions.assertEquals(PrimitiveUtil.toBigDecimal(Boolean.TRUE).intValue(), 1);
    }

    @Test
    public void UI_toDouble() {
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


        Assertions.assertEquals(PrimitiveUtil.toDouble(BigDecimal.valueOf(12.12)), 12.12D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.11001D, 0), 100D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.11001D, 1), 100.1D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.12001D, 2), 100.120D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.12301D, 3), 100.123D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.12341D, 4), 100.1234D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(100.12341D, 5), 100.12341D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240.225, 5), 240.225D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240.225, 3), 240.225D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240.225, 2), 240.22D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240.23, 2), 240.23D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240), 240D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(240L), 240D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(true), 1D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(false), 0D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(Boolean.TRUE), 1D);
        Assertions.assertEquals(PrimitiveUtil.toDouble(Boolean.FALSE), 0D);
    }

    @Test
    public void UI_toDate() {
        var dt = LocalDateTime.now();
        var dt19010101 = LocalDate.of(1901, 1, 1);
        Assertions.assertNull(PrimitiveUtil.toDate(""));
        Assertions.assertEquals(PrimitiveUtil.toDate(dt19010101.toString()), dt19010101);
        Assertions.assertEquals(PrimitiveUtil.toDate("1901-01-01"), dt19010101);
        Assertions.assertEquals(PrimitiveUtil.toDate(dt), dt.toLocalDate());
        Assertions.assertEquals(PrimitiveUtil.toDate(dt19010101.toEpochDay()), dt19010101);
    }

    @Test
    public void UI_toTime() {
        var dt = LocalDateTime.now();
        var tm010203 = LocalTime.of(1, 2, 3);
        Assertions.assertNull(PrimitiveUtil.toTime((String) null));
        Assertions.assertNull(PrimitiveUtil.toTime(""));
        Assertions.assertEquals(PrimitiveUtil.toTime(tm010203.toString()), tm010203);
        Assertions.assertEquals(PrimitiveUtil.toTime("01:02:03"), tm010203);
        Assertions.assertEquals(PrimitiveUtil.toTime(dt), dt.toLocalTime());
        Assertions.assertEquals(PrimitiveUtil.toTime(tm010203.toSecondOfDay()), tm010203);
    }

    @Test
    public void UI_toDateTime() {
        var dt19010101 = LocalDateTime.of(LocalDate.of(1901, 1, 1), LocalTime.of(1, 2, 3));
        var dt = dt19010101.toLocalDate();
        var tm = dt19010101.toLocalTime();

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(dt19010101.toString()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01T01:02:03.999000001"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01T01:02:03"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01T01:02"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01T01"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("01-01-01"));

        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(dt19010101.toString()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01T01:02:03.999000001"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01T01:02:03"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01T01:02"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01T01"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("1901-01-01"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime("01-01-01"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(dt));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(tm));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toDateTime(dt19010101.toEpochSecond(ZoneOffset.UTC)));


        Assertions.assertNull(PrimitiveUtil.toDateTime((LocalDate) null));
        Assertions.assertNull(PrimitiveUtil.toDateTime((LocalTime) null));

        Assertions.assertNull(PrimitiveUtil.toDateTime(""));
        Assertions.assertEquals(PrimitiveUtil.toDateTime(dt19010101.toString()), dt19010101);
        Assertions.assertNotNull(PrimitiveUtil.toDateTime("1901-01-01T01:02:03.999000001"));
        Assertions.assertNotNull(PrimitiveUtil.toDateTime("1901-01-01T01:02:03"));
        Assertions.assertNotNull(PrimitiveUtil.toDateTime("1901-01-01T01:02"));
        Assertions.assertNotNull(PrimitiveUtil.toDateTime("1901-01-01T01"));
        Assertions.assertNull(PrimitiveUtil.toDateTime("1901-01-01"));
        Assertions.assertNull(PrimitiveUtil.toDateTime("01-01-01"));
        Assertions.assertEquals(PrimitiveUtil.toDateTime(dt), LocalDateTime.of(dt, TEST_MIN_LOCALTIME));
        Assertions.assertEquals(PrimitiveUtil.toDateTime(tm), LocalDateTime.of(TEST_MIN_LOCALDATE, tm));
        Assertions.assertEquals(PrimitiveUtil.toDateTime(dt19010101.toEpochSecond(ZoneOffset.UTC)), dt19010101);
    }

    @Test
    public void UI_toBool() {
        Assertions.assertFalse(PrimitiveUtil.toBool((String) null));
        Assertions.assertTrue(PrimitiveUtil.toBool("true"));
        Assertions.assertFalse(PrimitiveUtil.toBool("false"));
        Assertions.assertTrue(PrimitiveUtil.toBool("1"));
        Assertions.assertFalse(PrimitiveUtil.toBool("0"));
        Assertions.assertTrue(PrimitiveUtil.toBool(1));
        Assertions.assertFalse(PrimitiveUtil.toBool(0));
        Assertions.assertTrue(PrimitiveUtil.toBool(1L));
        Assertions.assertFalse(PrimitiveUtil.toBool(0L));
        Assertions.assertTrue(PrimitiveUtil.toBool(1D));
        Assertions.assertFalse(PrimitiveUtil.toBool(0D));
    }

    @Test
    public void UI_isEmpty() {
        Assertions.assertTrue(PrimitiveUtil.isEmpty((LocalDate) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((LocalTime) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((LocalDateTime) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((List) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((UUID) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((String) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((URI) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((Path) null));

        Assertions.assertTrue(PrimitiveUtil.isEmpty(TEST_MIN_LOCALDATETIME));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(TEST_MIN_LOCALDATE));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(TEST_MIN_LOCALDATE));

        Assertions.assertTrue(PrimitiveUtil.isEmpty((UUID) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((String) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((URI) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty((Path) null));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(Path.of("")));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(URI.create("")));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(""));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(" "));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(0));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(new ArrayList<>()));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(0L));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(0.00));
        Assertions.assertTrue(PrimitiveUtil.isEmpty(0D));


        Assertions.assertFalse(PrimitiveUtil.isEmpty(UUID.randomUUID()));
        Assertions.assertFalse(PrimitiveUtil.isEmpty("...."));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(URI.create("/tmp/teste")));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(Path.of("/tmp/teste")));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(1));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(List.of("....")));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(1L));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(0.12));
        Assertions.assertFalse(PrimitiveUtil.isEmpty(2D));
    }

    @Test
    public void UI_InRange() {

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
                new Check<>(curDate, null, curDate, true),
                new Check<>(curDate, curDate, null, true),
                new Check<>(null, null, null, false),
                new Check<>(curDate, curDate.plusDays(1), curDate, false),
                new Check<>(curDate, curDate, curDate.minusDays(1), false)
        );
        for (var check : listDate)
            Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);

        var curTime = LocalTime.now();
        List<Check<LocalTime>> listTime = List.of(
                new Check<>(curTime, curTime, curTime, true),
                new Check<>(curTime, null, curTime, true),
                new Check<>(curTime, curTime, null, true),
                new Check<>(null, null, null, false),
                new Check<>(curTime, curTime.plusMinutes(1), curTime, false),
                new Check<>(curTime, curTime, curTime.minusMinutes(1), false)
        );
        for (var check : listTime)
            Assertions.assertEquals(PrimitiveUtil.inRange(check.v, check.begin, check.end), check.r);

        var curDateTime = LocalDateTime.of(curDate, curTime);
        List<Check<LocalDateTime>> listDateTime = List.of(
                new Check<>(curDateTime, curDateTime, curDateTime, true),
                new Check<>(curDateTime, null, curDateTime, true),
                new Check<>(curDateTime, curDateTime, null, true),
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

}
