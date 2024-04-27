package com.littlecode.setup.config;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.setup.Setup;
import com.littlecode.setup.SetupSetting;
import com.littlecode.setup.privates.SetupConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class SetupAutoConfiguration {
    @Value("${littlecode.setup.auto-start:false}")
    private boolean autoStart;
    public SetupAutoConfiguration(ApplicationContext applicationContext, Environment environment) {
        UtilCoreConfig.setApplicationContext(applicationContext);
        UtilCoreConfig.setEnvironment(environment);
        final String logPrefix = this.getClass().getName() + ": ";
        log.info("{}: started", logPrefix);
        try {
            if(autoStart)
                log.info("{}: skipped", logPrefix);
            else{
                log.info("{}: executing", logPrefix);
                var setupConfig=new SetupConfig(applicationContext, environment);
                var setup = new Setup(new SetupSetting(setupConfig));
                setup.execute();
                log.info("{}: executed", logPrefix);
            }
        } catch (Exception e) {
            log.error("{}: fail:{}", logPrefix, e.getMessage());
        } finally {
            log.info("{}: finished", logPrefix);
        }
    }
}
