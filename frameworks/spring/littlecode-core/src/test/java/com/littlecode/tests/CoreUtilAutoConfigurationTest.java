package com.littlecode.tests;

import com.littlecode.privates.CoreUtilAutoConfiguration;
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
class CoreUtilAutoConfigurationTest {
    @Test
    @DisplayName("Deve validar class CoreUtilAutoConfiguration")
    void UT_CHECK() {
        var context = Mockito.mock(ApplicationContext.class);
        var environment = Mockito.mock(Environment.class);
        Assertions.assertDoesNotThrow(() -> new CoreUtilAutoConfiguration(context, environment));
        Assertions.assertDoesNotThrow(() -> new CoreUtilAutoConfiguration(context, environment).getCoreConfig());

        var config = new CoreUtilAutoConfiguration(context, environment);
        Assertions.assertNotNull(config);
        Assertions.assertNotNull(config.getCoreConfig());


    }
}
