package com.littlecode.tests;

import com.littlecode.setup.Setup;
import com.littlecode.setup.SetupSetting;
import com.littlecode.setup.privates.SetupConfig;
import com.littlecode.tests.db.HikariConnectionPool;
import lombok.Getter;
import org.mockito.Mockito;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.vendor.Database;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Getter
public class Factory {
    private ApplicationContext mockContext;
    private Environment mockEnvironment;
    private SetupConfig mockConfig;
    private SetupSetting mockSetting;
    private Setup setup;

    public Factory() {
        mockStaticEnvironment();
        mockStaticContext();
        mockStaticSetting();
    }


    private void mockStaticContext() {
        mockContext = Mockito.mock(ApplicationContext.class);

        Mockito.when(mockContext.getBean(Setup.STP_CONFIGURE_BEAN_CONTEXT)).thenReturn(mockContext);
        Mockito.when(mockContext.getBean(Setup.STP_CONFIGURE_BEAN_ENVIRONMENT)).thenReturn(mockEnvironment);

        Mockito.when(mockContext.getBean(Setup.STP_CONFIGURE_BEAN_NOTIFY, Setup.ExecutorNotify.class)).thenReturn(
                Setup.ExecutorNotify
                        .builder()
                        .prepare(target -> {
                        })
                        .fail(() -> {
                        })
                        .successful(() -> {
                        })
                        .started(() -> {
                        })
                        .finished(() -> {
                        })
                        .build()
        );
        Mockito.when(mockContext.getBean(Setup.STP_CONFIGURE_BEAN_DATABASE, Setup.ExecutorDataBase.class)).thenReturn(
                Setup.ExecutorDataBase
                        .builder()
                        .before(() -> {
                        })
                        .sourceExecute(statementItem -> {
                        })
                        .connection(HikariConnectionPool::getConnection)
                        .sourceParser(s -> s)
                        .after(() -> {
                        })
                        .build()
        );
        Mockito.when(mockContext.getBean(Setup.STP_CONFIGURE_BEAN_BUSINESS, Setup.ExecutorBusiness.class)).thenReturn(
                Setup.ExecutorBusiness
                        .builder()
                        .before(() -> {
                        })
                        .executor(() -> null)
                        .after(() -> {
                        })
                        .build()
        );

    }

    private void mockStaticEnvironment() {

        Supplier<String> getDatabaseList = new Supplier<String>() {
            @Override
            public String get() {
                return Stream.of(Database.values())
                        .map(Object::toString)
                        .reduce((obj1, obj2) -> obj1 + "," + obj2)
                        .orElse("");
            }
        };


        mockEnvironment = Mockito.mock(Environment.class);
        var envList = List.of(
                Map.of(
                        "spring.application.name", "app-test",
                        "spring.datasource.url", "jdbc:h2:mem:test",
                        "spring.datasource.username", "user",
                        "spring.datasource.password", "password",

                        "service.datasource.url", "jdbc:h2:mem:test",
                        "service.datasource.username", "user",
                        "service.datasource.password", "password",

                        "spring.jpa.properties.hibernate.default_schema", "schema_test",
                        "spring.datasource.schema", "schema_test",
                        "service.datasource.schema", "schema_test"
                ),
                Map.of(
                        SetupSetting.env_auto_start, "true",
                        "spring.application.name", "app-test"
                ),
                Map.of(
                        SetupSetting.env_business_auto_start, "true",
                        SetupSetting.env_database_auto_apply, "true"
                ),
                Map.of(
                        SetupSetting.env_database_target_auto_start, "true",
                        SetupSetting.env_database_target_databases, getDatabaseList.get(),
                        SetupSetting.env_database_target_tags, "",
                        SetupSetting.env_database_target_auto_update, "true",
                        SetupSetting.env_database_target_model_scan, "true"
                ),
                Map.of(
                        SetupSetting.env_database_auto_start, "true",
                        SetupSetting.env_database_ddl_exporter_dir, "",
                        SetupSetting.env_database_ddl_package_names, "com.littlecode.tests.db",
                        SetupSetting.env_database_ddl_package_auto_scan, "true",
                        SetupSetting.env_database_ddl_auto_save, "true"
                )
        );
        envList.forEach(envs -> {
            envs.forEach((env, value) -> {
                Mockito.when(mockEnvironment.getProperty(env)).thenReturn(value);
                Mockito.when(mockEnvironment.getProperty(env, "")).thenReturn(value);
            });
        });
    }

    private void mockStaticSetting() {
        mockConfig = new SetupConfig(mockContext, mockEnvironment);
        mockSetting = new SetupSetting(mockConfig);
        setup = new Setup(mockSetting);
    }
}
