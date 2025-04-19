package com.littlecode.health;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckConfig {
    private boolean stopOnFail;
    private boolean stopOnIdle;
    private String stopOnIdleDuration;
    private boolean stopOnLimitExecution;
    private String stopOnLimitDuration;
}
