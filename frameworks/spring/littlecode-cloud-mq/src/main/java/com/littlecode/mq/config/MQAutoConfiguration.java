package com.littlecode.mq.config;

import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.MQAdapter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Getter
@Configuration
@EnableAutoConfiguration(exclude = RabbitAutoConfiguration.class)
public class MQAutoConfiguration {
    private final MQ mq;

    @Bean
    @ConditionalOnMissingBean
    public MQ createMQ(){
        return new MQ();
    }

    public MQAutoConfiguration(ApplicationContext applicationContext, Environment environment) {
        this.checkAdapters();
        this.mq = new MQ(applicationContext, environment);
        if (this.mq.setting().isAutoStart())
            this.mq.listen();
    }

    private void checkAdapters() {
        var reflections = new Reflections(MQAdapter.class.getPackageName());
        var scanClasses = reflections.getTypesAnnotatedWith(MQAdapter.Indicator.class);
        var adapters = new StringBuilder();
        scanClasses
                .forEach(aClass -> {
                    if (!adapters.isEmpty())
                        adapters.append(", ");
                    adapters.append(aClass.getName());
                });
        if (adapters.isEmpty())
            log.debug("MQ.Adapters: no adapters found");
        else
            log.debug("MQ.Adapters: " + adapters);
    }

}
