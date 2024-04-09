package com.app.business.service.event;

import com.app.business.dto.EventIn;
import com.littlecode.mq.MQ;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventConsumerService {
    private final EventIngesterService eventIngesterService;


    public void register(MQ.Message.Task task) {
        if (task == null) {
            log.error("%{}, task is null", MQ.Message.Task.class);
            return;
        }
        log.info("msg:[{}], type:[{}]", task.getMessageId(), task.getTypeName());
        if (!task.canType(EventIn.class)) {
            log.error("[{}]: incompatible type: [{}]==>[{}]", task.getClass(), task.getTypeName(), EventIn.class);
            return;
        }

        var eventIn = task.asObject(EventIn.class);
        if (eventIn == null) {
            log.error("[{}]:, invalid cast: [{}]==>[{}]", task.getClass(), task.getTypeName(), EventIn.class);
            return;
        }

        eventIngesterService.register(eventIn);
    }

}
