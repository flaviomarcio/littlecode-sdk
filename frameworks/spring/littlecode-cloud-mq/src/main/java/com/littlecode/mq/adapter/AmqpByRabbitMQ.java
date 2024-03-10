package com.littlecode.mq.adapter;

import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.impl.MQAMQPRabbitMQImpl;

@MQAdapter.Indicator
public class AmqpByRabbitMQ extends MQAMQPRabbitMQImpl {
    public AmqpByRabbitMQ(MQ mq) {
        super(mq);
    }
}