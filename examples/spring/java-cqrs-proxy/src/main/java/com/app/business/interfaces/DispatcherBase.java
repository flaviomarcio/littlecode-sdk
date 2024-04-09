package com.app.business.interfaces;

import com.app.business.model.ofservice.ProxyEventItem;
import com.app.business.model.ofservice.ProxyForwarder;

public interface DispatcherBase {

    ProxyForwarder.Dispatcher dispatcher();

    void dispatcher(ProxyForwarder forwarder, ProxyEventItem eventItem);

}
