package com.littlecode.mq.config;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.MQAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Data
public class MQAutoConfiguration {
    private final MQ mq;
    @Value("${littlecode.mq.auto-start:false}")
    private boolean autoStart;

    public MQAutoConfiguration(MQ mq, ApplicationContext applicationContext, Environment environment) {
        UtilCoreConfig.setApplicationContext(applicationContext);
        UtilCoreConfig.setEnvironment(environment);
        if(mq==null)
            throw new NullPointerException("Invalid "+MQ.class.getCanonicalName());
        this.mq = mq;
        this.start();
    }

    public void start(){
        if(autoStart)
            mq.listen();
    }
}
