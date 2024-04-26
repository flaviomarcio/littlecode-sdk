package com.littlecode.mq.config;

import com.littlecode.containers.ObjectContainer;
import com.littlecode.mq.MQ;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.util.EnvironmentUtil;
import lombok.*;
import org.springframework.context.annotation.Configuration;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Configuration
public class MQSetting {
    private static final String env_exception_on_fail = "littlecode.mq.exception.on-fail";
    private static final String env_mq_engine = "littlecode.mq.engine";
    private static final String env_mq_auto_create = "littlecode.mq.auto-create";
    private static final String env_mq_auto_start = "littlecode.mq.auto-start";
    private static final String env_mq_stop_on_fail = "littlecode.mq.stop-on-fail";
    private static final String env_mq_hostname = "littlecode.mq.hostname";
    private static final String env_mq_v_hostname = "littlecode.mq.v-hostname";
    private static final String env_mq_consumers = "littlecode.mq.consumers";
    private static final String env_mq_channel = "littlecode.mq.channel";
    private static final String env_mq_exchange = "littlecode.mq.exchange";
    private static final String env_mq_port = "littlecode.mq.port";
    private static final String env_mq_url = "littlecode.mq.url";
    private static final String env_mq_region = "littlecode.mq.region";
    private static final String env_mq_name = "littlecode.mq.name";
    private static final String env_mq_name_consumer = "littlecode.mq.name.consumer";
    private static final String env_mq_name_dispatcher = "littlecode.mq.name.dispatcher";
    private static final String env_mq_recovery_interval = "littlecode.mq.recovery-time";
    private static final String env_mq_max_number = "littlecode.mq.max-number";
    private static final String env_mq_idle_sleep = "littlecode.mq.idle-sleep";
    private static final String env_mq_idle_keep_alive = "littlecode.mq.keep-alive";
    private static final String env_client_id = "littlecode.mq.client.id";
    private static final String env_client_secret = "littlecode.mq.client.secret";
    private boolean loaded = false;
    @Getter
    @Setter
    private boolean exceptionOnFail;
    @Getter
    @Setter
    private String engine;
    @Getter
    @Setter
    private boolean autoCreate;
    @Getter
    @Setter
    private boolean autoStart;
    @Getter
    @Setter
    private boolean stopOnFail;
    @Getter
    @Setter
    private String hostName;
    @Getter
    @Setter
    private String vHostName;
    @Setter
    private int recoveryInterval;
    @Setter
    private int queueConsumers;
    @Getter
    @Setter
    private String queueChannel;
    @Getter
    @Setter
    private String queueExchange;
    @Getter
    @Setter
    private int port;
    @Getter
    @Setter
    private String url;
    @Setter
    private int heartbeat;
    @Getter
    @Setter
    private String clientId;
    @Getter
    @Setter
    private String clientSecret;
    @Getter
    private List<String> queueName;
    @Setter
    private List<String> queueNameConsumer;
    @Getter
    @Setter
    private List<String> queueNameDispatcher;
    @Getter
    @Setter
    private String queueRegion;
    @Setter
    private int queueMaxNumber;
    @Setter
    private int queueIdleSleep;

    public MQSetting(){
        this.load();
    }

    private MQSetting load() {
        if (loaded)
            return this;
        var envUtil=new EnvironmentUtil();
        this.exceptionOnFail = envUtil.asBool(env_mq_engine, true);
        this.engine = envUtil.asString(env_mq_engine, "amqp");
        this.autoCreate = envUtil.asBool(env_mq_auto_create, false);
        this.autoStart = envUtil.asBool(env_mq_auto_start, false);
        this.stopOnFail = envUtil.asBool(env_mq_stop_on_fail, true);
        this.hostName = envUtil.asString(env_mq_hostname);
        this.vHostName = envUtil.asString(env_mq_v_hostname,"/");
        this.port = envUtil.asInt(env_mq_port);
        this.url = envUtil.asString(env_mq_url);
        this.heartbeat = envUtil.asInt(env_mq_idle_keep_alive, 30);
        this.recoveryInterval = envUtil.asInt(env_mq_recovery_interval, 10);
        this.clientId = envUtil.asString(env_client_id);
        this.clientSecret = envUtil.asString(env_client_secret);
        this.queueConsumers = envUtil.asInt(env_mq_consumers);
        this.queueChannel = envUtil.asString(env_mq_channel);
        this.queueExchange = envUtil.asString(env_mq_exchange);
        this.queueRegion = envUtil.asString(env_mq_region);
        this.queueName = envUtil.asListOfString(env_mq_name);
        this.queueNameConsumer = envUtil.asListOfString(env_mq_name_consumer);
        this.queueNameDispatcher = envUtil.asListOfString(env_mq_name_dispatcher);
        this.queueMaxNumber = envUtil.asInt(env_mq_max_number, 1);
        this.queueIdleSleep = envUtil.asInt(env_mq_idle_sleep, 1000);
        this.loaded = true;
        return this;
    }

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

    @SuppressWarnings("unused")
    public void setQueueName(final List<String> queueName) {
        this.queueName = queueName;
    }

    public void setQueueName(final String queueName) {
        if (PrimitiveUtil.isEmpty(queueName))
            this.queueName.clear();
        else
            this.queueName = List.of(queueName);
    }

    public List<String> getQueueNameConsumer() {
        if (!PrimitiveUtil.isEmpty(this.queueName))
            return this.queueName;
        if (!PrimitiveUtil.isEmpty(this.queueNameConsumer))
            return this.queueNameConsumer;
        return new ArrayList<>();
    }

    public List<String> getQueueNameDispatchers() {
        if (!PrimitiveUtil.isEmpty(this.queueName))
            return this.queueName;
        if (!PrimitiveUtil.isEmpty(this.queueNameDispatcher))
            return this.queueNameDispatcher;
        return new ArrayList<>();
    }

    public int getQueueConsumers() {
        if (queueConsumers <= 0)
            return queueConsumers = 1;
        return queueConsumers;
    }

    public int getQueueMaxNumber() {
        if (queueMaxNumber <= 0)
            return queueMaxNumber = 10;
        return queueMaxNumber;
    }

    public int getQueueIdleSleep() {
        if (queueIdleSleep <= 0)
            return 1000;
        return queueIdleSleep;
    }
}


