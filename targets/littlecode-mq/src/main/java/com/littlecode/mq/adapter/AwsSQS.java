package com.littlecode.mq.adapter;

import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.impl.MQSQSAWSImpl;

@MQAdapter.Indicator
public class AwsSQS extends MQSQSAWSImpl {
    public AwsSQS(MQ mq) {
        super(mq);
    }
}

