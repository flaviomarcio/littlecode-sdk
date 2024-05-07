package com.littlecode.mq;

import com.littlecode.mq.config.MQAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Bean(name = MQ.MQ_BEAN_CONFIGURER)
@Configuration
@Import(MQAutoConfiguration.class)
public @interface MQConsumer {
}
