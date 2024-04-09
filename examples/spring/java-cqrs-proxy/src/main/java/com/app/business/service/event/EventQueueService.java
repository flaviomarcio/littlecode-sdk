package com.app.business.service.event;

import com.app.business.model.ofservice.ProxyEvent;
import com.littlecode.containers.ObjectReturn;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventQueueService {


    public ObjectReturn send(ProxyEvent event) {
        return ObjectReturn.Empty();
    }

}
