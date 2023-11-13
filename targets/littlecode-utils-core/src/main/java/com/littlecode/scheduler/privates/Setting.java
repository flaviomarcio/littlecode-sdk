package com.littlecode.scheduler.privates;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Getter
public class Setting {
    private static final String env_log = "littlecode.scheduler.log";
    private static final String env_auto_start = "littlecode.scheduler.auto-start";
    private final ApplicationContext applicationContext;
    private final Environment environment;

    private boolean log;
    private boolean autoStart;

    public Setting(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
        this.load();
    }

    public String readEnv(String env, String defaultValue) {
        try {
            return environment.getProperty(env, defaultValue);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private void load() {
        this.log = Boolean.parseBoolean(readEnv(env_log, "false"));
        this.autoStart = Boolean.parseBoolean(readEnv(env_auto_start, "true"));
    }
}
