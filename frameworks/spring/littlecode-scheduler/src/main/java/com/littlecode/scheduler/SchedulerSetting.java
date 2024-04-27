package com.littlecode.scheduler;

import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableAutoConfiguration
public class SchedulerSetting {
    @Value("${littlecode.scheduler.log:false}")
    private boolean log;
    @Value("${littlecode.scheduler.auto-start:false}")
    private boolean autoStart;
}