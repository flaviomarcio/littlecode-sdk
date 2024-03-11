package com.littlecode.setup.privates;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.setup.Setup;
import com.littlecode.util.EnvironmentUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Slf4j
public class SetupConfig {
    private final ApplicationContext context;
    private final Environment environment;

    @Getter
    private final EnvironmentUtil environmentUtil;

    public SetupConfig(ApplicationContext context, Environment environment) {
        this.context = context;
        this.environment = environment;
        this.environmentUtil = new EnvironmentUtil(environment);
    }

    @Bean
    public ApplicationContext getContext() {
        if (this.context == null)
            return UtilCoreConfig.getApplicationContext();
        return this.context;
    }

    @Bean
    public Environment getEnvironment() {
        if (this.environment == null)
            return UtilCoreConfig.getEnvironment();
        return this.environment;
    }

    public Connection createConnection(Setup.ExecutorDataBase executorDataBase) {
        try {
            var datasourceUrl = environmentUtil.asString("spring.datasource.url");
            var datasourceUsername = environmentUtil.asString("spring.datasource.username");
            var datasourcePassword = environmentUtil.asString("spring.datasource.password");

            if (datasourceUrl == null) {
                datasourceUrl = environmentUtil.asString("service.datasource.url");
                datasourceUsername = environmentUtil.asString("service.datasource.username");
                datasourcePassword = environmentUtil.asString("service.datasource.password");
            }

            return (executorDataBase.connection != null)
                    ? executorDataBase.connection.call()
                    : DriverManager.getConnection(datasourceUrl, datasourceUsername, datasourcePassword);
        } catch (SQLException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public String getDefaultSchema() {
        var schemaName = environmentUtil.asString("spring.jpa.properties.hibernate.default_schema");
        if (schemaName.trim().isEmpty())
            schemaName = environmentUtil.asString("spring.datasource.schema");
        if (schemaName.trim().isEmpty())
            schemaName = environmentUtil.asString("service.datasource.schema");
        return schemaName;
    }

}
