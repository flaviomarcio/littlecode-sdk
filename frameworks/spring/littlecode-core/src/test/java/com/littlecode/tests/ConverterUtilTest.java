package com.littlecode.tests;

import com.littlecode.parsers.ConverterUtil;
import com.littlecode.config.CorePublicConsts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ConverterUtilTest {

    @Test
    public void UT_000_CHECK_CONSTRUCTOR() {
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(""));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDate.now()));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalTime.now()));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(new File("/tmp/file")));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(new Object()));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Path.of("/tmp/file")));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(URI.create("/tmp/file")));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID()));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((Double)null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((File) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((Integer) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((LocalDate) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((LocalDateTime) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((LocalTime) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((Long) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((Object) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((Path) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((String) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((URI) null));
//        Assertions.assertThrows(NullPointerException.class,() -> new ConverterUtil((UUID) null));

        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(""));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(0));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(0D));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(0L));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(LocalDate.now()));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(LocalTime.now()));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(new File("/tmp/file")));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(new Object()));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(Path.of("/tmp/file")));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(URI.create("/tmp/file")));
        Assertions.assertDoesNotThrow(() -> ConverterUtil.of(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(() -> new ConverterUtil("").getTarget());
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((Double)null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((File) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((Integer) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((LocalDate) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((LocalDateTime) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((LocalTime) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((Long) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((Object) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((Path) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((String) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((URI) null));
//        Assertions.assertThrows(NullPointerException.class,() -> ConverterUtil.of((UUID) null));


    }

    @Test
    public void UT_000_CHECK_CONVERT_toString() {
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID().toString()).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(String.valueOf(0)).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0.00).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Double.parseDouble("0")).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDate.now()).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalTime.now()).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(URI.create("/tmp/file")).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Path.of("/tmp/file")).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(new File("/tmp/file")).toString());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID()).toString());

        var path=Path.of("/tmp/file");
        Assertions.assertEquals(new ConverterUtil(path.toString()).toString(), path.toString());
        Assertions.assertEquals(new ConverterUtil(path).toString(), path.toString());
        Assertions.assertEquals(new ConverterUtil(path.toUri()).toString(), path.toUri().toString());
        Assertions.assertEquals(new ConverterUtil(path.toFile()).toString(), path.toFile().getAbsolutePath());
        Assertions.assertEquals(new ConverterUtil(CorePublicConsts.MIN_LOCALDATE).toString(), CorePublicConsts.MIN_LOCALDATE.toString());
        Assertions.assertEquals(new ConverterUtil(CorePublicConsts.MIN_LOCALTIME).toString(), CorePublicConsts.MIN_LOCALTIME.toString());
        Assertions.assertEquals(new ConverterUtil(CorePublicConsts.MIN_LOCALDATETIME).toString(), CorePublicConsts.MIN_LOCALDATETIME.toString());
    }

    @Test
    public void UT_000_CHECK_CONVERT_toBool() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("0").toBool());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toBool());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L).toBool());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D).toBool());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toBool());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0.00).toBool());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Boolean.TRUE).toBool());

        Assertions.assertFalse(new ConverterUtil(LocalDate.now()).toBool());
        Assertions.assertFalse(new ConverterUtil(0).toBool());
        Assertions.assertFalse(new ConverterUtil(0D).toBool());
        Assertions.assertFalse(new ConverterUtil(0L).toBool());
        Assertions.assertFalse(new ConverterUtil("").toBool());
        Assertions.assertFalse(new ConverterUtil("0").toBool());
        Assertions.assertFalse(new ConverterUtil("test").toBool());
        Assertions.assertFalse(new ConverterUtil("false").toBool());
        Assertions.assertFalse(new ConverterUtil(Boolean.FALSE).toBool());

        Assertions.assertTrue(new ConverterUtil(1).toBool());
        Assertions.assertTrue(new ConverterUtil(1D).toBool());
        Assertions.assertTrue(new ConverterUtil(1L).toBool());
        Assertions.assertTrue(new ConverterUtil("1").toBool());
        Assertions.assertTrue(new ConverterUtil("t").toBool());
        Assertions.assertTrue(new ConverterUtil("true").toBool());
        Assertions.assertTrue(new ConverterUtil(Boolean.TRUE).toBool());
    }

    @Test
    public void UT_000_CHECK_CONVERT_toInt() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("0").toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L).toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D).toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0.00).toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Double.parseDouble("0")).toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDate.now()).toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalTime.now()).toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toInt());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID()).toInt());

        Assertions.assertEquals(new ConverterUtil("").toInt(),0);
    }

    @Test
    public void UT_000_CHECK_CONVERT_toLong() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("0").toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L).toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D).toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0.00).toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Double.parseDouble("0")).toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDate.now()).toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalTime.now()).toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toLong());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID()).toLong());

        Assertions.assertEquals(new ConverterUtil("").toLong(),0L);
    }

    @Test
    public void UT_000_CHECK_CONVERT_toDouble() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("0").toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L).toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D).toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0.00).toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Double.parseDouble("0")).toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDate.now()).toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalTime.now()).toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toDouble());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID()).toDouble());

        Assertions.assertEquals(new ConverterUtil("0").toDouble(),0L);
    }

    @Test
    public void UT_000_CHECK_CONVERT_toLocalDate() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("0").toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L).toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D).toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0.00).toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Double.parseDouble("0")).toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDate.now()).toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalTime.now()).toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toLocalDate());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID()).toLocalDate());

        Assertions.assertEquals(new ConverterUtil("1901-01-01").toLocalDate(),LocalDate.of(1901,1,1));
    }


    @Test
    public void UT_000_CHECK_CONVERT_toLocalTime() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("0").toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L).toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D).toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0.00).toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Double.parseDouble("0")).toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDate.now()).toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalTime.now()).toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toLocalTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID()).toLocalTime());

        Assertions.assertEquals(new ConverterUtil("23:59:59").toLocalTime(),LocalTime.of(23,59,59));
        Assertions.assertEquals(new ConverterUtil("23:59:00").toLocalTime(),LocalTime.of(23,59,00));
        Assertions.assertEquals(new ConverterUtil("23:59").toLocalTime(),LocalTime.of(23,59,00));
    }

    @Test
    public void UT_000_CHECK_CONVERT_toLocalDateTime() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("0").toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L).toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D).toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0.00).toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Double.parseDouble("0")).toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDate.now()).toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalTime.now()).toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toLocalDateTime());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID()).toLocalDateTime());

        Assertions.assertEquals(new ConverterUtil("1901-01-01").toLocalDateTime(), CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertEquals(new ConverterUtil("1901-01-01T00").toLocalDateTime(), CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertEquals(new ConverterUtil("1901-01-01T00:00").toLocalDateTime(), CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertEquals(new ConverterUtil("1901-01-01T00:00:00").toLocalDateTime(), CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertEquals(new ConverterUtil("1901-01-01T23:59:59.999").toLocalDateTime(), CorePublicConsts.MIN_LOCALDATETIME_MAX_TIME);
    }

    @Test
    public void UT_000_CHECK_CONVERT_toUUID() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("0").toUUID());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toUUID());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0L).toUUID());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0D).toUUID());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0).toUUID());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(0.00).toUUID());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID()).toUUID());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(UUID.randomUUID().toString()).toUUID());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toUUID());

        var uuid=UUID.randomUUID();
        Assertions.assertEquals(new ConverterUtil(uuid).toUUID(), uuid);
        Assertions.assertEquals(new ConverterUtil(uuid.toString()).toUUID(), uuid);
        Assertions.assertNull(new ConverterUtil(LocalDateTime.now()).toUUID());
    }

    @Test
    public void UT_000_CHECK_CONVERT_toURI() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("/tmp/tmp").toURI());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Path.of("/tmp/tmp")).toURI());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(new File("/tmp/tmp")).toURI());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toURI());

        var path=Path.of("/tmp/tmp");
        Assertions.assertEquals(new ConverterUtil(path).toURI(), path.toUri());
        Assertions.assertEquals(new ConverterUtil(path.toUri()).toURI(), path.toUri());
        Assertions.assertEquals(new ConverterUtil(path.toFile()).toURI(), path.toUri());
        Assertions.assertNull(new ConverterUtil(LocalDateTime.now()).toURI());
    }

    @Test
    public void UT_000_CHECK_CONVERT_toPath() {

        Assertions.assertDoesNotThrow(() -> new ConverterUtil("/tmp/tmp").toPath());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Path.of("/tmp/tmp")).toPath());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(Path.of("/tmp/tmp").toUri()).toPath());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(new File("/tmp/tmp")).toPath());
        Assertions.assertDoesNotThrow(() -> new ConverterUtil(LocalDateTime.now()).toPath());

        var path=Path.of("/tmp/tmp");
        Assertions.assertEquals(new ConverterUtil(path).toPath(), path);
        Assertions.assertEquals(new ConverterUtil(path.toUri()).toURI(), path.toUri());
        Assertions.assertEquals(new ConverterUtil(path.toFile()).toPath(), path);
        Assertions.assertNull(new ConverterUtil(LocalDateTime.now()).toPath());
    }






}
