package com.littlecode.scheduler;

import com.littlecode.util.EnvironmentUtil;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class SchedulerSetting {
    private static final String env_log = "littlecode.scheduler.log";
    private static final String env_auto_start = "littlecode.scheduler.auto-start";

    private final boolean log;
    private final boolean autoStart;

    public SchedulerSetting() {
        var envUtil = new EnvironmentUtil();
        this.log = envUtil.asBool(env_log, false);
        this.autoStart = envUtil.asBool(env_auto_start, true);
    }

}
