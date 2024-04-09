package com.app.business.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${app.proxy.state.sent}")
    private boolean appProxyEnabled;
    @Value("${app.proxy.enabled}")
    private String appProxyStateSent;
}
