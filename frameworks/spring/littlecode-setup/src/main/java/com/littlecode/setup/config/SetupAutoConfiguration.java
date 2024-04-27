package com.littlecode.setup.config;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.setup.Setup;
import com.littlecode.setup.SetupSetting;
import com.littlecode.setup.privates.SetupConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class SetupAutoConfiguration {
    public SetupAutoConfiguration(ApplicationContext applicationContext, Environment environment) {
        UtilCoreConfig.setApplicationContext(applicationContext);
        UtilCoreConfig.setEnvironment(environment);
        final String logPrefix = this.getClass().getName() + ": ";
        var setupConfig=new SetupConfig(applicationContext, environment);
        try {
            log.debug("{}: started", logPrefix);
            var setup = new Setup(new SetupSetting(setupConfig));
            setup.execute();
        } catch (Exception e) {
            log.error("{}: fail:{}", logPrefix, e.getMessage());
        } finally {
            log.debug("{}: finished", logPrefix);
        }
    }
}
