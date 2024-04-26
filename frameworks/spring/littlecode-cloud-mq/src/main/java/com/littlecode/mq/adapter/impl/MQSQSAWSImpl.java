package com.littlecode.mq.adapter.impl;

import com.littlecode.mq.MQ;
import com.littlecode.mq.adapter.MQAdapter;
import com.littlecode.mq.config.MQSetting;
import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
public class MQSQSAWSImpl extends MQAdapter {
    private Dispatcher queueDispatcher;

    public MQSQSAWSImpl(MQ mq) {
        super(mq);
    }

    public MQ.Executor listenStart() {
        return MQ.Executor
                .builder()
                .listen(() -> {
                    //noinspection unchecked
                    List<String> queueNameList = BeanUtil.of(context()).bean(MQ.MQ_BEAN_NAME_CONSUMER).as(List.class);
                    if (PrimitiveUtil.isEmpty(queueNameList))
                        queueNameList = setting().getQueueNameConsumer();

                    if (PrimitiveUtil.isEmpty(queueNameList))
                        throw ExceptionBuilder.ofFrameWork("Invalid queue name");

                    for (var queueName : queueNameList)
                        Listener.listen(this, setting(), queueName);
                })
                .build();
    }

    public MQ.Executor beanQueueDispatcherObject() {
        return MQ.Executor
                .builder()
                .dispatcherObject(task -> {
                    this.queueDispatcher = this.queueDispatcher == null
                            ? new Dispatcher(setting(), this)
                            : queueDispatcher;
                    return queueDispatcher.dispatcher(task);
                })
                .build();
    }

