package com.littlecode.scheduler.privates;

import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Getter
@Configuration
public class Started {
    private final ApplicationContext applicationContext;
    private final Environment environment;
    private final Engine engine;

    public Started(ApplicationContext applicationContext, Environment environment) {
        this.applicationContext = applicationContext;
        this.environment = environment;
        this.engine = new Engine(new Factory(new Setting(applicationContext, environment)));
        if (this.engine.setting().isAutoStart())
            this.engine.start();
    }

}
