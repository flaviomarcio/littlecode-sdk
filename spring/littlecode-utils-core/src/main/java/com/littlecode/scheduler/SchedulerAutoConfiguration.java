package com.littlecode.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SchedulerAutoConfiguration {
    public SchedulerAutoConfiguration() {
        try {
            var setting = new SchedulerSetting();
            if (setting.isAutoStart())
                SchedulerRunner.start();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
