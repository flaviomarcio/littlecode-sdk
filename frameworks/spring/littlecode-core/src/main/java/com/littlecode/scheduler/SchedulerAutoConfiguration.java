package com.littlecode.scheduler;

import com.littlecode.config.UtilCoreConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class SchedulerAutoConfiguration {
    @Value("${littlecode.scheduler.auto-start:false}")
    private boolean autoStart;
    public SchedulerAutoConfiguration(ApplicationContext applicationContext, Environment environment) {
        UtilCoreConfig.setApplicationContext(applicationContext);
        UtilCoreConfig.setEnvironment(environment);
        if (autoStart)
            SchedulerRunner.start();
    }
}
