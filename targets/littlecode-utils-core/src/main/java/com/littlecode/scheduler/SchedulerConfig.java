package com.littlecode.scheduler;

import com.littlecode.scheduler.privates.Engine;
import com.littlecode.scheduler.privates.Setting;
import com.littlecode.util.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class SchedulerConfig {


    public SchedulerConfig(ApplicationContext applicationContext, Environment environment) {

        try {
            var engine = new Engine(new Setting(applicationContext, environment),BeanUtil.of(applicationContext));
            if (engine.setting().isAutoStart())
                engine.start();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
