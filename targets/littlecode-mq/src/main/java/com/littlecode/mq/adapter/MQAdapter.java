package com.littlecode.mq.adapter;

import com.littlecode.mq.MQ;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Slf4j
@Component
public class MQAdapter {
    private final MQ mq;

    public MQAdapter(MQ mq) {
        this.mq = mq;
    }

    public MQ mq() {
        return this.mq;
    }

    public MQ.Setting setting() {
        return this.mq.setting();
    }

    public MQ.Executor listenStart() {
        return null;
    }

    public MQ.Executor beanQueueDispatcherObject() {
        return null;
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Component
    public @interface Indicator {
    }
}
