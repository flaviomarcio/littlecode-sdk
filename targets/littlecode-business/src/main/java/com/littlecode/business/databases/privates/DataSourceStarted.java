package com.littlecode.business.databases.privates;

import com.littlecode.config.UtilCoreConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@SuppressWarnings("unused")
public class DataSourceStarted {

    @Autowired
    private UtilCoreConfig utilCoreConfig;

    @Autowired
    private Environment environment;

    @Bean
    public Environment getEnvironment(){
        return environment;
    }

    @Bean
    public UtilCoreConfig utilCoreConfig(){
        return this.utilCoreConfig;
    }

    @Bean
    public DataSourceStarted dataSourceStarted(){
        return this;
    }
}
