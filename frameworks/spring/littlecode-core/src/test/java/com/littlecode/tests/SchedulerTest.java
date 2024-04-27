package com.littlecode.tests;

import com.littlecode.scheduler.Scheduler;
import com.littlecode.scheduler.SchedulerAutoConfiguration;
import com.littlecode.scheduler.SchedulerSetting;
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
public class SchedulerTest {
    @Test
    public void UT_CHECK_CONSTRUCTOR() {
        Assertions.assertDoesNotThrow(() -> new SchedulerSetting());
        Assertions.assertDoesNotThrow(() -> new SchedulerSetting().isAutoStart());
        Assertions.assertDoesNotThrow(() -> new SchedulerSetting().isLog());

        Assertions.assertNotNull(Scheduler.CRON);
        Assertions.assertFalse(new SchedulerSetting().isAutoStart());
        Assertions.assertFalse(new SchedulerSetting().isLog());


        Assertions.assertDoesNotThrow(() -> new SchedulerAutoConfiguration(Mockito.mock(ApplicationContext.class),Mockito.mock(Environment.class)));
    }


}
