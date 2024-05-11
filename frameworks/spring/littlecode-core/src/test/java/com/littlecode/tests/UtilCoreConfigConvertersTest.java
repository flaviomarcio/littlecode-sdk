package com.littlecode.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.config.UtilCoreConfigConverters;
import com.littlecode.parsers.ObjectUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.spi.MappingContext;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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



    @Test
    @DisplayName("Deve validar converters")
    void UT_CHECK_UtilCoreConfigConverters() {
        Assertions.assertNotNull(UtilCoreConfigConverters.converters());
        Assertions.assertNotNull(UtilCoreConfigConverters.modules());
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDate);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDateString);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalTime);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalTimeString);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDateTimeByStr);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDateTimeByDbl);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDateTimeByLng);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDateTimeByInt);
        Assertions.assertNotNull(UtilCoreConfigConverters.toLocalDateTimeString);



        var mappingContext= Mockito.mock(MappingContext.class);


        Assertions.assertDoesNotThrow(()->UtilCoreConfigConverters.toLocalDate.convert(mappingContext));
        Assertions.assertDoesNotThrow(()->UtilCoreConfigConverters.toLocalDateString.convert(mappingContext));
        Assertions.assertDoesNotThrow(()->UtilCoreConfigConverters.toLocalTime.convert(mappingContext));
        Assertions.assertDoesNotThrow(()->UtilCoreConfigConverters.toLocalTimeString.convert(mappingContext));
        Assertions.assertDoesNotThrow(()->UtilCoreConfigConverters.toLocalDateTimeByStr.convert(mappingContext));
        Assertions.assertDoesNotThrow(()->UtilCoreConfigConverters.toLocalDateTimeByDbl.convert(mappingContext));
        Assertions.assertDoesNotThrow(()->UtilCoreConfigConverters.toLocalDateTimeByLng.convert(mappingContext));
        Assertions.assertDoesNotThrow(()->UtilCoreConfigConverters.toLocalDateTimeByInt.convert(mappingContext));
        Assertions.assertDoesNotThrow(()->UtilCoreConfigConverters.toLocalDateTimeString.convert(mappingContext));
    }

    @Test
    @DisplayName("Deve validar converters")
    void UT_CHECK_MapperConfigModules() {
        var objectMapper= UtilCoreConfig.newObjectMapper();
        Assertions.assertDoesNotThrow(() -> objectMapper.writeValueAsString(new PrivateModuleObjectTest()));
        Assertions.assertDoesNotThrow(() -> objectMapper.readValue(ObjectUtil.toString(new PrivateModuleObjectTest()), PrivateModuleObjectTest.class));
//        Assertions.assertDoesNotThrow(() -> objectMapper.readValue("{\"dt\":100}", PrivateModuleObjectTest.class));
//        Assertions.assertDoesNotThrow(() -> objectMapper.readValue("{\"dt\":100.00}", PrivateModuleObjectTest.class));
//        Assertions.assertDoesNotThrow(() -> objectMapper.readValue("{\"dt\":100}", PrivateModuleObjectTest.class));
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrivateModuleObjectTest {
        private LocalDateTime dt=LocalDateTime.now();
        private LocalDate date=LocalDate.now();
        private LocalTime times=LocalTime.now();
    }
}
