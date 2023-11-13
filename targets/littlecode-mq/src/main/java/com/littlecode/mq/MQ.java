package com.littlecode.mq;

import com.littlecode.containers.ObjectContainer;
import com.littlecode.mq.adapter.MQAdapter;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class MQ {
    public static final String MQ_BEAN_NAME_INSTANCE = "littleCodeMqBeanNameInstance";
    public static final String MQ_BEAN_NAME_DISPATCHER = "littleCodeMqBeanNameDispatcher";
    public static final String MQ_BEAN_NAME_CONSUMER = "littleCodeMqBeanNameConsumer";
    public static final String MQ_BEAN_RECEIVER = "littleCodeMqBeanReceiver";

    private final Factory factory;
    private MQAdapter adapter = null;


    public Setting setting() {
        return this.factory.setting();
    }

    private MQAdapter adapterCreate() {
        if(adapter!=null)
            return this.adapter;
        var engine = factory.setting().getEngine();
        if (PrimitiveUtil.isEmpty(engine))
            throw ExceptionBuilder.ofNotFound(String.format("Invalid [%s] name", MQAdapter.class.getName()));
        var aClass = ObjectUtil.classByName(engine);
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
    public static class Executor {

        private Method listen;
        private MethodArg<Message.Task> received;
        private MethodReturn<Message.Response, MQ.Message.Task> dispatcherObject;
        private MethodReturn<Message.Response, String> dispatcherString;
    }

    @Configuration
    @Getter
    public static class Setting {
        private static final String env_mq_engine = "littlecode.mq.engine";
        private static final String env_mq_auto_create = "littlecode.mq.auto-create";
        private static final String env_mq_auto_start = "littlecode.mq.auto-start";
        private static final String env_mq_stop_on_fail = "littlecode.mq.stop-on-fail";
        private static final String env_mq_hostname = "littlecode.mq.hostname";
        private static final String env_mq_v_hostname = "littlecode.mq.v-hostname";
        private static final String env_mq_channel = "littlecode.mq.channel";
        private static final String env_mq_exchange = "littlecode.mq.exchange";
        private static final String env_mq_port = "littlecode.mq.port";
        private static final String env_mq_url = "littlecode.mq.url";
        private static final String env_mq_region = "littlecode.mq.region";
        private static final String env_mq_name = "littlecode.mq.name";
        private static final String env_mq_name_consumer = "littlecode.mq.name.consumer";
        private static final String env_mq_name_dispatcher = "littlecode.mq.name.dispatcher";
        private static final String env_mq_max_number = "littlecode.mq.max-number";
        private static final String env_mq_idle_sleep = "littlecode.mq.idle-sleep";
        private static final String env_client_id = "littlecode.mq.client.id";
        private static final String env_client_secret = "littlecode.mq.client.secret";
        private final ApplicationContext applicationContext;
        private final Environment environment;

        private String engine;
        private boolean autoCreate;
        private boolean autoStart;
        private boolean stopOnFail;
        private String hostName;
        private String vHostName;
        private String queueChannel;
        private String queueExchange;
        private int port;
        private String url;
        private List<String> queueName;
        private List<String> queueNameConsumer;
        private List<String> queueNameDispatcher;
        private String queueRegion;
        private int queueMaxNumber;
        private int queueIdleSleep;
        private String clientId;
        private String clientSecret;

        public Setting(ApplicationContext applicationContext, Environment environment) {
            this.applicationContext = applicationContext;
            this.environment = environment;
            this.load();
        }

        public String readEnv(String env, String defaultValue) {
            try {
                return environment.getProperty(env, defaultValue);
            } catch (Exception e) {
                return defaultValue;
            }
        }

        public List<String> readEnvList(String env) {
            try {
                var values = List.of(environment.getProperty(env, "").split(","));
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

        private void load() {
            this.engine = readEnv(env_mq_engine, "amqp");
            this.autoCreate = PrimitiveUtil.toBool(readEnv(env_mq_auto_create, "false"));
            this.autoStart = PrimitiveUtil.toBool(readEnv(env_mq_auto_start, "false"));
            this.stopOnFail = PrimitiveUtil.toBool(readEnv(env_mq_stop_on_fail, "true"));
            this.hostName = readEnv(env_mq_hostname);
            this.vHostName = readEnv(env_mq_v_hostname);
            this.port = PrimitiveUtil.toInt(readEnv(env_mq_port));
            this.url = readEnv(env_mq_url);
            this.queueChannel = readEnv(env_mq_channel);
            this.queueExchange = readEnv(env_mq_exchange);
            this.queueRegion = readEnv(env_mq_region);
            this.queueName = readEnvList(env_mq_name);
            this.queueNameConsumer = readEnvList(env_mq_name_consumer);
            this.queueNameDispatcher = readEnvList(env_mq_name_dispatcher);
            this.clientId = readEnv(env_client_id);
            this.clientSecret = readEnv(env_client_secret);
            this.queueMaxNumber = PrimitiveUtil.toInt(environment.getProperty(env_mq_max_number, "1"));
            this.queueIdleSleep = PrimitiveUtil.toInt(environment.getProperty(env_mq_idle_sleep, "1000"));
        }


        public List<String> getQueueNameConsumer() {
            if (!PrimitiveUtil.isEmpty(this.queueName))
                return this.queueName;
            if (!PrimitiveUtil.isEmpty(this.queueNameConsumer))
                return this.queueNameConsumer;
            return new ArrayList<>();
        }


        public List<String> getQueueNameDispatchers() {
            return this.queueNameDispatcher;
        }

        public int getQueueMaxNumber() {
            if (queueMaxNumber <= 0)
                return 1;
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
            public Task(){
            }

            public static Task of(Object type, Object body) {
                var outType=ObjectUtil.classToName(type);
                return Task
                        .builder()
                        .type(outType)
                        .body(body)
                        .build();
            }

            public static Task of(Object taskObject) {
                var type=taskObject.getClass().equals(String.class)
                        ?null
                        :taskObject.getClass();
                return of(type, taskObject);
            }

            public static Task from(String body) {
                var values=ObjectUtil.toMapObject(body);
                Task task=ObjectUtil.createFromValues(Task.class,values);
                if(task!=null)
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

            public Object getType(){
                return this.type==null?"":this.type;
            }

            @Transient
            public String getTypeName(){
                return ObjectContainer.classToName(this.type);
            }

            @Transient
            public Class<?> getTypeClass(){
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

    @Slf4j
    @RequiredArgsConstructor
    @Configuration
    public static class Factory {
        private final Setting setting;

        public Setting setting() {
            return setting;
        }

        public <T> T getBean(String beanName, Class<T> valueType) {
            try {
                return setting.getApplicationContext().getBean(beanName, valueType);
            } catch (BeansException ignore) {
                return null;
            }
        }

        @SuppressWarnings("unused")
        public String getBeanString(String beanName) {
            var response = this.getBean(beanName, String.class);
            return response == null ? "" : response;
        }

        public List<String> getBeanListString(String beanName) {
            var list = getBean(beanName, List.class);
            if (list == null)
                return new ArrayList<>();
            List<String> out = new ArrayList<>();
            for (var item : list) {
                var queue = item.toString().trim();
                if (!queue.isEmpty())
                    out.add(item.toString());
            }

            return out;
        }
    }
}