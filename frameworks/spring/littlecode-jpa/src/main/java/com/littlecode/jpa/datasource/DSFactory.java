package com.littlecode.jpa.datasource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.Map;

@Slf4j
public class DSFactory {
    private final Environment environment;
    @Getter
    private final Database database;
    @Getter
    private final String[] packages;


    public DSFactory(Environment environment, Database database, String[] packages) {
        this.environment = environment;
        this.database = database;
        this.packages = packages;
        internalLog("Using database {}", "constructor", database);
        internalLog("Using packages {}", "constructor", packages);
    }

    public HibernateJpaVendorAdapter makeVendorAdapter() {
        internalLog("   makeVendorAdapter", "started");
        internalLog("       database", getDatabase().toString());
        try {
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setDatabase(getDatabase());
            return vendorAdapter;
        } finally {
            internalLog("   makeVendorAdapter", "finished");
        }
    }

    public LocalContainerEntityManagerFactoryBean makeEntityManagerFactory() {
        internalLog("makeEntityManagerFactory", "","started");
        try {
            LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
            em.setDataSource(makeDataSource());
            em.setPackagesToScan(getPackages());
            em.setJpaVendorAdapter(makeVendorAdapter());

            Map<String, Object> properties = Map.of(
                    AvailableSettings.PHYSICAL_NAMING_STRATEGY, org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy.class.getCanonicalName(),
                    AvailableSettings.IMPLICIT_NAMING_STRATEGY, org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl.class.getCanonicalName()
            );

            em.setJpaPropertyMap(properties);
            return em;
        } finally {
            internalLog("makeEntityManagerFactory", "","finished");
        }
    }

    public DataSource makeDataSource() {
        internalLog("   makeDataSource", "started");
        try {
            DriverManagerDataSource dataSource = new DriverManagerDataSource();
            var __env_dbType = this.getDatabase().name().toLowerCase();
            var __env_url = String.format("spring.datasource.%s.url", __env_dbType);
            var __env_username = String.format("spring.datasource.%s.username", __env_dbType);
            var __env_password = String.format("spring.datasource.%s.password", __env_dbType);
            var __env_schema = String.format("spring.datasource.%s.schema", __env_dbType);

            internalLog("       env", "");
            internalLog("           ", "env.dbType", __env_dbType);
            internalLog("           ", "env.url", __env_url);
            internalLog("           ", "env.username", __env_username);
            internalLog("           ", "env.password", __env_password);
            internalLog("           ", "env.schema", __env_schema);

            var __dbType = this.getDatabase().name().toLowerCase();
            var __url = environment.getProperty(String.format("spring.datasource.%s.url", __env_dbType));
            var __username = environment.getProperty(String.format("spring.datasource.%s.username", __env_dbType));
            var __password = environment.getProperty(String.format("spring.datasource.%s.password", __env_dbType));
            var __schema = environment.getProperty(String.format("spring.datasource.%s.schema", __env_dbType));

            internalLog("       value", "");
            internalLog("           ", "value.dbType ", __dbType);
            internalLog("           ", "value.url", __url);
            internalLog("           ", "value.username", __username);
            internalLog("           ", "value.password", __password);
            internalLog("           ", "value.schema", __schema);

            dataSource.setUrl(__url);
            dataSource.setUsername(__username);
            dataSource.setPassword(__password);
            dataSource.setSchema(__schema);
            return dataSource;

        } finally {
            internalLog("   makeDataSource", "finished", (Object) packages);
        }
    }

    public PlatformTransactionManager makeTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(makeEntityManagerFactory().getObject());
        return transactionManager;
    }

    private static void internalLog(String method, String subMethod, Object... args) {
        var __prefix=String.format("%s - %s",subMethod, method);
        log.debug("{} - {}", __prefix, Arrays.toString(args));
    }

    private static void internalLog(String method, String subMethod, String... args) {
        var __prefix=String.format("%s - %s",subMethod, method);
        log.debug("{} - {}", __prefix, Arrays.toString(args));
    }
}
