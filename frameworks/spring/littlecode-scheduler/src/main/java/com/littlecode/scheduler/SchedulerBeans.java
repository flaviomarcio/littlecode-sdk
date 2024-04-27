package com.littlecode.scheduler;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class SchedulerBeans {
    private final SchedulerSetting schedulerSetting;

    @Bean
    public SchedulerSetting newSchedulerSetting(){
        return schedulerSetting;
    }

}