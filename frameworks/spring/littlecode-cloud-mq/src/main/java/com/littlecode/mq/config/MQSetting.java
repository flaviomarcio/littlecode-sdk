package com.littlecode.mq.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Configuration
@EnableAutoConfiguration
public class MQSetting {
    @Value("${littlecode.mq.engine:amqp}")
    private String engine;

    @Value("${littlecode.mq.auto-create:false}")
    private boolean autoCreate;

    @Value("${littlecode.mq.auto-start:false}")
    private boolean autoStart;

    @Value("${littlecode.mq.stop-on-fail:true}")
    private boolean stopOnFail;

    @Value("${littlecode.mq.hostname:}")
    private String hostName;

    @Value("${littlecode.mq.v-hostname:/}")
    private String vHostName;

    @Value("${littlecode.mq.consumers:1}")
    private int queueConsumers;

    @Value("${littlecode.mq.channel:}")
    private String queueChannel;

    @Value("${littlecode.mq.exchange:}")
    private String queueExchange;

    @Value("${littlecode.mq.port:}")
    private int port;

    @Value("${littlecode.mq.url:}")
    private String url;

    @Value("${littlecode.mq.keep-alive:30}")
    private int heartbeat;

    @Value("${littlecode.mq.recovery-time:10}")
    private int recoveryInterval;

    @Value("${littlecode.mq.client.id:}")
    private String clientId;

    @Value("${littlecode.mq.client.secret:}")
    private String clientSecret;

    @Value("${littlecode.mq.region:us-east-1}")
    private String queueRegion;

    @Value("${littlecode.mq.max-number:1}")
    private int queueMaxNumber;

    @Value("${littlecode.mq.idle-sleep:1000}")
    private int queueIdleSleep;

    @Value("${littlecode.mq.name:}")
    private String queueName;

    @Value("${littlecode.mq.name.consumer:}")
    private String queueNameConsumer;

    @Value("${littlecode.mq.name.dispatcher:}")
    private String queueNameDispatcher;

    public int getHeartbeat() {
        if (this.heartbeat <= 0)
            return this.heartbeat = 30;
        return this.heartbeat;
    }

    public int getRecoveryInterval() {
        if (this.recoveryInterval <= 0)
            return this.recoveryInterval = 60;
        return this.recoveryInterval;
    }

    public int getQueueConsumers() {
        if (this.queueConsumers <= 0)
            return this.queueConsumers = 1;
        return this.queueConsumers;
    }

    public int getQueueMaxNumber() {
        if (this.queueMaxNumber <= 0)
            return this.queueMaxNumber = 1;
        return this.queueMaxNumber;
    }

    public int getQueueIdleSleep() {
        if(this.queueIdleSleep<=0)
            return this.queueIdleSleep=1000;
        return this.queueIdleSleep;
    }


    public List<String> getQueueNameConsumers() {
        if (this.queueName!=null && !this.queueName.trim().isEmpty())
            return List.of(this.queueName.split(","));
        if (this.queueNameConsumer!=null && !this.queueNameConsumer.trim().isEmpty())
            return List.of(this.queueNameConsumer.split(","));
        return new ArrayList<>();
    }

    public List<String> getQueueNameDispatchers() {
        if (this.queueName!=null && !this.queueName.trim().isEmpty())
            return List.of(this.queueName.split(","));
        if (this.queueNameDispatcher!=null && !this.queueNameDispatcher.trim().isEmpty())
            return List.of(this.queueNameDispatcher.split(","));
        return new ArrayList<>();
    }


}


