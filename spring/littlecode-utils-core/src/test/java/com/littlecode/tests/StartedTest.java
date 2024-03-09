package com.littlecode.tests;

import com.littlecode.privates.CoreUtilAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StartedTest {
    @Test
    public void UT_CHECK() {
        Assertions.assertDoesNotThrow(() -> {
            var context = Mockito.mock(ApplicationContext.class);
            var environment = Mockito.mock(Environment.class);
            new CoreUtilAutoConfiguration(context, environment);
        });

    }
}
