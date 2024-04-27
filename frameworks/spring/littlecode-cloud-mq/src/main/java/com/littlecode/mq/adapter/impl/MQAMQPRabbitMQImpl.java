package com.littlecode.mq.adapter.impl;

import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.MQAdapter;
import com.littlecode.mq.config.MQSetting;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.util.BeanUtil;
import com.rabbitmq.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Slf4j
public class MQAMQPRabbitMQImpl extends MQAdapter {
    private static final String DEFAULT_LOCALHOST = "localhost";
    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "guest";
    private static final int DEFAULT_PORT = 5672;
    private final HashMap<String, Listener> listenerHashMap = new HashMap<>();
    private Dispatcher queueDispatcher;


    public MQAMQPRabbitMQImpl(MQ mq) {
        super(mq);
    }

    private static Connection newClient(MQSetting setting) {
        var queueUsername = PrimitiveUtil.isEmpty(setting.getClientId()) ? DEFAULT_USERNAME : setting.getClientId();
        var queuePassword = PrimitiveUtil.isEmpty(setting.getClientSecret()) ? DEFAULT_PASSWORD : setting.getClientSecret();
        var queueVHostName = PrimitiveUtil.isEmpty(setting.getVHostName()) ? "/" : setting.getVHostName();
        var queueHostName = PrimitiveUtil.isEmpty(setting.getHostName()) ? DEFAULT_LOCALHOST : setting.getHostName();
        var queuePort = setting.getPort() <= 0 ? DEFAULT_PORT : setting.getPort();
        try {
            var factory = new ConnectionFactory();
            factory.setUsername(queueUsername);
            factory.setPassword(queuePassword);
            factory.setVirtualHost(queueVHostName);
            factory.setHost(queueHostName);
            factory.setPort(queuePort);
            factory.setRequestedHeartbeat(setting.getHeartbeat()); // pulse for detect fail in connection
            factory.setAutomaticRecoveryEnabled(setting.getRecoveryInterval() > 0);//enable recovery
            factory.setNetworkRecoveryInterval(setting.getRecoveryInterval() * 1000);
            return factory.newConnection();
        } catch (IOException | TimeoutException e) {
            log.error("host: {}, port: {}, vHost: {}, vUsername: {}, error: fail on connect, ex:{}", queueHostName, queuePort, queueVHostName, queueUsername, e.getMessage());
        }
        return null;
    }

    public static void closeClient(Connection queueClient, Channel queueChannel) {
        try {
            if (queueChannel != null)
                queueChannel.close();
        } catch (TimeoutException | IOException ignored) {
        }
        try {
            if (queueClient != null)
                queueClient.close();
        } catch (IOException ignored) {
        }
    }

    public MQ.Executor listenStart() {
        this.listenStop();
        return MQ.Executor
                .builder()
                .dispatcherString(new MQ.MethodReturn<MQ.Message.Response, String>() {
                    @Override
                    public MQ.Message.Response accept(String var1) {
                        return null;
                    }
                })
                .dispatcherObject(new MQ.MethodReturn<MQ.Message.Response, MQ.Message.Task>() {
                    @Override
                    public MQ.Message.Response accept(MQ.Message.Task var1) {
                        return null;
                    }
                })
                .listen(() -> {
                    this.listenStop();
                    List<String> queueNameList = BeanUtil.of(MQ.MQ_BEAN_NAME_CONSUMER).as(List.class);
                    if (queueNameList==null || queueNameList.isEmpty())
                        queueNameList = setting().getQueueNameConsumers();

                    if (queueNameList!=null && !queueNameList.isEmpty()){
                        for (var queueName : queueNameList) {
                            for (int consumer = 1; consumer <= this.setting().getQueueConsumers(); consumer++) {
                                var listener = Listener.from(this, setting(), queueName, consumer);
                                this.listenerHashMap.put(listener.getName(), listener);
                                listener.start();
                            }
                        }
                    }
                })
                .build();
    }

    public void listenStop() {
        var map = Map.copyOf(listenerHashMap);
        listenerHashMap.clear();
        map.forEach((s, listener) -> listener.stop());
    }

