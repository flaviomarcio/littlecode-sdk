package com.app.business.service.impl;

import com.app.business.interfaces.DispatcherBase;
import com.app.business.model.ofservice.ProxyForwarder;
import com.app.business.model.ofservice.ProxyEventItem;
import com.littlecode.parsers.ObjectUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DispatcherKAFKA implements DispatcherBase {

    @Override
    public ProxyForwarder.Dispatcher dispatcher() {
        return ProxyForwarder.Dispatcher.KAFKA;
    }

    @Override
    public void dispatcher(ProxyForwarder forwarder, ProxyEventItem eventItem) {
        log.info(ObjectUtil.toString(eventItem));
    }
}
