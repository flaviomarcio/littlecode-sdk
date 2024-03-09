package com.littlecode.tests;

import com.littlecode.cron.CronUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class CronUtilTest {
    @Test
    public void UT_CHECK_CRON() {
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron(0));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron(0, 0));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron(0, 0, 0));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron(0, 0, 0, 0));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron(0, 0, 0, 0));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron(0, 0, 0, 0, 0, 0));

        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron(""));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron("", ""));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron("", "", ""));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron("", "", "", ""));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron("", "", "", ""));
        Assertions.assertDoesNotThrow(() -> CronUtil.ofCron("", "", "", "", "", ""));

        Assertions.assertDoesNotThrow(() -> CronUtil.everySecond(2));
        Assertions.assertDoesNotThrow(() -> CronUtil.everyMinute(2));
        Assertions.assertDoesNotThrow(() -> CronUtil.everyDay(2));

        Assertions.assertDoesNotThrow(() -> CronUtil.everySecond());
        Assertions.assertDoesNotThrow(() -> CronUtil.everyMinute());
        Assertions.assertDoesNotThrow(() -> CronUtil.everyDay());
        Assertions.assertDoesNotThrow(() -> CronUtil.builder()
                .temporal(LocalDateTime.now())
                .expression(CronUtil.ofCron("10"))
                .build().next()
        );

    }


}