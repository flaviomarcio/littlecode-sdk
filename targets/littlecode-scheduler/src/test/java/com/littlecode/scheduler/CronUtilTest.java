package com.littlecode.scheduler;

import com.littlecode.scheduler.privates.CronUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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

    }

}
