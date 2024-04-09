package com.app.business.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {
    @Value("${app.title:NoTitle}")
    private String title;
    @Value("${app.className:NoClass}")
    private String className;
    @Value("${app.callBack.enabled:false}")
    private boolean callBackEnabled;
    @Value("${app.callBack.queue:NoQueueName}")
    private String callBackQueue;
    @Value("${app.callBack.queue:false}")
    private boolean callBackQueueAutoCreate;
    @Value("${app.callBack.client.id:}")
    private String callBackClientId;
    @Value("${app.callBack.client.secret:}")
    private String callBackClientSecret;
}
