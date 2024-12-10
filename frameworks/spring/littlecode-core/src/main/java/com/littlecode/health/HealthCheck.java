package com.littlecode.health;

import com.littlecode.parsers.DateUtil;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;

import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
public class HealthCheck {

    private static LocalDateTime staticStartExecution=LocalDateTime.now();
    private static LocalDateTime staticLastExecution;
    private static String staticFailMessage;

    public static void reset(){
        staticStartExecution=null;
        staticLastExecution=null;
        staticFailMessage=null;
    }

    public static void setStaticStartExecution(LocalDateTime staticStartExecution) {
        HealthCheck.staticStartExecution = staticStartExecution;
    }

    public static void setStaticLastExecution(LocalDateTime staticLastExecution) {
        HealthCheck.staticLastExecution = staticLastExecution;
    }

    public static void executionNotify() {
        staticLastExecution = LocalDateTime.now();
    }

    public static void failNotify(String message) {
        staticFailMessage = message;
    }

    public static Indicator createHealthIndicator(Config config) {
        return new Indicator(config);
    }

    public static Indicator createHealthIndicator(Config config, Indicator.Event event) {
        return new Indicator(config,event);
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Config {
        private boolean stopOnFail;
        private boolean stopOnIdle;
        private String stopOnIdleDuration;
        private boolean stopOnLimitExecution;
        private String stopOnLimitDuration;
    }

    @Slf4j
    public static class Indicator implements HealthIndicator {
        @Getter
        private final Event event;
        private final Config config;

        public Indicator(Config config) {
            this.config = config;
            this.event = new Indicator.Event(this.config);
        }

        public Indicator(Config config, Event event) {
            this.config = config;
            this.event = event;
        }

        @Override
        public Health health() {
            var health = event.createHealth();
            return health == null ? Health.up().build() : health;
        }

        public static class Event {
            private final Config config;
            public Event(Config config) {
                this.config=config;
            }
            public Health createHealth() {
                if (config.isStopOnFail()){
                    if(!PrimitiveUtil.isEmpty(staticFailMessage))
                        return Health.down().withDetail("Service fail", staticFailMessage).build();
                }

                if (config.isStopOnLimitExecution() && staticStartExecution!=null) {
                    var duration= DateUtil.toDuration(config.getStopOnLimitDuration());
                    if (!Duration.ZERO.equals(duration)) {
                        var limiteDt = LocalDateTime
                                .now()
                                .minusSeconds(duration.toSeconds());
                        if (staticStartExecution.isBefore(limiteDt))
                            return Health.down().withDetail("Service Idle", "Limite idle service exceeded").build();
                    }
                }

                if (config.isStopOnIdle() && staticLastExecution!=null) {
                    var duration= DateUtil.toDuration(config.getStopOnIdleDuration());
                    if (!Duration.ZERO.equals(duration)) {
                        var limiteDt = LocalDateTime
                                .now()
                                .minusSeconds(duration.toSeconds());
                        if (staticLastExecution.isBefore(limiteDt))
                            return Health.down().withDetail("Service Idle", "Limite idle service exceeded").build();
                    }
                }
                return null;
            }
        }
    }
}