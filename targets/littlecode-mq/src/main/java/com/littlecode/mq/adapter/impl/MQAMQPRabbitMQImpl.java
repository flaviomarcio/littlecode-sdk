package com.littlecode.mq.adapter.impl;

import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.MQAdapter;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.util.SystemUtil;
import com.rabbitmq.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

@Slf4j
public class MQAMQPRabbitMQImpl extends MQAdapter {
    private static final String DEFAULT_LOCALHOST = "localhost";
    private static final String DEFAULT_USERNAME = "guest";
    private static final String DEFAULT_PASSWORD = "guest";
    private static final int DEFAULT_PORT = 5672;
    private Dispatcher queueDispatcher;

    public MQAMQPRabbitMQImpl(MQ mq) {
        super(mq);
    }

    private static Connection newClient(MQ.Setting setting) {
        var accessKey = PrimitiveUtil.isEmpty(setting.getClientId()) ? DEFAULT_USERNAME : setting.getClientId();
        var secretKey = PrimitiveUtil.isEmpty(setting.getClientSecret()) ? DEFAULT_PASSWORD : setting.getClientSecret();
        var queueVHostName = PrimitiveUtil.isEmpty(setting.getVHostName()) ? "/" : setting.getVHostName();
        var queueHostName = PrimitiveUtil.isEmpty(setting.getHostName()) ? DEFAULT_LOCALHOST : setting.getHostName();
        var queuePort = setting.getPort() <= 0 ? DEFAULT_PORT : setting.getPort();

        var factory = new ConnectionFactory();
        factory.setUsername(accessKey);
        factory.setPassword(secretKey);
        factory.setVirtualHost(queueVHostName);
        factory.setHost(queueHostName);
        factory.setPort(queuePort);

        try {
            return factory.newConnection();
        } catch (TimeoutException | IOException e) {
            throw ExceptionBuilder.of(e);
        }
    }

    public MQ.Executor listenStart() {
        return MQ.Executor
                .builder()
                .listen(() -> {
                    var queueFactory = new MQ.Factory(setting());
                    var queueNameList = queueFactory.getBeanListString(MQ.MQ_BEAN_NAME_CONSUMER);
                    if (PrimitiveUtil.isEmpty(queueNameList))
                        queueNameList = queueFactory.setting().getQueueNameConsumer();

                    if (PrimitiveUtil.isEmpty(queueNameList))
                        throw ExceptionBuilder.ofFrameWork("Invalid queue name");

                    for (var queueName : queueNameList)
                        Listener.listen(this, queueFactory, queueName);
                })
                .build();
    }

