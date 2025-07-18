package com.littlecode.health;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

@Slf4j
public class HealthCheck {

    public static void reset() {
        HealthCheckStatics.staticStartExecution = null;
        HealthCheckStatics.staticLastExecution = null;
        HealthCheckStatics.staticFailMessage = null;
    }

    public static void setStaticStartExecution(LocalDateTime staticStartExecution) {
        HealthCheckStatics.staticStartExecution = staticStartExecution;
    }

    public static void setStaticLastExecution(LocalDateTime staticLastExecution) {
        HealthCheckStatics.staticLastExecution = staticLastExecution;
    }

    public static void executionNotify() {
        HealthCheckStatics.staticLastExecution = LocalDateTime.now();
    }

    public static void failNotify(String message) {
        HealthCheckStatics.staticFailMessage = message;
    }

    public static HealthCheckIndicator createHealthIndicator(HealthCheckConfig config) {
        return new HealthCheckIndicator(config);
    }

    public static HealthCheckIndicator createHealthIndicator(HealthCheckConfig config, HealthCheckEvent event) {
        return new HealthCheckIndicator(config, event);
    }


}