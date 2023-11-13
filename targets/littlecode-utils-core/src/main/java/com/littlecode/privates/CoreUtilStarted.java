package com.littlecode.privates;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.scheduler.SchedulerConfig;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Configuration
public class CoreUtilStarted {
    private final ApplicationContext applicationContext;
    private final Environment environment;
    private final UtilCoreConfig CoreConfig;
    private final SchedulerConfig schedulerConfig;

    public CoreUtilStarted(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
        this.CoreConfig = new UtilCoreConfig(applicationContext, environment);
        this.schedulerConfig = new SchedulerConfig(applicationContext, environment);
    }

    @SuppressWarnings("unused")
    @Bean
    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @SuppressWarnings("unused")
    @Bean
    public Environment getEnvironment() {
        return this.environment;
    }

    @SuppressWarnings("unused")
    @Bean
    public UtilCoreConfig getCoreConfig() {
        return CoreConfig;
    }

    @SuppressWarnings("unused")
    @Bean
    public SchedulerConfig getSchedulerConfig() {
        return this.schedulerConfig;
    }
}