    public MQ.Executor beanQueueDispatcherObject() {
        return MQ.Executor
                .builder()
                .dispatcherObject(task -> {
                    this.queueDispatcher =
                            (this.queueDispatcher == null)
                                    ? new Dispatcher(setting(), this)
                                    : queueDispatcher;
                    return queueDispatcher.dispatcher(task);
                })
                .listen(new MQ.Method() {
                    @Override
                    public void accept() {

                    }
                })
                .build();
    }


    public String queueSelector(final Channel queueChannel, final List<String> queueList) {
        String curQueueName = "";
        long curQueueCount = 0;
        for (var queueName : queueList) {
            if (!queueCreate(queueChannel, queueName))
                continue;
            try {
                var messagesAvailable = queueChannel.messageCount(queueName);
                //se a fila estiver vazia
                if (messagesAvailable == 0)
                    return queueName;

                //se ainda nao tiver queueName repassamos a fila do loop
                //se quantidade de itens na fila for maior que a quantidade da fila do loop
                if (PrimitiveUtil.isEmpty(curQueueName) || (curQueueCount > messagesAvailable)) {
                    curQueueName = queueName;
                    curQueueCount = messagesAvailable;
                }
            } catch (IOException e) {
                log.error("Queue: [{}], queueSelector: {}", queueName, e.getMessage());
            }
        }
        return curQueueName;
    }

