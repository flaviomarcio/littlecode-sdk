package com.littlecode.mq.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.containers.ObjectContainer;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.*;
import com.littlecode.mq.adapter.impl.MQAMQPRabbitMQImpl;
import com.littlecode.mq.adapter.impl.MQSQSAWSImpl;
import com.littlecode.mq.config.MQAutoConfiguration;
import com.littlecode.mq.config.MQSetting;
import com.littlecode.parsers.ObjectUtil;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class MQTest {

    @Test
    public void UT_CHECK_STARTED() {
        Assertions.assertThrows(NullPointerException.class,() -> new MQAutoConfiguration(null,Mockito.mock(ApplicationContext.class),Mockito.mock(Environment.class)));
        Assertions.assertDoesNotThrow(() -> new MQAutoConfiguration(Mockito.mock(MQ.class),Mockito.mock(ApplicationContext.class),Mockito.mock(Environment.class)));
        var config=new MQAutoConfiguration(Mockito.mock(MQ.class),Mockito.mock(ApplicationContext.class),Mockito.mock(Environment.class));
        Assertions.assertDoesNotThrow(() -> config.getMq());
        Assertions.assertDoesNotThrow(() -> config.isAutoStart());
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

        Assertions.assertThrows(NullPointerException.class,()-> new MQ(null));
        Assertions.assertDoesNotThrow(()->new MQ(Mockito.mock(MQSetting.class)));
        var setting=Mockito.mock(MQSetting.class);
        Mockito.when(setting.getEngine()).thenReturn(MQAdapter.class.getCanonicalName());
        var mq=new MQ(setting);
        Assertions.assertDoesNotThrow(mq::getSetting);
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
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.from("ancd"));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.from(ObjectUtil.toString(MQ.Message.Task.builder().messageId(UUID.randomUUID().toString()).build())));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.of(new Object()));
        Assertions.assertDoesNotThrow(()->MQ.Message.Task.of("234","23423"));
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
    public void UI_CHECK_ATTR(){
        var config=new MQSetting();
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

        Assertions.assertNull(config.getClientId());
        Assertions.assertNull(config.getClientSecret());
        Assertions.assertNull(config.getEngine());
        Assertions.assertNull(config.getHostName());
        Assertions.assertNull(config.getQueueChannel());
        Assertions.assertNull(config.getQueueExchange());
        Assertions.assertNull(config.getQueueName());
        Assertions.assertNull(config.getQueueNameConsumer());
        Assertions.assertNull(config.getQueueNameDispatcher());
        Assertions.assertNotNull(config.getQueueNameConsumers());
        Assertions.assertNotNull(config.getQueueNameDispatchers());
        Assertions.assertNull(config.getQueueRegion());
        Assertions.assertNull(config.getUrl());
        Assertions.assertNull(config.getVHostName());

        Assertions.assertEquals(config.getHeartbeat(),30);
        Assertions.assertEquals(config.getRecoveryInterval(),60);
        Assertions.assertEquals(config.getQueueConsumers(),1);
        Assertions.assertEquals(config.getQueueMaxNumber(),1);
        Assertions.assertEquals(config.getQueueIdleSleep(),1000);

        config.setHeartbeat(1);
        Assertions.assertEquals(config.getHeartbeat(),1);
        config.setRecoveryInterval(1);
        Assertions.assertEquals(config.getRecoveryInterval(),1);
        config.setQueueConsumers(2);
        Assertions.assertEquals(config.getQueueConsumers(),2);
        config.setQueueMaxNumber(2);
        Assertions.assertEquals(config.getQueueMaxNumber(),2);
        config.setQueueIdleSleep(1);
        Assertions.assertEquals(config.getQueueIdleSleep(),1);

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
            Assertions.assertTrue(task.canType(EnumCheck.EnumA));
            Assertions.assertFalse(task.canType(EnumCheck.EnumB));
            Assertions.assertFalse(task.canType(null));
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
