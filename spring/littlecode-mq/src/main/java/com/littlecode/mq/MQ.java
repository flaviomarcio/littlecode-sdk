package com.littlecode.mq;

import com.littlecode.containers.ObjectContainer;
import com.littlecode.mq.adapter.MQAdapter;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.util.BeanUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class MQ {
    public static final String MQ_BEAN_NAME_DISPATCHER = "littleCodeMqBeanNameDispatcher";
    public static final String MQ_BEAN_NAME_CONSUMER = "littleCodeMqBeanNameConsumer";
    public static final String MQ_BEAN_RECEIVER = "littleCodeMqBeanReceiver";
    public static final String MQ_BEAN_CONFIGURER = "littleCodeMqBeanConfigurer";
    private final Setting setting;
    private MQAdapter adapter = null;

    public MQ() {
        this.setting = new Setting();
    }

    public MQ(Setting setting) {
        this.setting = setting;
    }

    public MQ(ApplicationContext applicationContext, Environment environment) {
        this.setting = new Setting(applicationContext, environment);
    }

    public Setting setting() {
        return setting.load();
    }

    public MQ queueName(String queueName) {
        this.setting().setQueueName(queueName);
        return this;
    }

    private MQAdapter adapterCreate() {
        if (adapter != null)
            return this.adapter;

        var configurer = BeanUtil
                .of(setting.getContext())
                .bean(MQ.MQ_BEAN_CONFIGURER)
                .getBean(MQ.Configurer.class);
        if (configurer != null)
            configurer.configurer.accept(this.setting);

        var engine = setting.getEngine();
        if (PrimitiveUtil.isEmpty(engine))
            throw ExceptionBuilder.ofNotFound(String.format("Invalid [%s] name", MQAdapter.class.getName()));
        var aClass = ObjectUtil.getClassByName(engine);
        this.adapter = ObjectUtil.createWithArgsConstructor(aClass, this);
        if (adapter == null)
            throw ExceptionBuilder.ofNotFound(String.format("Invalid [%s] name: [%s]", MQAdapter.class.getName(), engine));

        return adapter;
    }

    public Message.Response dispatcher(MQ.Message.Task task) {
        var adapter = adapterCreate();
        Executor executor = adapter.beanQueueDispatcherObject();
        if (executor == null)
            throw ExceptionBuilder.ofNoImplemented(String.format("Invalid listenStart method by %s", adapter.getClass().getName()));
        return executor.getDispatcherObject().accept(task);
    }

    @SuppressWarnings("unused")
    public Message.Response dispatcher(Object task) {
        return this.dispatcher(MQ.Message.Task.of(task));
    }

    @SuppressWarnings("unused")
    public Message.Response dispatcher(String message) {
        return this.dispatcher(Message.Task.of(null, message));
    }

    public void listen() {
        var adapter = this.adapterCreate();
        Executor executor = adapter.listenStart();
        if (executor == null)
            throw ExceptionBuilder.ofNoImplemented(String.format("Invalid listenStart method by %s", adapter.getClass().getName()));
        executor.getListen().accept();
    }


    @FunctionalInterface
    public interface Method {
        void accept();
    }

    @FunctionalInterface
    public interface MethodArg<T> {
        void accept(T var1);
    }

    @FunctionalInterface
    public interface MethodReturn<OUT, IN> {
        OUT accept(IN var1);
    }

    @Builder
    @Component
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Configurer {
        private MethodArg<Setting> configurer;
    }

    @Builder
    @Component
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Executor {
        private Method listen;
        private MethodArg<Message.Task> received;
        private MethodReturn<Message.Response, MQ.Message.Task> dispatcherObject;
        private MethodReturn<Message.Response, String> dispatcherString;
    }

    public static class Setting {
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
        private static ApplicationContext STATIC_APPLICATION_CONTEXT;
        private static Environment STATIC_ENVIRONMENT;
        private boolean loaded = false;
        private ApplicationContext context;
        private Environment environment;
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


        public Setting() {
            configure(STATIC_APPLICATION_CONTEXT, STATIC_ENVIRONMENT);
        }

        public Setting(ApplicationContext context, Environment environment) {
            configure(context, environment);
        }

        public ApplicationContext getContext() {
            if (this.context == null)
                return STATIC_APPLICATION_CONTEXT;
            return this.context;
        }

        public Environment getEnvironment() {
            if (this.environment == null)
                return STATIC_ENVIRONMENT;
            return this.environment;
        }

        private void configure(ApplicationContext context, Environment environment) {
            this.context = context;
            this.environment = environment;
            if (Setting.STATIC_APPLICATION_CONTEXT == null)
                Setting.STATIC_APPLICATION_CONTEXT = context;
            if (Setting.STATIC_ENVIRONMENT == null)
                Setting.STATIC_ENVIRONMENT = environment;
            this.load();
        }


        public String readEnv(String env, String defaultValue) {
            try {
                return getEnvironment().getProperty(env, defaultValue);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        public List<String> readEnvList(String env) {
            try {
                var values = List.of(getEnvironment().getProperty(env, "").split(","));
                List<String> out = new ArrayList<>();
                values.forEach(s -> {
                    if (s != null && !s.trim().isEmpty())
                        out.add(s);
                });
                return out;
            } catch (Exception e) {
                return new ArrayList<>();
            }
        }

        public String readEnv(String env) {
            return this.readEnv(env, "");
        }

        private Setting load() {
            if (loaded)
                return this;
            this.engine = readEnv(env_mq_engine, "amqp");
            this.autoCreate = PrimitiveUtil.toBool(readEnv(env_mq_auto_create, "false"));
            this.autoStart = PrimitiveUtil.toBool(readEnv(env_mq_auto_start, "false"));
            this.stopOnFail = PrimitiveUtil.toBool(readEnv(env_mq_stop_on_fail, "true"));
            this.hostName = readEnv(env_mq_hostname);
            this.vHostName = readEnv(env_mq_v_hostname);
            this.port = PrimitiveUtil.toInt(readEnv(env_mq_port));
            this.url = readEnv(env_mq_url);
            this.heartbeat = PrimitiveUtil.toInt(getEnvironment().getProperty(env_mq_idle_keep_alive, "30"));
            this.recoveryInterval = PrimitiveUtil.toInt(getEnvironment().getProperty(env_mq_recovery_interval, "10"));
            this.clientId = readEnv(env_client_id);
            this.clientSecret = readEnv(env_client_secret);
            this.queueConsumers = PrimitiveUtil.toInt(readEnv(env_mq_consumers));
            this.queueChannel = readEnv(env_mq_channel);
            this.queueExchange = readEnv(env_mq_exchange);
            this.queueRegion = readEnv(env_mq_region);
            this.queueName = readEnvList(env_mq_name);
            this.queueNameConsumer = readEnvList(env_mq_name_consumer);
            this.queueNameDispatcher = readEnvList(env_mq_name_dispatcher);
            this.queueMaxNumber = PrimitiveUtil.toInt(getEnvironment().getProperty(env_mq_max_number, "1"));
            this.queueIdleSleep = PrimitiveUtil.toInt(getEnvironment().getProperty(env_mq_idle_sleep, "1000"));
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

    public static class Message {
        @Builder
        @Getter
        @Setter
        @AllArgsConstructor
        public static class Task {
            private Object type;
            private String messageId;
            private UUID checksum;
            private Object body;
            private String fail;

            public static Task of(Object type, Object body) {
                var outType = ObjectUtil.classToName(type);
                return Task
                        .builder()
                        .type(outType)
                        .body(body)
                        .build();
            }

            public static Task of(Object taskObject) {
                var type = taskObject.getClass().equals(String.class)
                        ? null
                        : taskObject.getClass();
                return of(type, taskObject);
            }

            public static Task from(String body) {
                var values = ObjectUtil.toMapObject(body);
                Task task = ObjectUtil.createFromValues(Task.class, values);
                if (task != null)
                    return task;
                return Task.of(body);
            }

            public boolean canType(Object type) {
                if (type == null || this.getType() == null)
                    return false;

                var eA = ObjectUtil.classToName(type);
                var eB = ObjectUtil.classToName(getType());
                if (eA.equals(eB))
                    return true;

                var of = ObjectContainer.classDictionaryByName(eA);
                //noinspection RedundantIfStatement
                if (of != null)
                    return true;
                return false;
            }

            public Object getType() {
                return this.type == null ? "" : this.type;
            }

            @Transient
            public String getTypeName() {
                return ObjectContainer.classToName(this.type);
            }

            @Transient
            public Class<?> getTypeClass() {
                return ObjectContainer.classByName(this.type);
            }

            @Transient
            public String asString() {
                return ObjectUtil.toString(this.body);
            }

            @Transient
            public ObjectContainer asContainer() {
                var msgId = HashUtil.isUuid(messageId)
                        ? HashUtil.toUuid(messageId)
                        : UUID.randomUUID();
                return ObjectContainer.from(msgId, type, body);
            }

            @Transient
            public <T> T asObject(Class<T> valueType) {
                return ObjectUtil.createFromString(valueType, this.asString());
            }

            @Transient
            public <T> T asObject(Object valueType) {
                Class<?> aClass = ObjectContainer.classByName(valueType);
                if (aClass == null)
                    throw ExceptionBuilder.ofFrameWork("Class not found");
                var o = ObjectUtil.createFromString(aClass, this.asString());
                //noinspection unchecked
                return (T) o;
            }
        }

        @Builder
        @Getter
        public static class Response implements Serializable {
            private String messageId;
            private LocalDateTime dt;
            @Setter
            private UUID checksum;
        }
    }

}