package com.littlecode.mq;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.containers.ObjectContainer;
import com.littlecode.mq.adapter.MQAdapter;
import com.littlecode.mq.config.MQSetting;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.privates.CoreUtilAutoConfiguration;
import com.littlecode.util.BeanUtil;
import com.littlecode.util.EnvironmentUtil;
import lombok.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
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
@Component
@Getter
public class MQ {
    public static final String MQ_BEAN_NAME_DISPATCHER = "littleCodeMqBeanNameDispatcher";
    public static final String MQ_BEAN_NAME_CONSUMER = "littleCodeMqBeanNameConsumer";
    public static final String MQ_BEAN_RECEIVER = "littleCodeMqBeanReceiver";
    public static final String MQ_BEAN_CONFIGURER = "littleCodeMqBeanConfigurer";
    private final MQSetting setting;
    private MQAdapter adapter = null;

    public MQ(MQSetting setting) {
        if(setting==null)
            throw new NullPointerException("setting is null");
        this.setting = setting;
    }

    public MQSetting setting(){
        return this.setting;
    }

    public MQ queueName(String queueName) {
        this.setting.setQueueName(queueName);
        return this;
    }

    private MQAdapter adapterCreate() {
        if (adapter != null)
            return this.adapter;

        var configurer = BeanUtil
                .of(UtilCoreConfig.getApplicationContext())
                .bean(MQ.MQ_BEAN_CONFIGURER)
                .getBean(MQ.Configurer.class);
        if (configurer != null)
            configurer.configurer.accept(this.setting);

        var engine = setting.getEngine();
        if (engine==null || engine.trim().isEmpty())
            throw ExceptionBuilder.ofFrameWork(String.format("Invalid [%s] name", MQAdapter.class.getName()));
        var aClass = ObjectUtil.getClassByName(engine);
        this.adapter = ObjectUtil.createWithArgsConstructor(aClass, this);
        if (adapter == null)
            throw ExceptionBuilder.ofFrameWork(String.format("Invalid [%s] name: [%s]", MQAdapter.class.getName(), engine));

        return adapter;
    }

    private Message.Response internalDispatcher(MQ.Message.Task task) {
        var adapter = adapterCreate();
        return adapter
                .beanQueueDispatcherObject()
                .getDispatcherObject().accept(task);
    }

    public Message.Response dispatcher(Object task) {
        return this.internalDispatcher(MQ.Message.Task.of(task));
    }

    public Message.Response dispatcher(Message.Task task) {
        return this.internalDispatcher(task);
    }

    public void listen() {
        var adapter = this.adapterCreate();
        adapter
                .listenStart()
                .getListen()
                .accept();
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
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Configurer {
        private MethodArg<MQSetting> configurer;
    }

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Executor {
        private Method listen;
        private MethodArg<Message.Task> received;
        private MethodReturn<Message.Response, MQ.Message.Task> dispatcherObject;
        private MethodReturn<Message.Response, String> dispatcherString;
    }

    @NoArgsConstructor
    public static class Message {

        @Builder
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
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
                if (type != null && this.getType() != null){
                    var eA = ObjectUtil.classToName(type);
                    var eB = ObjectUtil.classToName(getType());
                    if (eA.equals(eB))
                        return true;

                    var of = ObjectContainer.classDictionaryByName(eA);
                    return (of != null);
                }
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
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Response{
            private String messageId;
            private LocalDateTime dt;
            @Setter
            private UUID checksum;
        }
    }
}