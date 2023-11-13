package com.littlecode.tests;

import com.littlecode.privates.CoreUtilStarted;
import com.littlecode.util.BeanUtil;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class StartedTest {
    @Test
    public void UT_CHECK() {
        Assertions.assertDoesNotThrow(() -> {
            var context=Mockito.mock(ApplicationContext.class);
            var environment= Mockito.mock(Environment.class);
            new CoreUtilStarted(context,environment);
        });

    }
}
