package com.littlecode.setup.privates;

import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.setup.Setup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class SetupConfig {
    private static ApplicationContext STATIC_APPLICATION_CONTEXT;
    private static Environment STATIC_ENVIRONMENT;
    private final ApplicationContext context;
    private final Environment environment;

    public SetupConfig(ApplicationContext context, Environment environment) {
        this.context = context;
        this.environment = environment;
        configure(context, environment);
    }

    @Bean
    public ApplicationContext getContext() {
        if (this.context == null)
            return STATIC_APPLICATION_CONTEXT;
        return this.context;
    }

    @Bean
    public Environment getEnvironment() {
        if (this.environment == null)
            return STATIC_ENVIRONMENT;
        return this.environment;
    }

    private void configure(ApplicationContext context, Environment environment) {
        if (STATIC_APPLICATION_CONTEXT == null)
            STATIC_APPLICATION_CONTEXT = context;
        if (STATIC_ENVIRONMENT == null)
            STATIC_ENVIRONMENT = environment;
    }

    public <T> T getBean(String beanName, Class<T> valueType) {
        try {
            return STATIC_APPLICATION_CONTEXT.getBean(beanName, valueType);
        } catch (BeansException ignore) {
            return null;
        }
    }

    public String readEnv(String env) {
        try {
            var response = STATIC_ENVIRONMENT.getProperty(env);
            return response == null ? "" : response.trim();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean readEnvBool(String env, Boolean defaultValue) {
        return Boolean.parseBoolean(this.readEnv(env, defaultValue.toString()));
    }

    public String readEnv(String env, String defaultValue) {
        try {
            var response = this.readEnv(env);
            return response.isEmpty() ? defaultValue : response;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public <T> List<T> readEnvEnums(String env, Class<?> enumClass) {
        return readEnvEnums(env, enumClass, null);
    }


    public <T> List<T> readEnvEnums(String env, Class<?> enumClass, List<T> defaultValue) {
        if (enumClass == null)
            throw ExceptionBuilder.ofFrameWork("Invalid enum: enum is null");
        if (!enumClass.isEnum())
            throw ExceptionBuilder.ofFrameWork("Invalid enum: %s is not enum type", enumClass.getName());

        var enumList = enumClass.getEnumConstants();

        List<T> __return = new ArrayList<>();
        try {
            var values = STATIC_ENVIRONMENT.getProperty(env, "").split(",");
            for (String s : values) {
                if (s == null || s.trim().isEmpty())
                    continue;
                for (var e : enumList) {
                    var eName = e.toString();
                    if (!eName.equalsIgnoreCase(s))
                        continue;
                    //noinspection unchecked
                    __return.add((T) e);
                }
            }
        } catch (Exception ignored) {
        }
        return !__return.isEmpty()
                ? __return
                :
                defaultValue == null
                        ? new ArrayList<>()
                        : defaultValue;
    }

    public List<String> readEnvList(String env) {
        try {
            var values = List.of(STATIC_ENVIRONMENT.getProperty(env, "").split(","));
            List<String> out = new ArrayList<>();
            values.forEach(s -> {
                if (s != null && !s.trim().isEmpty())
                    out.add(s);
            });
            return out;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<String> readEnvList(String env, List<String> defaultValue) {
        var response = this.readEnvList(env);
        if (response == null || response.isEmpty())
            return defaultValue == null ? new ArrayList<>() : defaultValue;
        return response;
    }

    public Connection createConnection(Setup.ExecutorDataBase executorDataBase) {
        try {
            var datasourceUrl = this.readEnv("spring.datasource.url");
            var datasourceUsername = this.readEnv("spring.datasource.username");
            var datasourcePassword = this.readEnv("spring.datasource.password");

            if (datasourceUrl == null) {
                datasourceUrl = this.readEnv("service.datasource.url");
                datasourceUsername = this.readEnv("service.datasource.username");
                datasourcePassword = this.readEnv("service.datasource.password");
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
        var schemaName = this.readEnv("spring.jpa.properties.hibernate.default_schema").trim();
        if (schemaName.trim().isEmpty())
            schemaName = this.readEnv("spring.datasource.schema").trim();
        if (schemaName.trim().isEmpty())
            schemaName = this.readEnv("service.datasource.schema").trim();
        return schemaName;
    }

}