    public MQ.Executor beanQueueDispatcherObject() {
        return MQ.Executor
                .builder()
                .dispatcherObject(task -> {
                    this.queueDispatcher =
                            (this.queueDispatcher==null)
                                    ?new Dispatcher(this, new MQ.Factory(setting()))
                                    :queueDispatcher;
                    return queueDispatcher.dispatcher(task);
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

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
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
    @Configuration
    @RequiredArgsConstructor
    public static class Dispatcher {
        private final MQAMQPRabbitMQImpl adapter;
        private final MQ.Factory queueFactory;
        private List<String> queue;
        private Connection queueClient;
        private Channel channelDispatcher;

        public List<String> queue() {
            var queueNames = this.queue;
            if (PrimitiveUtil.isEmpty(queueNames))
                queueNames = queueFactory.getBeanListString(MQ.MQ_BEAN_NAME_DISPATCHER);
            if (PrimitiveUtil.isEmpty(queueNames))
                queueNames = this.queueFactory.setting().getQueueNameDispatchers();
            return queueNames;
        }

        @SuppressWarnings("unused")
        public Dispatcher queue(List<String> queueName) {
            this.queue = queueName;
            return this;
        }

        public MQ.Message.Response dispatcher(Object o) {
            if (o == null)
                throw ExceptionBuilder.ofFrameWork("Invalid object");

            var message = ObjectUtil.toString(o);
            if (message == null || message.trim().isEmpty())
                throw ExceptionBuilder.ofFrameWork("Invalid object body");
            return this.internalDispatcher(message);
        }

        @SuppressWarnings("unused")
        public MQ.Message.Response dispatcher(String message) {
            if (message == null || message.trim().isEmpty())
                throw ExceptionBuilder.ofFrameWork("Invalid message");
            return this.internalDispatcher(message);
        }

        private synchronized MQ.Message.Response internalDispatcher(String message) {
            if (queueClient == null)
                queueClient = MQAMQPRabbitMQImpl.newClient(this.adapter.setting());
            try {
                if (channelDispatcher == null) {
                    var channel = PrimitiveUtil.toInt(adapter.setting().getQueueChannel());
                    channelDispatcher = channel <= 0
                            ? queueClient.createChannel()
                            : queueClient.createChannel(channel);
                }

                final var queueExchange = adapter.setting().getQueueExchange();
                final var queueName = adapter.queueSelector(channelDispatcher, this.queue());
                if (queueName.isEmpty())
                    throw ExceptionBuilder.ofFrameWork("Invalid queue name");
                log.debug("Queue:[{}], dispatcher a new message", queueName);
                channelDispatcher.basicPublish(queueExchange, queueName, null, message.getBytes());

                return MQ.Message.Response
                        .builder()
                        .messageId(UUID.randomUUID().toString())
                        .dt(LocalDateTime.now())
                        .build();
            } catch (IOException e) {
                throw ExceptionBuilder.of(e);
            }
        }
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class Listener implements Runnable {
        private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
        private static final ConcurrentMap<String, Future<?>> listeners = new ConcurrentHashMap<>();
        private final MQAMQPRabbitMQImpl rabbitMQ;
        private final MQ.Factory queueFactory;
        private final String queueName;

        @SuppressWarnings("unused")
        public static void stop() {
            var aux = Map.copyOf(listeners);
            listeners.clear();
            aux.forEach((s, listener) -> listener.cancel(true));
        }

        public static void listen(MQAMQPRabbitMQImpl rabbitMQ, MQ.Factory queueFactory, String queueName) {
            if (listeners.containsKey(queueName.toLowerCase()))
                return;
            Future<?> listener = executorService.submit(new Listener(rabbitMQ, queueFactory, queueName));
            listeners.put(queueName.toLowerCase(), listener);
        }


        private void sleep(int queueIdleSleep) {
            try {
                Thread.sleep(queueIdleSleep);//wait 1s to empty messages
            } catch (InterruptedException ignored) {
            }
        }

        private void queueExec(final MQ.Executor queueExecutor, final Channel queueChannel, final String messageId, final Delivery delivery) {
            var messageBody = new String(delivery.getBody(), StandardCharsets.UTF_8);

            log.debug("Queue: [{}], messageId: [{}], started", queueName, messageId);

            try {
                var task = MQ.Message.Task.from(messageBody);
                task.setMessageId(messageId);

                //executa a task
                queueExecutor
                        .getReceived().accept(task);
                //remove a mensagem da fila
                queueChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                log.debug("Queue: [{}], messageId: [{}], successful", queueName, messageId);
            } catch (IOException e) {
                throw ExceptionBuilder.of(e);
            }
        }

        @Override
        public void run() {
            String logPrefix=String.format("Queue:[%s]",queueName);
            log.debug("{} started", logPrefix);
            var queueExecutor = queueFactory.getBean(MQ.MQ_BEAN_RECEIVER, MQ.Executor.class);
            if (queueExecutor == null)
                throw ExceptionBuilder.ofFrameWork(String.format("Invalid %s", MQ.Executor.class.getName()));

            Connection queueClient=null;
            try{
                queueClient = MQAMQPRabbitMQImpl.newClient(this.rabbitMQ.setting());
                var queueChannel = queueClient.createChannel();
                while (!this.rabbitMQ.queueExists(queueChannel, queueName)) {
                    log.error("Queue: [{}], not found", queueName);
                    sleep(queueFactory.setting().getQueueIdleSleep());
                }

                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    var messageId = String.valueOf(delivery.getEnvelope().getDeliveryTag());
                    String _logPrefix=String.format("Queue:[%s]:[%s]",queueName, messageId);
                    log.debug("{}, started", _logPrefix);
                    try {
                        queueExec(queueExecutor, queueChannel, messageId, delivery);
                    } catch (Exception e) {
                        log.error("{}, fail:{}", _logPrefix,e.getMessage());
                    } finally {
                        log.debug("{}, finished", _logPrefix);
                    }
                };

                queueChannel.basicConsume(queueName, false, deliverCallback, consumerTag -> {});

            } catch (IOException e) {
                throw ExceptionBuilder.of(e);
            } finally {
                while(!Thread.currentThread().isInterrupted()){
                    log.debug("{}: wait finish listen", logPrefix);
                    SystemUtil.sleep(1000);
                }
                if(queueClient!=null) {
                    try {
                        queueClient.close();
                    } catch (IOException ignored) {
                    }
                }
                log.debug("{} finished", logPrefix);
            }


        }
    }
}
