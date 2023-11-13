package com.littlecode.tests;

import com.littlecode.cron.CronUtil;
import com.littlecode.scheduler.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SchedulerTest {
    @Test
    public void UT_CHECK() {
        Assertions.assertNotNull(Scheduler.CRON);
    }


}
