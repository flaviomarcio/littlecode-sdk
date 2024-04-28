package com.littlecode.mq.config;

import com.littlecode.mq.MQ;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MQBeans {

    @Bean
    public MQ newMQ(){
        return new MQ();
    }

    @Bean
    public MQSetting newMQSetting() {
        return new MQSetting();
    }
}
