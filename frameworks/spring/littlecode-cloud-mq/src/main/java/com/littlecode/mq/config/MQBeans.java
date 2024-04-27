package com.littlecode.mq.config;

import com.littlecode.mq.MQ;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@EnableAutoConfiguration
public class MQBeans {
    private MQSetting mqSetting;

    @Bean
    public MQ newMQ(){
        return new MQ(mqSetting);
    }
}
