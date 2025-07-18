package com.littlecode.health;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

@Slf4j
public class HealthCheckIndicator implements HealthIndicator {
    @Getter
    private final HealthCheckEvent event;
    private final HealthCheckConfig config;

    public HealthCheckIndicator(HealthCheckConfig config) {
        this.config = config;
        this.event = new HealthCheckEvent(this.config);
    }

    public HealthCheckIndicator(HealthCheckConfig config, HealthCheckEvent event) {
        this.config = config;
        this.event = event;
    }

    @Override
    public Health health() {
        var health = event.createHealth();
        return health == null ? Health.up().build() : health;
    }
}

