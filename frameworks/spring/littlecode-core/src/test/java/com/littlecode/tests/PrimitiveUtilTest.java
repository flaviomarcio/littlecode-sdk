package com.littlecode.tests;

import com.littlecode.parsers.PrimitiveUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class PrimitiveUtilTest {
    public static final LocalDate TEST_MIN_LOCALDATE = LocalDate.of(1901, 1, 1);
    public static final LocalTime TEST_MAX_LOCALTIME = LocalTime.of(23, 59, 59, 999);
    public static final LocalTime TEST_MIN_LOCALTIME = LocalTime.of(0, 0, 0, 0);
    public static final LocalDateTime TEST_MIN_LOCALDATETIME = LocalDateTime.of(TEST_MIN_LOCALDATE, TEST_MIN_LOCALTIME);
    public static final LocalDateTime TEST_MAX_LOCALDATETIME = LocalDateTime.of(LocalDate.now(), TEST_MAX_LOCALTIME);

    @Test
    public void UI_toString() {

        var dt = LocalDate.of(2000, 1, 3);
        var tm = LocalTime.of(22, 37, 12);
        Assertions.assertDoesNotThrow(() -> new PrimitiveUtil());
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(LocalDate.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(LocalTime.now()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString(new Object()));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toString((Object)null));
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
    public void UI_toUUID() {
        var uuid = UUID.fromString("c4ca4238-a0b9-2382-0dcc-509a6f75849b");
        var uuid_str = "c4ca4238a0b923820dcc509a6f75849b";
        Assertions.assertNull(PrimitiveUtil.toUUID(""));
        Assertions.assertEquals(PrimitiveUtil.toUUID(uuid.toString()), uuid);
        Assertions.assertEquals(PrimitiveUtil.toUUID(uuid_str), uuid);
    }

    @Test
    public void UI_toLong() {
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(""));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(null));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong("100"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong("100.1"));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(true));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(false));
        Assertions.assertDoesNotThrow(() -> PrimitiveUtil.toLong(1.5));

        Assertions.assertEquals(PrimitiveUtil.toLong(""), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong(null), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong("100"), 100);
        Assertions.assertEquals(PrimitiveUtil.toLong("100.1"), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong(true), 1);
        Assertions.assertEquals(PrimitiveUtil.toLong(false), 0);
        Assertions.assertEquals(PrimitiveUtil.toLong(1.5), 1);
    }

    @Test
    public void UI_toInt() {
        Assertions.assertEquals(PrimitiveUtil.toInt(""), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt("100"), 100);
        Assertions.assertEquals(PrimitiveUtil.toInt("100.1"), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt("9999999999999999999999999"), 0);
        Assertions.assertEquals(PrimitiveUtil.toInt(100), 100);
        Assertions.assertEquals(PrimitiveUtil.toInt(100.1), 100);
        Assertions.assertEquals(PrimitiveUtil.toInt(true), 1);
        Assertions.assertEquals(PrimitiveUtil.toInt(false), 0);
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
    }

    @Test
    public void UI_toDate() {
        var dt19010101 = LocalDate.of(1901, 1, 1);
        Assertions.assertNull(PrimitiveUtil.toDate(""));
        Assertions.assertEquals(PrimitiveUtil.toDate(dt19010101.toString()), dt19010101);
        Assertions.assertEquals(PrimitiveUtil.toDate("1901-01-01"), dt19010101);
    }

    @Test
    public void UI_toTime() {
        var tm010203 = LocalTime.of(1, 2, 3);
        Assertions.assertNull(PrimitiveUtil.toTime(""));
        Assertions.assertEquals(PrimitiveUtil.toTime(tm010203.toString()), tm010203);
        Assertions.assertEquals(PrimitiveUtil.toTime("01:02:03"), tm010203);
    }

    @Test
    public void UI_toDateTime() {
        var dt19010101 = LocalDateTime.of(LocalDate.of(1901, 1, 1), LocalTime.of(1, 2, 3));
        Assertions.assertNull(PrimitiveUtil.toDateTime(""));
        Assertions.assertEquals(PrimitiveUtil.toDateTime(dt19010101.toString()), dt19010101);
        Assertions.assertEquals(PrimitiveUtil.toDateTime("1901-01-01T01:02:03"), dt19010101);
    }

    @Test
    public void UI_toBool() {
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
    public void UI_isEmpty() {
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
        Assertions.assertTrue(PrimitiveUtil.isEmpty((Object)null));
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
                new Check<>(curDate, curDate.minusDays(1), curDate, true),
                new Check<>(curDate, curDate, curDate.plusDays(1), true),
                new Check<>(curDate, null, curDate, false),
                new Check<>(curDate, curDate, null, false),
                new Check<>(null, null, null, false),
                new Check<>(curDate, curDate.plusDays(1), curDate, false),
                new Check<>(curDate, curDate, curDate.minusDays(1), false)
        );
        for (var check : listDate){
            Assertions.assertDoesNotThrow(()->PrimitiveUtil.inRange(check.v, check.begin, check.end));
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

}
