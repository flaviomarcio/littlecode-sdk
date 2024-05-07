package com.littlecode.tests;

import com.littlecode.config.UtilCoreConfigConverters;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UtilCoreConfigConvertersTest {
    @Test
    @DisplayName("Deve validar class UtilCoreConfigConverters")
    public void UT_ObjectConverterConfig() {
        Assertions.assertDoesNotThrow(UtilCoreConfigConverters::converters);
        Assertions.assertNotNull(UtilCoreConfigConverters.converters());

        Assertions.assertDoesNotThrow(() -> LocalDate.parse("1901-01-01"));
        Assertions.assertNotNull(LocalDate.parse("1901-01-01"));

        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDate);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDateString);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDateTimeByStr);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalTime);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalTimeString);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDateTimeString);

        Assertions.assertDoesNotThrow(UtilCoreConfigConverters::modules);
        Assertions.assertNotNull(UtilCoreConfigConverters.modules());

    }
}
