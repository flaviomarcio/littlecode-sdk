package com.littlecode.scheduler;

import com.littlecode.config.UtilCoreConfig;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Data
@Configuration
public class SchedulerAutoConfiguration {
    @Value("${littlecode.scheduler.auto-start:false}")
    private boolean autoStart;
    public SchedulerAutoConfiguration(ApplicationContext applicationContext, Environment environment) {
        UtilCoreConfig.setApplicationContext(applicationContext);
        UtilCoreConfig.setEnvironment(environment);
        this.start();
    }

    public void start() {
        String logPrefix = this.getClass().getName() + ": ";
        log.info("{}: started", logPrefix);

        try {
            if (!this.autoStart) {
                log.info("{}: skipped", logPrefix);
            } else {
                log.info("{}: executing", logPrefix);
                SchedulerRunner.start();
                log.info("{}: executed", logPrefix);
            }
        } finally {
            log.info("{}: finished", logPrefix);
        }

    }
}
