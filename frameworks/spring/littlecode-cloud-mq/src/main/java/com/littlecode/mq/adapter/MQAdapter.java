package com.littlecode.mq.adapter;

import com.littlecode.mq.MQ;
import com.littlecode.mq.config.MQSetting;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Slf4j
@Component
public class MQAdapter {
    private final MQ mq;

    public MQAdapter(MQ mq) {
        if(mq==null)
            throw new NullPointerException("MQ is null");
        this.mq = mq;
    }

    public final MQ mq() {
        return this.mq;
    }

    public final MQSetting setting() {
        return this.mq.getSetting();
    }

    public MQ.Executor listenStart() {
        return MQ.Executor
                .builder()
                .listen(new MQ.Method() {
                    @Override
                    public void accept() {

                    }
                })
                .dispatcherString(new MQ.MethodReturn<MQ.Message.Response, String>() {
                    @Override
                    public MQ.Message.Response accept(String var1) {
                        return MQ.Message.Response.builder().build();
                    }
                })
                .received(new MQ.MethodArg<MQ.Message.Task>() {
                    @Override
                    public void accept(MQ.Message.Task var1) {

                    }
                })
                .dispatcherObject(new MQ.MethodReturn<MQ.Message.Response, MQ.Message.Task>() {
                    @Override
                    public MQ.Message.Response accept(MQ.Message.Task var1) {
                        return MQ.Message.Response.builder().build();
                    }
                })
                .build();
    }

    public MQ.Executor beanQueueDispatcherObject()
    {
        return listenStart();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @Component
    public @interface Indicator {
    }
}
