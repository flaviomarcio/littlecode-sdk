package com.littlecode.mq.started;

import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.MQAdapter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;


@Slf4j
@Getter
@Service
public class MQStarted {
    private final MQ mq;

    public MQStarted(ApplicationContext applicationContext, Environment environment) {
        this.checkAdapters();
        this.mq = new MQ(new MQ.Factory(new MQ.Setting(applicationContext, environment)));
        if (this.mq().setting().isAutoStart())
            this.mq().listen();
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

    @Bean(name = MQ.MQ_BEAN_NAME_INSTANCE)
    public MQ mq() {
        return this.mq;
    }

}
