package com.littlecode.mq.adapter;

import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.impl.MQAMQPRabbitMQImpl;

@MQAdapter.Indicator
public class Amqp extends MQAMQPRabbitMQImpl {
    public Amqp(MQ mq) {
        super(mq);
    }
}
