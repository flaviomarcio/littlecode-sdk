package com.littlecode.mq.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.containers.ObjectContainer;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.Amqp;
import com.littlecode.mq.adapter.AmqpByRabbitMQ;
import com.littlecode.mq.adapter.AwsSQS;
import com.littlecode.mq.adapter.MQAdapter;
import com.littlecode.mq.adapter.impl.MQAMQPRabbitMQImpl;
import com.littlecode.mq.adapter.impl.MQSQSAWSImpl;
import com.littlecode.mq.config.MQAutoConfiguration;
import com.littlecode.mq.config.MQSetting;
import com.littlecode.parsers.ObjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class MQTest {

    @BeforeAll
    public static void setUp() {
        UtilCoreConfig.setApplicationContext(Mockito.mock(ApplicationContext.class));
        UtilCoreConfig.setEnvironment(Mockito.mock(Environment.class));
    }

    @Test
    public void UT_CHECK_STARTED() {
//        Assertions.assertDoesNotThrow(() -> new MQBeans());
//        Assertions.assertDoesNotThrow(() -> new MQBeans().newMQSetting());
//        Assertions.assertDoesNotThrow(() -> new MQBeans().newMQ());
//        Assertions.assertNotNull(new MQBeans().newMQSetting());
//        Assertions.assertNotNull(new MQBeans().newMQ());


        Assertions.assertDoesNotThrow(() -> new MQAutoConfiguration(Mockito.mock(ApplicationContext.class), Mockito.mock(Environment.class)));
        var config = new MQAutoConfiguration(Mockito.mock(ApplicationContext.class), Mockito.mock(Environment.class));
        Assertions.assertDoesNotThrow(config::isAutoStart);
        Assertions.assertDoesNotThrow(config::start);
        config.setAutoStart(true);
        Assertions.assertDoesNotThrow(config::start);
    }


    @Test
    public void UT_CHECK_MQ() {

        var context=Mockito.mock(ApplicationContext.class);
        var environment=Mockito.mock(Environment.class);
        UtilCoreConfig.setApplicationContext(context);
        UtilCoreConfig.setEnvironment(environment);

        Assertions.assertThrows(NullPointerException.class, () -> new MQ((MQSetting) null));
        Assertions.assertThrows(NullPointerException.class, () -> new MQ((Environment) null));
        Assertions.assertDoesNotThrow(() -> new MQ(Mockito.mock(Environment.class)));
        Assertions.assertDoesNotThrow(()->new MQ(Mockito.mock(MQSetting.class)));
        Assertions.assertDoesNotThrow(() -> new MQ());
        var setting=Mockito.mock(MQSetting.class);
        Mockito.when(setting.getEngine()).thenReturn(MQAdapter.class.getCanonicalName());
        var mq=new MQ(setting);
        Assertions.assertDoesNotThrow(() -> mq.getSetting().getEnvironmentUtil());
        Assertions.assertDoesNotThrow(mq::getAdapter);
        Assertions.assertNotNull(mq.getSetting());
        Assertions.assertDoesNotThrow(mq::setting);
        Assertions.assertDoesNotThrow(()->mq.queueName(""));
        Assertions.assertDoesNotThrow(()->mq.dispatcher("teste123"));
        Assertions.assertDoesNotThrow(()->mq.dispatcher(new Object()));
        Assertions.assertDoesNotThrow(()->mq.dispatcher(MQ.Message.Task.builder().build()));
        Assertions.assertDoesNotThrow(mq::listen);


        Assertions.assertDoesNotThrow(()->new MQ.Configurer());
        Assertions.assertDoesNotThrow(()->MQ.Configurer.builder().build());
        Assertions.assertDoesNotThrow(()->MQ.Configurer.builder().build().getConfigurer());
        Assertions.assertDoesNotThrow(()->MQ.Configurer.builder().build());

        Assertions.assertDoesNotThrow(()->new MQ.Executor());
        Assertions.assertDoesNotThrow(()->MQ.Executor.builder().build());
        Assertions.assertDoesNotThrow(()->MQ.Executor.builder().build().getReceived());
        Assertions.assertDoesNotThrow(()->MQ.Executor.builder().build().getDispatcherObject());
        Assertions.assertDoesNotThrow(()->MQ.Executor.builder().build().getListen());
        Assertions.assertDoesNotThrow(()->MQ.Executor.builder().build().getDispatcherString());
        Assertions.assertDoesNotThrow(()->MQ.Executor.builder().build());

        Assertions.assertDoesNotThrow(MQ.Message::new);
        Assertions.assertDoesNotThrow(()->new MQ.Message.Task());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build().getType());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build().getMessageId());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build().getChecksum());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build().getBody());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build().getFail());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build().getTypeClass());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build().asContainer());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().messageId(UUID.randomUUID().toString()).build().asContainer());
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build().asObject(Object.class));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.builder().build().canType(Object.class));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.from(null));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.from("ancd"));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.from(ObjectUtil.toString(MQ.Message.Task.builder().messageId(UUID.randomUUID().toString()).build())));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.of(null));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.of(new Object()));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.of("234","23423"));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.of("234",null));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.of(String.class,"23423"));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.of(String.class,null));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.of(new MQ.Message.Task()));
        Assertions.assertNotNull(MQ.Message.Task.builder().type(null).build().getType());
        Assertions.assertEquals(MQ.Message.Task.builder().type(Object.class.getCanonicalName()).build().getType(),Object.class.getCanonicalName());


        Assertions.assertDoesNotThrow(()->new MQ.Message.Response());
        Assertions.assertDoesNotThrow(()->MQ.Message.Response.builder().build());
        Assertions.assertDoesNotThrow(()->MQ.Message.Response.builder().build().getMessageId());
        Assertions.assertDoesNotThrow(()->MQ.Message.Response.builder().build().getDt());
        Assertions.assertDoesNotThrow(()->MQ.Message.Response.builder().build().getChecksum());
        Assertions.assertDoesNotThrow(()->MQ.Message.Response.builder().build().setChecksum(UUID.randomUUID()));

    }

    private void UT_CHECK_ADAPTERS(MQAdapter mqAdapter) {


        Assertions.assertNotNull(mqAdapter);
        Assertions.assertDoesNotThrow(mqAdapter::setting);
        Assertions.assertDoesNotThrow(mqAdapter::mq);
        Assertions.assertDoesNotThrow(mqAdapter::listenStart);
        Assertions.assertDoesNotThrow(mqAdapter::beanQueueDispatcherObject);


        Assertions.assertDoesNotThrow(()->mqAdapter.listenStart().getListen().accept());
//        //Assertions.assertDoesNotThrow(()->mqAdapter.listenStart().getReceived().accept(MQ.Message.Task.builder().build()));
//        Assertions.assertDoesNotThrow(()->mqAdapter.beanQueueDispatcherObject().getDispatcherObject().accept(MQ.Message.Task.builder().build()));
//        Assertions.assertDoesNotThrow(()->mqAdapter.beanQueueDispatcherObject().getDispatcherString().accept("teste"));
    }

    @Test
    public void UT_CHECK_ADAPTERS() {
        var context=Mockito.mock(ApplicationContext.class);
        var environment=Mockito.mock(Environment.class);
        UtilCoreConfig.setApplicationContext(context);
        UtilCoreConfig.setEnvironment(environment);

        Assertions.assertThrows(NullPointerException.class,()-> new MQAdapter(null));
        Assertions.assertDoesNotThrow(()->new MQAdapter(Mockito.mock(MQ.class)));
        Assertions.assertDoesNotThrow(()->new Amqp(Mockito.mock(MQ.class)));
        Assertions.assertDoesNotThrow(()->new AmqpByRabbitMQ(Mockito.mock(MQ.class)));
        Assertions.assertDoesNotThrow(()->new AwsSQS(Mockito.mock(MQ.class)));
        Assertions.assertDoesNotThrow(()->new MQAMQPRabbitMQImpl(Mockito.mock(MQ.class)));
        Assertions.assertDoesNotThrow(()->new MQSQSAWSImpl(Mockito.mock(MQ.class)));

        var mq=new MQ(Mockito.mock(MQSetting.class));
        this.UT_CHECK_ADAPTERS(new MQAdapter(mq));
        this.UT_CHECK_ADAPTERS(new Amqp(mq));
        this.UT_CHECK_ADAPTERS(new AmqpByRabbitMQ(mq));
        this.UT_CHECK_ADAPTERS(new AwsSQS(mq));
        this.UT_CHECK_ADAPTERS(new MQAMQPRabbitMQImpl(mq));
        this.UT_CHECK_ADAPTERS(new MQSQSAWSImpl(mq));


//        Assertions.assertDoesNotThrow(()->new Amqp(Mockito.mock(MQ.class)));
//        Assertions.assertDoesNotThrow(() -> new MQAutoConfiguration(Mockito.mock(MQ.class)));
//
//        var config=new MQAutoConfiguration(Mockito.mock(MQ.class));
//        Assertions.assertDoesNotThrow(config::start);
//        config.setAutoStart(true);
//        Assertions.assertDoesNotThrow(config::start);
    }

    @Test
    public void UI_CHECK_MQSETTING() {
        Assertions.assertThrows(NullPointerException.class, () -> new MQSetting(null));
        Assertions.assertDoesNotThrow(() -> new MQSetting());
        Assertions.assertDoesNotThrow(() -> new MQSetting(Mockito.mock(Environment.class)));
        var config=new MQSetting();
        Assertions.assertDoesNotThrow(config::resetEnvs);
        Assertions.assertDoesNotThrow(config::isAutoCreate);
        Assertions.assertDoesNotThrow(config::isAutoStart);
        Assertions.assertDoesNotThrow(config::isStopOnFail);
        Assertions.assertDoesNotThrow(config::getHeartbeat);
        Assertions.assertDoesNotThrow(config::getPort);
        Assertions.assertDoesNotThrow(config::getQueueConsumers);
        Assertions.assertDoesNotThrow(config::getQueueIdleSleep);
        Assertions.assertDoesNotThrow(config::getQueueMaxNumber);
        Assertions.assertDoesNotThrow(config::getRecoveryInterval);
        Assertions.assertDoesNotThrow(config::getClientId);
        Assertions.assertDoesNotThrow(config::getClientSecret);
        Assertions.assertDoesNotThrow(config::getEngine);
        Assertions.assertDoesNotThrow(config::getHostName);
        Assertions.assertDoesNotThrow(config::getQueueChannel);
        Assertions.assertDoesNotThrow(config::getQueueExchange);
        Assertions.assertDoesNotThrow(config::getQueueName);
        Assertions.assertDoesNotThrow(config::getQueueNameConsumer);
        Assertions.assertDoesNotThrow(config::getQueueNameDispatcher);
        Assertions.assertDoesNotThrow(config::getQueueRegion);
        Assertions.assertDoesNotThrow(config::getUrl);
        Assertions.assertDoesNotThrow(config::getVHostName);

        Assertions.assertNotNull(config.getClientId());
        Assertions.assertNotNull(config.getClientSecret());
        Assertions.assertNotNull(config.getEngine());
        Assertions.assertNotNull(config.getHostName());
        Assertions.assertNotNull(config.getQueueChannel());
        Assertions.assertNotNull(config.getQueueExchange());
        Assertions.assertNotNull(config.getQueueName());
        Assertions.assertNotNull(config.getQueueNameConsumer());
        Assertions.assertNotNull(config.getQueueNameDispatcher());
        Assertions.assertNotNull(config.getQueueNameConsumers());
        Assertions.assertNotNull(config.getQueueNameDispatchers());
        Assertions.assertNotNull(config.getQueueRegion());
        Assertions.assertNotNull(config.getUrl());
        Assertions.assertNotNull(config.getVHostName());

        Assertions.assertFalse(config.isAutoCreate());
        Assertions.assertFalse(config.isAutoStart());
        Assertions.assertTrue(config.isStopOnFail());


        Assertions.assertEquals(config.getClientId(), "guest");
        Assertions.assertEquals(config.getClientSecret(), "guest");
        Assertions.assertEquals(config.getQueueName(), "");
        Assertions.assertEquals(config.getQueueRegion(), "us-east-1");
        Assertions.assertEquals(config.getHeartbeat(),30);
        Assertions.assertEquals(config.getRecoveryInterval(), 10);
        Assertions.assertEquals(config.getQueueConsumers(),1);
        Assertions.assertEquals(config.getQueueMaxNumber(),1);
        Assertions.assertEquals(config.getQueueIdleSleep(),1000);
        Assertions.assertEquals(config.getHeartbeat(), 30);
        Assertions.assertEquals(config.getRecoveryInterval(), 10);

        Assertions.assertTrue(config.getQueueNameConsumers().isEmpty());
        Assertions.assertTrue(config.getQueueNameDispatchers().isEmpty());

        config.setQueueName("teste");
        Assertions.assertFalse(config.getQueueNameConsumers().isEmpty());
        Assertions.assertFalse(config.getQueueNameDispatchers().isEmpty());

        config.setQueueName("");
        config.setQueueNameConsumer("teste,teste2");
        Assertions.assertFalse(config.getQueueNameConsumers().isEmpty());
        config.setQueueNameDispatcher("teste,teste2");
        Assertions.assertFalse(config.getQueueNameDispatchers().isEmpty());

    }



    @Test
    public void UT_CHECK_TASK() {
        enum EnumCheck {
            EnumA, EnumB
        }

        var objectCheck = new ObjectCheck();
        var bodyJson = String.format("{\"id\":\"%s\"}", objectCheck.getId());


        {
            var task = MQ.Message.Task.of(EnumCheck.EnumA, objectCheck);
            Assertions.assertTrue(MQ.Message.Task.of(bodyJson).getType().toString().isEmpty());
            Assertions.assertEquals(MQ.Message.Task.of(objectCheck).getType(), objectCheck.getClass().getName());

            Assertions.assertEquals(task.asString(), bodyJson);
            Assertions.assertNotNull(task.asContainer());
            Assertions.assertTrue(MQ.Message.Task.of(objectCheck).canType(ObjectCheck.class));
            for(var aBool: List.of(Boolean.TRUE, Boolean.FALSE)) {
                task.setCompareTypeFullName(aBool);
                Assertions.assertTrue(task.canType(EnumCheck.EnumA));
                Assertions.assertFalse(task.canType(EnumCheck.EnumB));
                Assertions.assertFalse(task.canType(null));
            }
        }


        {
            var task = MQ.Message.Task.of(objectCheck);
            Assertions.assertNotNull(task.asObject(ObjectCheck.class));
            Assertions.assertNotNull(task.asObject(ObjectCheck.class.getName()));
            Assertions.assertThrows(FrameworkException.class, () -> task.asObject(ObjectCheck.class.getSimpleName()));
            Assertions.assertThrows(FrameworkException.class, () -> task.asObject(ObjectCheck.class.toString()));
            ObjectContainer.classDictionaryRegister(ObjectCheck.class);
            Assertions.assertNotNull(task.getType());
            Assertions.assertNotNull(task.getTypeName());
            Assertions.assertNotNull(task.getTypeClass());

            Assertions.assertDoesNotThrow(() -> task.asObject(ObjectCheck.class));
            Assertions.assertDoesNotThrow(() -> task.asObject(ObjectCheck.class.getName()));
            Assertions.assertDoesNotThrow(() -> task.asObject(ObjectCheck.class.getSimpleName()));
            Assertions.assertDoesNotThrow(() -> task.asObject(ObjectCheck.class.toString()));
            Assertions.assertEquals(task.asObject(ObjectCheck.class).getId(), objectCheck.getId());

        }

    }

    @Getter
    @Setter
    public static class ObjectCheck {
        private final UUID id;

        public ObjectCheck() {
            this.id = UUID.randomUUID();
        }
    }

}
