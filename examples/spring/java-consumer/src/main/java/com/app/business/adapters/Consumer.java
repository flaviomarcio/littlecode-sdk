package com.app.business.adapters;

import com.app.business.service.ConsumerService;
import com.littlecode.mq.MQ;
import com.littlecode.mq.MQConsumer;
import com.littlecode.mq.MQReceiveMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@MQConsumer
public class Consumer {
    private final ConsumerService service;

    @MQReceiveMethod
    public MQ.Executor execute() {
        return MQ.Executor
                .builder()
                .received(service::register)
                .build();
    }
}
