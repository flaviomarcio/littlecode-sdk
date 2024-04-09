package com.app.business.service.event;

import com.app.business.dto.EventIn;
import com.app.business.model.ofservice.ProxyEvent;
import com.littlecode.containers.ObjectReturn;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventIngesterService {
    private final EventQueueService eventQueueService;
    private final EventService eventService;

    public ObjectReturn register(EventIn eventIn) {
        var __return = eventService.register(eventIn);
        if (!__return.isOK())
            return __return;
        return eventQueueService.send(__return.cast(ProxyEvent.class));
    }
}
