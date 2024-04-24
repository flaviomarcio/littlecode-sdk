package com.littlecode.jpa.datasource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DSFactory {
    private final Environment environment;
    @Getter
    private final Database database;
    @Getter
    private final String[] packages;

    private final String __ds_base_path;

    public DSFactory(final Environment environment, final Database database, final String[] packages) {
        this.environment = environment;
        this.database = database;
        this.packages = packages;
        this.__ds_base_path=String.format("spring.datasource.%s", database.name().toLowerCase());
    }

    public HibernateJpaVendorAdapter makeVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(getDatabase());
        return vendorAdapter;
    }

    public LocalContainerEntityManagerFactoryBean makeEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setJpaVendorAdapter(makeVendorAdapter());
        em.setPackagesToScan(getPackages());
        em.setDataSource(makeDataSource());
        em.setJpaPropertyMap(__make_default_properties());
        em.afterPropertiesSet();
        return em;
    }

    public DataSource makeDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        var __url = __property__get("url");
        var __username = __property__get("username");
        var __password = __property__get("password");
        var __schema = __property__get("schema");

        dataSource.setUrl(__url);
        dataSource.setUsername(__username);
        dataSource.setPassword(__password);
        dataSource.setSchema(__schema);
        return dataSource;

    }

    public PlatformTransactionManager makeTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(makeEntityManagerFactory().getObject());
        return transactionManager;
    }

    private void __property__add(final Map<String,String> properties, String property){
        __property__add(properties, property, null);
    }

    private void __property__add(final Map<String,String> properties, final String property, String defaultValue){
        var __ds_value = __property__get(property).trim();
        if(__ds_value.isEmpty() && defaultValue!=null)
            __ds_value=defaultValue;
        if(!__ds_value.trim().isEmpty())
            properties.put(property, __ds_value);
    }

    private String __property__get(String property){
        property = String.format("%s.%s", __ds_base_path, property);
        final var __ds_value = environment.getProperty(property,"");
        return __ds_value==null?"":__ds_value;
    }

    private Map<String,String> __make_default_properties() {
        Map<String,String> properties=new HashMap<>();
        var hibernate_envs= List.of(AvailableSettings.DIALECT,AvailableSettings.SHOW_SQL,AvailableSettings.FORMAT_SQL,AvailableSettings.USE_SQL_COMMENTS,AvailableSettings.PHYSICAL_NAMING_STRATEGY,AvailableSettings.IMPLICIT_NAMING_STRATEGY);
        for(var env: hibernate_envs)
            __property__add(properties, env);

        if(!properties.containsKey(AvailableSettings.PHYSICAL_NAMING_STRATEGY))
            __property__add(properties, AvailableSettings.PHYSICAL_NAMING_STRATEGY, CamelCaseToUnderscoresNamingStrategy.class.getCanonicalName());
        if(!properties.containsKey(AvailableSettings.IMPLICIT_NAMING_STRATEGY))
            __property__add(properties, AvailableSettings.IMPLICIT_NAMING_STRATEGY, SpringImplicitNamingStrategy.class.getCanonicalName());

        //PhysicalNamingStrategyStandardImpl.INSTANCE.

        return properties;
    }
}
