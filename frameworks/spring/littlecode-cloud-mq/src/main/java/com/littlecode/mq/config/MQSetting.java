package com.littlecode.mq.config;

import com.littlecode.mq.adapter.Amqp;
import com.littlecode.util.EnvironmentUtil;
import lombok.Data;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

@Data
public class MQSetting {
    private final EnvironmentUtil environmentUtil;
    private String engine;
    private boolean autoCreate;
    private boolean autoStart;
    private boolean stopOnFail;
    private String hostName;
    private String vHostName;
    private int queueConsumers;
    private String queueChannel;
    private String queueExchange;
    private int port;
    private String url;
    private int heartbeat;
    private int recoveryInterval;
    private String clientId;
    private String clientSecret;
    private String queueRegion;
    private int queueMaxNumber;
    private int queueIdleSleep;
    private String queueName;
    private String queueNameConsumer;
    private String queueNameDispatcher;

    public MQSetting() {
        this.environmentUtil = new EnvironmentUtil();
        this.resetEnvs();
    }

    public MQSetting(Environment environment) {
        if (environment == null)
            throw new NullPointerException("environment is null");
        this.environmentUtil = new EnvironmentUtil(environment);
        this.resetEnvs();
    }

    public void resetEnvs() {
        var envUtil = new EnvironmentUtil();
        this.engine = envUtil.asString("littlecode.mq.engine", Amqp.class.getCanonicalName());
        this.autoCreate = envUtil.asBool("littlecode.mq.auto-create", false);
        this.autoStart = envUtil.asBool("littlecode.mq.auto-start", false);
        this.stopOnFail = envUtil.asBool("littlecode.mq.stop-on-fail", true);
        this.hostName = envUtil.asString("littlecode.mq.hostname");
        this.vHostName = envUtil.asString("littlecode.mq.v-hostname", "/");
        this.queueConsumers = envUtil.asInt("littlecode.mq.consumers", 1);
        this.queueChannel = envUtil.asString("littlecode.mq.channel");
        this.queueExchange = envUtil.asString("littlecode.mq.exchange");
        this.port = envUtil.asInt("littlecode.mq.port", 5672);
        this.url = envUtil.asString("littlecode.mq.url");
        this.heartbeat = envUtil.asInt("littlecode.mq.keep-alive", 30);
        this.recoveryInterval = envUtil.asInt("littlecode.mq.recovery-time", 10);
        this.clientId = envUtil.asString("littlecode.mq.client.id", "guest");
        this.clientSecret = envUtil.asString("littlecode.mq.client.secret", "guest");
        this.queueRegion = envUtil.asString("littlecode.mq.region:", "us-east-1");
        this.queueMaxNumber = envUtil.asInt("littlecode.mq.max-number", 1);
        this.queueIdleSleep = envUtil.asInt("littlecode.mq.idle-sleep", 1000);
        this.queueName = envUtil.asString("littlecode.mq.name");
        this.queueNameConsumer = envUtil.asString("littlecode.mq.name.consumer");
        this.queueNameDispatcher = envUtil.asString("littlecode.mq.name.dispatcher");
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


