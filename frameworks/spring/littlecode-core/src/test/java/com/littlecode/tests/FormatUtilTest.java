package com.littlecode.tests;

import com.littlecode.parsers.FormatUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class FormatUtilTest {

    @Test
    @DisplayName("Deve configurar Locale")
    void testLocale() {
        Assertions.assertDoesNotThrow(() -> FormatUtil.setLOCALE(null));
        Assertions.assertDoesNotThrow(() -> FormatUtil.setLOCALE(Locale.US));
        Assertions.assertDoesNotThrow(() -> FormatUtil.setLOCALE(new Locale("pt", "BR")));
        Assertions.assertDoesNotThrow(FormatUtil::getLOCALE);
        Assertions.assertNotNull(FormatUtil.getLOCALE());
    }

    @Test
    @DisplayName("Deve deve validar formatBool")
    void deveValidarformatValue() {
        Assertions.assertEquals(FormatUtil.formatValue("test"), "test");
        var uuid = UUID.randomUUID();
        Assertions.assertEquals(FormatUtil.formatValue(uuid), uuid.toString());
    }

    @Test
    @DisplayName("Deve deve validar formatBool")
    void deveValidarformatBool() {
        Assertions.assertEquals(FormatUtil.formatBool(true), "Sim");
        Assertions.assertEquals(FormatUtil.formatBool(false), "Não");
        Assertions.assertEquals(FormatUtil.formatValue(Boolean.TRUE), "Sim");
        Assertions.assertEquals(FormatUtil.formatValue(Boolean.FALSE), "Não");
    }

    @Test
    @DisplayName("Deve deve validar formatInt")
    void deveValidarformatInt() {

        Assertions.assertEquals(FormatUtil.formatInt(0), "0");
        Assertions.assertEquals(FormatUtil.formatInt(10000), "10.000");
        Assertions.assertEquals(FormatUtil.formatInt(1), "1");
        Assertions.assertEquals(FormatUtil.formatInt(12), "12");

        Assertions.assertEquals(FormatUtil.formatValue(0), "0");
        Assertions.assertEquals(FormatUtil.formatValue(10000), "10.000");
        Assertions.assertEquals(FormatUtil.formatValue(1), "1");
        Assertions.assertEquals(FormatUtil.formatValue(12), "12");

    }

    @Test
    @DisplayName("Deve deve validar formatInt")
    void deveValidarformatLong() {

        Assertions.assertEquals(FormatUtil.formatLong(0L), "0");
        Assertions.assertEquals(FormatUtil.formatLong(10000L), "10.000");
        Assertions.assertEquals(FormatUtil.formatLong(1L), "1");
        Assertions.assertEquals(FormatUtil.formatLong(12L), "12");

        Assertions.assertEquals(FormatUtil.formatValue(0L), "0");
        Assertions.assertEquals(FormatUtil.formatValue(10000L), "10.000");
        Assertions.assertEquals(FormatUtil.formatValue(1L), "1");
        Assertions.assertEquals(FormatUtil.formatValue(12L), "12");

    }

    @Test
    @DisplayName("Deve deve validar formatDouble")
    void deveValidarformatDouble() {

        Assertions.assertEquals(FormatUtil.formatDouble(0), "0,000");
        Assertions.assertEquals(FormatUtil.formatDouble(0D), "0,000");
        Assertions.assertEquals(FormatUtil.formatDouble(0L), "0,000");
        Assertions.assertEquals(FormatUtil.formatDouble(10000.12), "10.000,120");
        Assertions.assertEquals(FormatUtil.formatDouble(1.123), "1,123");
        Assertions.assertEquals(FormatUtil.formatDouble(12.1235), "12,123");
        Assertions.assertEquals(FormatUtil.formatDouble(new BigDecimal(12.1235).doubleValue()), "12,123");

        Assertions.assertEquals(FormatUtil.formatValue(0D), "0,000");
        Assertions.assertEquals(FormatUtil.formatValue(10000.12), "10.000,120");
        Assertions.assertEquals(FormatUtil.formatValue(1.123), "1,123");
        Assertions.assertEquals(FormatUtil.formatValue(12.1235), "12,123");
        Assertions.assertEquals(FormatUtil.formatValue(new BigDecimal(12.1235)), "12,123");
    }

    @Test
    @DisplayName("Deve deve validar formatCurrency")
    void deveValidarformatCurrency() {

        Assertions.assertEquals(FormatUtil.formatCurrency(0), "R$ 0,00");
        Assertions.assertEquals(FormatUtil.formatCurrency(0D), "R$ 0,00");
        Assertions.assertEquals(FormatUtil.formatCurrency(0L), "R$ 0,00");
        Assertions.assertEquals(FormatUtil.formatCurrency(10000.12), "R$ 10.000,12");
        Assertions.assertEquals(FormatUtil.formatCurrency(1.123), "R$ 1,12");
        Assertions.assertEquals(FormatUtil.formatCurrency(12.1235), "R$ 12,12");

    }

    @Test
    @DisplayName("Deve deve validar formatCurrency")
    void deveValidarformatTimes() {

        var date = LocalDate.of(1901, 1, 1);
        var time = LocalTime.of(23, 59, 59);
        var dateTime = LocalDateTime.of(date, time);
        Assertions.assertEquals(FormatUtil.formatDateTime(dateTime), "01/01/1901 23:59:59");
        Assertions.assertEquals(FormatUtil.formatDate(date), "01/01/1901");
        Assertions.assertEquals(FormatUtil.formatDate(LocalDateTime.of(date, LocalTime.now())), "01/01/1901");
        Assertions.assertEquals(FormatUtil.formatTime(time), "23:59:59");
        Assertions.assertEquals(FormatUtil.formatTime(LocalDateTime.of(date, time)), "23:59:59");

        Assertions.assertEquals(FormatUtil.formatValue(dateTime), "01/01/1901 23:59:59");
        Assertions.assertEquals(FormatUtil.formatValue(date), "01/01/1901");
        Assertions.assertEquals(FormatUtil.formatValue(time), "23:59:59");
    }

}
