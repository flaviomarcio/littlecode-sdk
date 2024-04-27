package com.littlecode.scheduler;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableAutoConfiguration
public class SchedulerBeans {
    private SchedulerSetting schedulerSetting;

    @Bean
    public SchedulerSetting newSchedulerSetting(){
        return schedulerSetting;
    }

}