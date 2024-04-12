package com.littlecode.jpa.datasource;

import lombok.RequiredArgsConstructor;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@RequiredArgsConstructor
public class DSFactory{
    private final Environment environment;
    private final Database database;
    private final String[] packages;

    public Database getDatabase(){
        return database;
    }

    public String[] getPackages(){
        return this.packages;
    }

    public HibernateJpaVendorAdapter makeVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(getDatabase());
        return vendorAdapter;
    }

    public LocalContainerEntityManagerFactoryBean makeEntityManagerFactory() {
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
    }

    public DataSource makeDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        var __dbType=this.getDatabase().name().toLowerCase();
        if(this.getDatabase()!=null){
            dataSource.setUrl(environment.getProperty(String.format("spring.datasource.%s.url",__dbType)));
            dataSource.setUsername(environment.getProperty(String.format("spring.datasource.%s.username",__dbType)));
            dataSource.setPassword(environment.getProperty(String.format("spring.datasource.%s.password",__dbType)));
            dataSource.setSchema(environment.getProperty(String.format("spring.datasource.%s.schema",__dbType)));
        }
        return dataSource;
    }

    public PlatformTransactionManager makeTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(makeEntityManagerFactory().getObject());
        return transactionManager;
    }
}
