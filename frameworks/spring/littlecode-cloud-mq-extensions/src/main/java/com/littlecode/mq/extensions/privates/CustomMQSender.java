package com.littlecode.mq.extensions.privates;

import com.littlecode.mq.MQ;
import com.littlecode.mq.config.MQSetting;

import com.littlecode.parsers.ExceptionBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomMQSender<T>{
    private final String propertiesPrefix;
    private final MQ mq;

    public CustomMQSender(MQ mq){
        this.propertiesPrefix=null;
        this.mq=mq;
    }

    public CustomMQSender(String propertiesPrefix, MQ mq){
        this.propertiesPrefix=propertiesPrefix.trim();
        this.mq=mq;
    }

    public boolean send(Object payload){
        this.mq.dispatcher(payload);
        return true;
    }

    public MQSetting setting(){
        return this.mq.getSetting();
    }

    public CustomMQSender<T> queue(String queue){
        if(queue ==null)
            throw ExceptionBuilder.ofNullPointer("queue is null");
        if(queue.trim().isEmpty())
            throw ExceptionBuilder.ofInvalid("Invalid queue");
        this.mq.queueName(queue);
        return this;
    }
}