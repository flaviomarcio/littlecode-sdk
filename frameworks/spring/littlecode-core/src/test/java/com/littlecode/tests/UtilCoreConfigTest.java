package com.littlecode.tests;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.dataformat.javaprop.JavaPropsFactory;
import com.fasterxml.jackson.dataformat.xml.XmlFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.littlecode.config.UtilCoreConfig;
import com.littlecode.files.FileFormat;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UtilCoreConfigTest {
    @Test
    @DisplayName("Deve validar class UtilCoreConfig")
    public void UT_CHECK() {
        var context = Mockito.mock(ApplicationContext.class);
        var environment = Mockito.mock(Environment.class);
        Assertions.assertThrows(NullPointerException.class, () -> new UtilCoreConfig(context, null));
        Assertions.assertThrows(NullPointerException.class, () -> new UtilCoreConfig(null, null));
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

}
