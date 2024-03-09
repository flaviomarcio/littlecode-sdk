package com.littlecode.privates;

import com.littlecode.config.UtilCoreConfig;
import lombok.Getter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


@Getter
@Configuration
public class CoreUtilAutoConfiguration {
    private static ApplicationContext STATIC_CONTEXT;
    private static Environment STATIC_ENVIROMENT;

    private final ApplicationContext applicationContext;
    private final Environment environment;
    private final UtilCoreConfig coreConfig;

    public CoreUtilAutoConfiguration(ApplicationContext applicationContext, Environment environment) {
        if (STATIC_CONTEXT == null)
            STATIC_CONTEXT = applicationContext;
        if (STATIC_ENVIROMENT == null)
            STATIC_ENVIROMENT = environment;

        this.applicationContext = applicationContext;
        this.environment = environment;
        this.coreConfig = new UtilCoreConfig(applicationContext, environment);
    }


    @Bean
    public UtilCoreConfig getCoreConfig() {
        return coreConfig;
    }
}
