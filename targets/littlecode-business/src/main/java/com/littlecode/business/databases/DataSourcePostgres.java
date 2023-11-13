package com.littlecode.business.databases;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SuppressWarnings("unused")
//@Configuration
@EnableJpaRepositories(
        basePackages = DataSourceBase.BASE_PACKAGES_DEFAULT_POSTGRES,
        entityManagerFactoryRef = DataSourceBase.POSTGRESQL + DataSourceBase.METHOD_MANAGER_FACTORY,
        transactionManagerRef = DataSourceBase.POSTGRESQL + DataSourceBase.METHOD_TRANSACTION_MANAGER)
public class DataSourcePostgres extends DataSourceBase {
    private static final String DB = DataSourceBase.POSTGRESQL;

    public DataSourcePostgres() {
        super(DB);
    }

    @Bean(name = DB + METHOD_MANAGER_FACTORY)
    @Lazy
    public LocalContainerEntityManagerFactoryBean managerFactory() {
        return super.managerFactory();
    }

    @Bean(name = DB + METHOD_DATA_SOURCE)
    @Lazy
    public DataSource dataSource() {
        return super.dataSource();
    }

    @Bean(name = DB + METHOD_TRANSACTION_MANAGER)
    @Lazy
    public PlatformTransactionManager transactionManager() {
        return super.transactionManager();
    }
}