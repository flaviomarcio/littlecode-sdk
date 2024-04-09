package com.app.business.service;

import com.app.business.config.AppConfig;
import com.app.business.dto.CommandDTO;
import com.app.business.dto.ItemModelDTO;
import com.littlecode.containers.ObjectReturn;
import com.littlecode.mq.MQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {
    private final MQ mq;
    private final AppConfig appConfig;
    private final CRUDService crudService;

    public ObjectReturn insert(CommandDTO in) {
        if (in == null)
            return ObjectReturn.BadRequest(CommandDTO.class);
        in.setAction(CommandDTO.Action.INSERT);
        return this.callBack(this.proxy(in));
    }

    public ObjectReturn update(CommandDTO in) {
        if (in == null)
            return ObjectReturn.BadRequest(CommandDTO.class);
        in.setAction(CommandDTO.Action.INSERT);
        return this.callBack(this.proxy(in));
    }

    public ObjectReturn delete(CommandDTO in) {
        if (in == null)
            return ObjectReturn.BadRequest(CommandDTO.class);
        in.setAction(CommandDTO.Action.DELETE);
        return this.callBack(this.proxy(in));
    }

    public ObjectReturn register(CommandDTO in) {
        if (in == null) {
            return ObjectReturn.BadRequest("%{} is null", CommandDTO.class);
        }
        return this.callBack(this.proxy(in));
    }

    public ObjectReturn register(MQ.Message.Task task) {
        if (task == null) {
            return ObjectReturn.BadRequest("%{}, task is null", MQ.Message.Task.class);
        }
        log.info("msg:[{}], type:[{}]", task.getMessageId(), task.getTypeName());
        if (!task.canType(CommandDTO.class)) {
            return ObjectReturn.BadRequest("[{}]: incompatible type: [{}]==>[{}]", task.getClass(), task.getTypeName(), CommandDTO.class);
        }

        var in = task.asObject(CommandDTO.class);
        if (in == null) {
            return ObjectReturn.BadRequest("[{}]:, invalid cast: [{}]==>[{}]", task.getClass(), task.getTypeName(), CommandDTO.class);
        }

        return this.callBack(this.proxy(in));
    }

    public ObjectReturn callBack(ObjectReturn objectReturn) {
        if (!appConfig.isCallBackEnabled())
            return objectReturn;
        mq.setting().setClientId(appConfig.getCallBackClientId());
        mq.setting().setClientSecret(appConfig.getCallBackClientSecret());
        mq.setting().setAutoCreate(appConfig.isCallBackQueueAutoCreate());
        mq
                .queueName(appConfig.getCallBackQueue())
                .dispatcher(objectReturn);
        return objectReturn;
    }

    private ObjectReturn proxy(CommandDTO in) {
        if (in == null)
            return ObjectReturn.BadRequest(CommandDTO.class);

        if (in.getTarget() == null)
            return ObjectReturn.BadRequest(ItemModelDTO.class);

        var target = in.getTarget();

        return switch (in.getAction()) {
            case INSERT, UPDATE, UPSERT -> crudService.saveIn(target);
            case DELETE -> crudService.delete(target);
            default ->
                    ObjectReturn.BadRequest("Invalid proxy command: %s, object: %s", in.getAction(), target.getClass());
        };
    }


}