    public SqsClient newClient() {
        var accessKey = setting().getClientId();
        var secretKey = setting().getClientSecret();
        var queueUri = setting().getUrl();
        var queueRegion = Region.of(setting().getQueueRegion());

        if (queueUri == null || queueUri.trim().isEmpty())
            return SqsClient.builder()
                    .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                    .region(queueRegion)
                    .build();

        return SqsClient.builder()
                .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey))
                .region(queueRegion)
                .endpointOverride(URI.create(queueUri))
                .build();
    }

    public String queueSelector(SqsClient queueClient, List<String> queueList) {
        if (queueList.isEmpty())
            return "";

        String curQueueName = "";
        int curQueueCount = 0;
        for (var queueName : queueList) {
            if (!queueCreate(queueClient, queueName))
                continue;
            var queueUrl = queueClient.createQueue(CreateQueueRequest.builder().queueName(queueName).build()).queueUrl();
            GetQueueAttributesRequest request = GetQueueAttributesRequest.builder()
                    .queueUrl(queueUrl)
                    .attributeNames(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES)
                    .build();
            GetQueueAttributesResponse response = queueClient.getQueueAttributes(request);
            int messagesAvailable = Integer.parseInt(response.attributes().get(QueueAttributeName.APPROXIMATE_NUMBER_OF_MESSAGES));
            //se a fila estiver vazia
            if (messagesAvailable == 0)
                return queueName;

            //se ainda nao tiver queueName repassamos a fila do loop
            //se quantidade de itens na fila for maior que a quantidade da fila do loop
            if (PrimitiveUtil.isEmpty(curQueueName) || (curQueueCount > messagesAvailable)) {
                curQueueName = queueName;
                curQueueCount = messagesAvailable;
            }
        }
        return curQueueName;
    }

    @SuppressWarnings("unused")
    public String queueSelector(List<String> queueList) {
        return queueSelector(newClient(), queueList);
    }

    public boolean queueExists(SqsClient queueClient, String queueName) {
        var queueList = queueClient.listQueues();
        if (queueList == null || queueList.queueUrls().isEmpty())
            return false;
        for (var queue : queueList.queueUrls()) {
            if (queue.endsWith(queueName))
                return true;
        }
        return false;
    }

    @SuppressWarnings("unused")
    public boolean queueExists(String queueName) {
        return this.queueExists(newClient(), queueName);
    }

    public boolean queueCreate(SqsClient queueClient, String queueName) {
        try {
            if (this.queueExists(queueClient, queueName))
                return true;
            var queueUrl = queueClient.createQueue(CreateQueueRequest.builder().queueName(queueName).build()).queueUrl();
            return !queueUrl.isEmpty();
        } catch (AwsServiceException | SdkClientException e) {
            log.error("Queue:[{}], fail: {}", queueName, e.getMessage());
        }
        return false;
    }

    @SuppressWarnings("unused")
    public boolean queueCreate(String queueName) {
        return queueCreate(newClient(), queueName);
    }

    @Slf4j
    @RequiredArgsConstructor
    public static class Dispatcher {
        private final MQSetting setting;
        private MQSQSAWSImpl adapter;
        private List<String> queue;

        public List<String> queue() {
            List<String> queueNames = this.queue;
            if (PrimitiveUtil.isEmpty(queueNames))//noinspection unchecked
                queueNames = BeanUtil.of(setting.getContext()).bean(MQ.MQ_BEAN_NAME_DISPATCHER).as(List.class);
            if (PrimitiveUtil.isEmpty(queueNames))
                queueNames = this.setting.getQueueNameDispatchers();
            return queueNames;
        }

        public Dispatcher queue(List<String> queueName) {
            this.queue = queueName;
            return this;
        }

        public MQ.Message.Response dispatcher(MQ.Message.Task task) {
            if (task == null)
                throw ExceptionBuilder.of(MQ.Message.Task.class);

            var message = ObjectUtil.toString(task);
            if (PrimitiveUtil.isEmpty(message))
                throw ExceptionBuilder.ofDefault("Invalid [%s] body", MQ.Message.Task.class);
            return this.internalDispatcher(message);
        }

        public MQ.Message.Response dispatcher(String message) {
            if (message == null || message.trim().isEmpty())
                throw ExceptionBuilder.ofFrameWork("Invalid message");
            return this.internalDispatcher(message);
        }

        private MQ.Message.Response internalDispatcher(String message) {
            try (var sqsClient = adapter.newClient()) {
                var queueName = adapter.queueSelector(sqsClient, this.queue());
                if (queueName.isEmpty())
                    throw ExceptionBuilder.ofFrameWork("Invalid queue name");
                log.debug("Queue:[{}], dispatcher a new message", queueName);
                try {
                    GetQueueUrlResponse getQueueUrlResponse = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
                    var response = sqsClient.sendMessage(
                            SendMessageRequest.builder()
                                    .queueUrl(getQueueUrlResponse.queueUrl())
                                    .messageBody(message)
                                    .build()
                    );

                    log.debug("Queue:[{}], message: [{}] dispatcher successful", response.messageId(), queueName);

                    return MQ.Message.Response
                            .builder()
                            .messageId(response.messageId())
                            .dt(LocalDateTime.now())
                            .build();
                } catch (AwsServiceException | SdkClientException e) {
                    log.error("Queue:[{}], fail: {}, body: {}", queueName, e.getMessage(), message);
                    if (e instanceof QueueDoesNotExistException) {
                        if (!setting.isAutoCreate())
                            throw ExceptionBuilder.of(e);
                        if (!adapter.queueCreate(sqsClient, queueName))
                            throw ExceptionBuilder.of(e);
                        return this.dispatcher(message);
                    }
                    throw ExceptionBuilder.of(e);
                }
            }
        }

    }

    @Slf4j
    @RequiredArgsConstructor
    public static class Listener implements Runnable {
        private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
        private static final ConcurrentMap<String, Future<?>> listeners = new ConcurrentHashMap<>();
        private final MQSQSAWSImpl adapter;
        private final MQSetting setting;
        private final String queueName;

        @SuppressWarnings("unused")
        public static void stop() {
            var aux = Map.copyOf(listeners);
            listeners.clear();
            aux.forEach((s, listener) -> listener.cancel(true));
        }

        public static void listen(MQSQSAWSImpl adapter, MQSetting setting, String queueName) {
            if (listeners.containsKey(queueName.toLowerCase()))
                return;
            Future<?> listener = executorService.submit(new Listener(adapter, setting, queueName));
            listeners.put(queueName.toLowerCase(), listener);
        }

        private void sleep(int queueIdleSleep) {
            try {
                Thread.sleep(queueIdleSleep);//wait 1s to empty messages
            } catch (InterruptedException ignored) {
            }
        }

        private void queueExec(final MQ.Executor queueExecutor, final SqsClient queueClient, final Message message, final String queueUrl) {
            log.debug("Queue: [{}], messageId: [{}], started", queueName, message.messageId());
            try {
                //executa a task
                queueExecutor.getReceived().accept(
                        MQ.Message.Task
                                .builder()
                                .messageId(message.messageId())
                                .body(message.body())
                                .build()
                );
                //remove a mensagem da fila
                queueClient.deleteMessage(
                        DeleteMessageRequest
                                .builder()
                                .queueUrl(queueUrl)
                                .receiptHandle(message.receiptHandle())
                                .build()
                );
                log.debug("Queue: [{}], messageId: [{}], successful", queueName, message.messageId());
            } catch (AwsServiceException | SdkClientException e) {
                log.error("Queue: [{}], fail, messageId: [{}], body: {}", queueName, message.messageId(), message.body());
                throw ExceptionBuilder.of(e);
            }
        }

        @Override
        public void run() {
            log.debug("Queue:[{}] started", this.queueName);
            var queueExecutor = BeanUtil.of(setting.getContext()).bean(MQ.MQ_BEAN_RECEIVER).getBean(MQ.Executor.class);
            if (queueExecutor == null)
                throw ExceptionBuilder.ofFrameWork(String.format("Invalid %s", MQ.Executor.class.getName()));
            var queueMaxNumber = setting.getQueueMaxNumber();
            var queueIdleSleep = setting.getQueueIdleSleep();

            try (var sqsClient = adapter.newClient()) {
                while (!this.adapter.queueExists(sqsClient, queueName)) {
                    log.error("Queue: [{}], not found", queueName);
                    sleep(queueIdleSleep);
                }
                var queueUrl = sqsClient.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build()).queueUrl();
                log.debug("Queue: [{}], Listen, url: [{}], queueMaxNumber: [{}]", queueName, queueUrl, queueMaxNumber);

                while (!Thread.currentThread().isInterrupted()) {

                    var messageList =
                            sqsClient.receiveMessage(
                                    ReceiveMessageRequest
                                            .builder()
                                            .queueUrl(queueUrl)
                                            .maxNumberOfMessages(queueMaxNumber)/*message number*/
                                            .build()
                            );

                    if (messageList.messages().isEmpty()) {
                        log.debug("Queue: [{}], idle", queueName);
                        sleep(queueIdleSleep);
                        continue;
                    }

                    for (Message message : messageList.messages())
                        try {
                            queueExec(queueExecutor, sqsClient, message, queueUrl);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                            if (this.setting.isStopOnFail())//se ativo em caso de falha vai para o loop
                                break;
                        } finally {
                            log.debug("Queue: [{}], messageId: [{}], finished", queueName, message.messageId());
                        }
                }
            } catch (AwsServiceException | SdkClientException e) {
                throw ExceptionBuilder.of(e);
            } finally {
                log.debug("Queue:[{}] finished", this.queueName);
            }
        }
    }
}
