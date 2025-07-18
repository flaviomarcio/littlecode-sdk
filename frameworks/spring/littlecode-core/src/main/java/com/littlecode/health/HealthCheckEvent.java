package com.littlecode.health;

import com.littlecode.parsers.DateUtil;
import com.littlecode.parsers.PrimitiveUtil;
import org.springframework.boot.actuate.health.Health;

import java.time.Duration;
import java.time.LocalDateTime;

public class HealthCheckEvent {
    private final HealthCheckConfig config;

    public HealthCheckEvent(HealthCheckConfig config) {
        this.config = config;
    }

    public Health createHealth() {
        if (config.isStopOnFail()) {
            if (!PrimitiveUtil.isEmpty(HealthCheckStatics.staticFailMessage))
                return Health.down().withDetail("Service fail", HealthCheckStatics.staticFailMessage).build();
        }

        if (config.isStopOnLimitExecution() && HealthCheckStatics.staticStartExecution != null) {
            var duration = DateUtil.toDuration(config.getStopOnLimitDuration());
            if (!Duration.ZERO.equals(duration)) {
                var limiteDt = LocalDateTime
                        .now()
                        .minusSeconds(duration.toSeconds());
                if (HealthCheckStatics.staticStartExecution.isBefore(limiteDt))
                    return Health.down().withDetail("Service Idle", "Limite idle service exceeded").build();
            }
        }

        if (config.isStopOnIdle() && HealthCheckStatics.staticLastExecution != null) {
            var duration = DateUtil.toDuration(config.getStopOnIdleDuration());
            if (!Duration.ZERO.equals(duration)) {
                var limiteDt = LocalDateTime
                        .now()
                        .minusSeconds(duration.toSeconds());
                if (HealthCheckStatics.staticLastExecution.isBefore(limiteDt))
                    return Health.down().withDetail("Service Idle", "Limite idle service exceeded").build();
            }
        }
        return null;
    }
}