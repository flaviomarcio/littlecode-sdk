package com.littlecode.setup.privates;

import com.littlecode.setup.Setup;
import com.littlecode.setup.SetupSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@Slf4j
public class SetupStarted {
    private final ApplicationContext applicationContext;
    private final Environment environment;
    private SetupConfig config;
    private SetupSetting setting;
    private Setup setup;

    public SetupStarted(ApplicationContext applicationContext, Environment environment) {
        final String logPrefix = this.getClass().getName() + ": ";
        this.applicationContext = applicationContext;
        this.environment = environment;
        log.debug("{}: started", logPrefix);
        try {
            this.config = new SetupConfig(applicationContext, environment);
            this.setting = new SetupSetting(this.config);
            this.setup = new Setup(setting);
            setup.execute();
        } catch (Exception e) {
            log.error("{}: fail:{}", logPrefix, e.getMessage());
        } finally {
            log.debug("{}: finished", logPrefix);
        }
    }

    @SuppressWarnings("unused")
    @Bean(name = Setup.STP_CONFIGURE_BEAN_CONTEXT)
    public ApplicationContext getApplicationContext() {
        return this.applicationContext;
    }

    @SuppressWarnings("unused")
    @Bean(name = Setup.STP_CONFIGURE_BEAN_ENVIRONMENT)
    public Environment getEnvironment() {
        return this.environment;
    }

    @SuppressWarnings("unused")
    @Bean(name = Setup.STP_CONFIGURE_BEAN_CONFIG)
    public SetupConfig getConfig() {
        return this.config;
    }

    @SuppressWarnings("unused")
    @Bean(name = Setup.STP_CONFIGURE_BEAN_SETTING)
    public SetupSetting getSetting() {
        return this.setting;
    }

    @SuppressWarnings("unused")
    @Bean(name = Setup.STP_CONFIGURE_BEAN_SETUP)
    public Setup getSetup() {
        return this.setup;
    }

}
