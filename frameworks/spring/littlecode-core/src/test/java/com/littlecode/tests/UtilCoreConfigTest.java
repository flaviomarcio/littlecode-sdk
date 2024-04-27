package com.littlecode.tests;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.littlecode.config.UtilCoreConfig;
import com.littlecode.files.FileFormat;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.time.LocalDate;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UtilCoreConfigTest {
    @Test
    public void UT_CHECK() {
        var context = Mockito.mock(ApplicationContext.class);
        var environment = Mockito.mock(Environment.class);
        Assertions.assertDoesNotThrow(() -> new UtilCoreConfig(context, environment));
        Assertions.assertDoesNotThrow(UtilCoreConfig::getApplicationContext);
        Assertions.assertDoesNotThrow(UtilCoreConfig::getEnvironment);

        Assertions.assertNotNull(UtilCoreConfig.getApplicationContext());
        Assertions.assertNotNull(UtilCoreConfig.getEnvironment());

        Assertions.assertDoesNotThrow(() -> UtilCoreConfig.getFactoryFromFileFormat(null));
        Assertions.assertDoesNotThrow(() -> UtilCoreConfig.getFactoryFromFileFormat(FileFormat.JSON));
        Assertions.assertDoesNotThrow(() -> UtilCoreConfig.getFactoryFromFileFormat(FileFormat.XML));
        Assertions.assertDoesNotThrow(() -> UtilCoreConfig.getFactoryFromFileFormat(FileFormat.YML));
        Assertions.assertDoesNotThrow(() -> UtilCoreConfig.getFactoryFromFileFormat(FileFormat.PROPS));

        Assertions.assertNotNull(UtilCoreConfig.getFactoryFromFileFormat(null));
        Assertions.assertNotNull(UtilCoreConfig.getFactoryFromFileFormat(FileFormat.JSON));
        Assertions.assertNotNull(UtilCoreConfig.getFactoryFromFileFormat(FileFormat.XML));
        Assertions.assertNotNull(UtilCoreConfig.getFactoryFromFileFormat(FileFormat.YML));
        Assertions.assertNotNull(UtilCoreConfig.getFactoryFromFileFormat(FileFormat.PROPS));

        Assertions.assertEquals(UtilCoreConfig.getFactoryFromFileFormat(null).getClass(), JsonFactory.class);
        Assertions.assertEquals(UtilCoreConfig.getFactoryFromFileFormat(FileFormat.JSON).getClass(), JsonFactory.class);
        Assertions.assertEquals(UtilCoreConfig.getFactoryFromFileFormat(FileFormat.XML).getClass(), XmlFactory.class);
        Assertions.assertEquals(UtilCoreConfig.getFactoryFromFileFormat(FileFormat.YML).getClass(), YAMLFactory.class);
        Assertions.assertEquals(UtilCoreConfig.getFactoryFromFileFormat(FileFormat.PROPS).getClass(), JavaPropsFactory.class);
    }

    @Test
    public void UT_ObjectConverter() {
        Assertions.assertDoesNotThrow(UtilCoreConfig.ObjectConverter::converters);
        Assertions.assertNotNull(UtilCoreConfig.ObjectConverter.converters());

        Assertions.assertDoesNotThrow(() -> LocalDate.parse("1901-01-01"));
        Assertions.assertNotNull(LocalDate.parse("1901-01-01"));

        Assertions.assertNotNull(UtilCoreConfig.ObjectConverter.toLocalDate);
        Assertions.assertNotNull(UtilCoreConfig.ObjectConverter.toLocalDateString);
        Assertions.assertNotNull(UtilCoreConfig.ObjectConverter.toLocalDateTime);
        Assertions.assertNotNull(UtilCoreConfig.ObjectConverter.toLocalTime);
        Assertions.assertNotNull(UtilCoreConfig.ObjectConverter.toLocalTimeString);
        Assertions.assertNotNull(UtilCoreConfig.ObjectConverter.toLocalDateTimeString);

        Assertions.assertDoesNotThrow(UtilCoreConfig.ObjectConverter::modules);
        Assertions.assertNotNull(UtilCoreConfig.ObjectConverter.modules());

    }
}