    public boolean queueExists(final Channel queueChannel, final String queueName) {
        try {
            queueChannel.queueDeclarePassive(queueName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean queueCreate(final Channel channelDispatcher, final String queueName) {
//        if (this.queueExists(channelDispatcher, queueName))
//            return true;
        try {
            channelDispatcher.queueDeclare(queueName, false, false, false, null);
            return true;
        } catch (IOException ignored) {
            return true;
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class Dispatcher {
        private final MQSetting setting;
        private final MQAMQPRabbitMQImpl adapter;
        private List<String> queue;

        public List<String> queue() {
            var queueNames = this.queue;
            if (PrimitiveUtil.isEmpty(queueNames))
                //noinspection unchecked
                queueNames = BeanUtil.of(MQ.MQ_BEAN_NAME_DISPATCHER).as(List.class);
            if (PrimitiveUtil.isEmpty(queueNames))
                queueNames = setting.getQueueNameDispatchers();
            return queueNames;
        }

        public MQ.Message.Response dispatcher(Object o) {
            if (o == null)
                throw ExceptionBuilder.ofFrameWork("Invalid object");

            var message = ObjectUtil.toString(o);
            if (message == null || message.trim().isEmpty())
                throw ExceptionBuilder.ofFrameWork("Invalid object body");
            return this.internalDispatcher(message);
        }

        private synchronized MQ.Message.Response internalDispatcher(String message) {
            Connection queueClient = null;
            Channel queueChannel = null;
            try {
                queueClient = MQAMQPRabbitMQImpl.newClient(this.adapter.setting());

                if (queueClient == null)
                    throw ExceptionBuilder.ofFrameWork("Invalid queueClient");

                queueChannel = queueClient.createChannel();

                if (queueChannel == null)
                    throw ExceptionBuilder.ofFrameWork("Invalid channelDispatcher");

                final var queueExchange = adapter.setting().getQueueExchange();
                final var queueName = adapter.queueSelector(queueChannel, this.queue());
                if (queueName.isEmpty())
                    throw ExceptionBuilder.ofFrameWork("Invalid queue name");
                log.debug("Queue:[{}], dispatcher a new message", queueName);
                queueChannel.basicPublish(queueExchange, queueName, null, message.getBytes());

                return MQ.Message.Response
                        .builder()
                        .messageId(UUID.randomUUID().toString())
                        .dt(LocalDateTime.now())
                        .build();
            } catch (IOException e) {
                log.error("host:{}, port: {}, error: fail on sent, ex:{}", queueClient.getAddress().getHostAddress(), queueClient.getPort(), e.getMessage());
                log.error(e.getMessage());
                throw ExceptionBuilder.of(e);
            } finally {
                closeClient(queueClient, queueChannel);
            }
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class Listener {
        private final MQAMQPRabbitMQImpl rabbitMQ;
        private final MQSetting setting;
        private final String queueName;
        private final int queueConsumer;
        private Connection queueClient = null;
        private Channel queueChannel = null;
        private MQ.Executor queueExecutor = null;

        public static Listener from(MQAMQPRabbitMQImpl rabbitMQ, MQSetting setting, String queueName, int queueConsumer) {
            return new Listener(rabbitMQ, setting, queueName, queueConsumer);
        }

        public String getName() {
            return String.format("%s[%d]", this.queueName, this.queueConsumer);
        }

        private void sleep(int queueIdleSleep) {
            try {
                Thread.sleep(queueIdleSleep);//wait 1s to empty messages
            } catch (InterruptedException ignored) {
            }
        }

        private void queueExec(final Channel queueChannel, final long deliveryTag, final String messageBody) {
            if (this.queueClient == null)
                return;

            log.debug("Queue: [{}], messageId: [{}], started", queueName, deliveryTag);

            var task = MQ.Message.Task.from(messageBody);
            task.setMessageId(String.valueOf(deliveryTag));

            if (queueExecutor == null || queueExecutor.getReceived()==null){
                try {
                    queueChannel.basicNack(deliveryTag, false, true);//redireciona a mensagem novamente para fila
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            boolean ack = false;
            try {
                queueExecutor.getReceived().accept(task);
                ack = true;
            } catch (Exception e) {
                log.error("Queue: [{}], messageId: [{}], error on ack: {}", queueName, deliveryTag, e.getMessage());
            }

            try {
                if (ack) {
                    log.debug("Queue: [{}], messageId: [{}], required Ack", queueName, deliveryTag);
                    queueChannel.basicAck(deliveryTag, false);//remove mensagem apos execucao sem nenhum exception
                } else {
                    log.debug("Queue: [{}], messageId: [{}], required NAck", queueName, deliveryTag);
                    queueChannel.basicNack(deliveryTag, false, true);//redireciona a mensagem novamente para fila
                }
            } catch (IOException e2) {
                log.error("Queue: [{}], messageId: [{}], error on [Ack|NAck]: {}", queueName, deliveryTag, e2.getMessage());
            }
            log.debug("Queue: [{}], messageId: [{}], finished", queueName, deliveryTag);
        }

        public void stop() {
            synchronized (this) {
                closeClient(queueClient, queueChannel);
                queueChannel = null;
                queueClient = null;
            }
        }

        private void internalRun(String logPrefix) {

            this.stop();

            try {
                log.info("{}: connecting", logPrefix);
                queueClient = MQAMQPRabbitMQImpl.newClient(this.rabbitMQ.setting());
                if (queueClient == null) {
                    log.error("{}: fail on connect", logPrefix);
                    return;
                }
                log.info("{}: connected", logPrefix);
                queueChannel = queueClient.createChannel();
                queueChannel.basicQos(setting.getQueueMaxNumber());

                while (!this.rabbitMQ.queueExists(queueChannel, queueName)) {
                    log.error("{} queue[{}] not found", queueName, logPrefix);
                    sleep(setting.getQueueIdleSleep());
                }

                DefaultConsumer consumer = new DefaultConsumer(queueChannel) {
                    @Override
                    public void handleDelivery(
                            String consumerTag,
                            Envelope envelope,
                            AMQP.BasicProperties properties,
                            byte[] body) {

                        var messageId = envelope.getDeliveryTag();
                        var logPrefix = String.format("Queue:[%s]:[%s]", queueName, messageId);
                        log.debug("{}, started", logPrefix);
                        try {
                            synchronized (this) {
                                if (queueClient == null)
                                    return;
                                queueExec(queueChannel, messageId, new String(body, StandardCharsets.UTF_8));
                            }
                        } catch (Exception e) {
                            log.error("{}, fail:{}", logPrefix, e.getMessage());
                        } finally {
                            log.debug("{}, finished", logPrefix);
                        }
                    }
                };
                queueChannel.basicConsume(queueName, false, consumer);

            } catch (IOException e) {
                log.error("{} {}", logPrefix, e.getMessage());
            }
        }

        public void start() {
            var logPrefix = String.format("Queue:[%s]", queueName);
            try{
                if(queueExecutor==null)
                    return;
                int loop = 0;
                log.info("{} started", logPrefix);
                try {
                    this.internalRun(String.format("%s-%04d: ", logPrefix, ++loop));
                } catch (Exception e) {
                    log.error("{} {}", logPrefix, e.getMessage());
                }
            }finally {
                log.info("{} finished", logPrefix);
            }
        }
    }

}
