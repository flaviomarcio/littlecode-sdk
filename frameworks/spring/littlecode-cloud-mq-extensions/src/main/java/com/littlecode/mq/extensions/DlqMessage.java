package com.littlecode.mq.extensions;

import com.littlecode.mq.MQ;
import com.littlecode.mq.extensions.privates.CustomMQSender;
import com.littlecode.parsers.ExceptionBuilder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DlqMessage extends CustomMQSender{
    public DlqMessage(MQ mq) {
        super(mq);
    }

    public DlqMessage(String propertiesPrefix, MQ mq){
        super(propertiesPrefix, mq);
    }

    public boolean send(Object payload) {
        if(payload ==null)
            throw ExceptionBuilder.ofNullPointer("Invalid payload");
        if (payload instanceof Payload value)
            return super.send(value);
        return super.send(new Payload(payload));
    }

    @Getter
    public static class Payload {
        private final Object data;
        public Payload(Object data) {
            this.data=data;
        }
    }
}