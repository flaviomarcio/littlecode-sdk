package com.app.business.service;

import com.app.business.config.AppConfig;
import com.app.business.dto.TaskIn;
import com.app.business.dto.TaskOut;
import com.littlecode.containers.ObjectReturn;
import com.littlecode.mq.MQ;
import com.littlecode.parsers.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyService {
    private final AppConfig appConfig;
    private final MQ mq;

    private ObjectReturn dispatcher(Object payload) {
        if (!appConfig.isAppProxyEnabled())
            return ObjectReturn.Fail("dispatcher disabled");

        var task = TaskIn
                .builder()
                .id(UUID.randomUUID())
                .dt(LocalDateTime.now())
                .checksum(ObjectUtil.toMd5Uuid(payload))
                .state(appConfig.getAppProxyStateSent())
                .payload(payload)
                .build();

        mq.dispatcher(task);
        return ObjectReturn.of(
                TaskOut.builder()
                        .id(task.getId())
                        .dt(task.getDt())
                        .state(task.getState())
                        .checksum(task.getChecksum())
                        .build()
        );
    }

    public ObjectReturn proxy(Object payload) {
        if (payload == null)
            return ObjectReturn.BadRequest("Invalid object, payload is null");
        return this.dispatcher(payload);
    }
}
