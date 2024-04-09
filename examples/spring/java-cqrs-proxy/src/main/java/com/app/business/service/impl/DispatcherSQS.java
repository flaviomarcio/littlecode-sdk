package com.app.business.service.impl;

import com.app.business.interfaces.DispatcherBase;
import com.app.business.model.ofservice.ProxyEventItem;
import com.app.business.model.ofservice.ProxyForwarder;
import com.littlecode.parsers.ObjectUtil;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DispatcherSQS implements DispatcherBase {

    @Override
    public ProxyForwarder.Dispatcher dispatcher() {
        return ProxyForwarder.Dispatcher.SQS;
    }

    @Override
    public void dispatcher(ProxyForwarder forwarder, ProxyEventItem eventItem) {
        log.info(ObjectUtil.toString(eventItem));
    }
}
