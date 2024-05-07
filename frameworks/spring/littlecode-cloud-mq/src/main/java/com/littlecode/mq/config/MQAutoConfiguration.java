package com.littlecode.mq.config;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.mq.MQ;
import com.littlecode.util.EnvironmentUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Data
@Slf4j
@Configuration
@AutoConfiguration
public class MQAutoConfiguration {

    private boolean autoStart;

    public MQAutoConfiguration(ApplicationContext applicationContext, Environment environment) {
        UtilCoreConfig.setApplicationContext(applicationContext);
        UtilCoreConfig.setEnvironment(environment);
        var envUtil=new EnvironmentUtil();
        this.autoStart=envUtil.asBool("littlecode.mq.auto-start",false);
        start();
    }

    public void start(){
        String logPrefix = this.getClass().getName() + ": ";
        log.info("{}: started", logPrefix);
        try{
            if(!autoStart)
                log.info("{}: skipped", logPrefix);
            else{
                log.info("{}: executing", logPrefix);
                var mq = new MQ();
                mq.listen();
                log.info("{}: executed", logPrefix);
            }
        }finally {
            log.info("{}: finished", logPrefix);
        }
    }
}
