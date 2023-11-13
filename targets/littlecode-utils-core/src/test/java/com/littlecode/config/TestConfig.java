package com.littlecode.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {
    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ApplicationContext applicationContext() {
        return this.applicationContext;
    }

}
