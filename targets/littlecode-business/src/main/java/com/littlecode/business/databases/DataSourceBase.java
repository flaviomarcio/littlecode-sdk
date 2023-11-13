package com.littlecode.business.databases;

import com.littlecode.parsers.ExceptionBuilder;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

public class DataSourceBase {
    @Autowired
    private Environment environment;
    //database
    public static final String H2 = "h2";
    public static final String POSTGRESQL = "postgresql";
    public static final String SQLSERVER = "sqlserver";
    public static final String ORACLE = "oracle";
    public static final String MYSQL = "mysql";

    //Method
    public static final String METHOD_MANAGER_FACTORY = "ManagerFactory";
    public static final String METHOD_DATA_SOURCE = "DataSource";
    public static final String METHOD_TRANSACTION_MANAGER = "TransactionManager";
    //packages
    public static final String BASE_PACKAGES_DEFAULT_BUSINESS = "com.app.business.";
    private static final String HB_BASE ="spring.jpa.properties.hibernate";
    public static final String BASE_PACKAGES_DEFAULT_MODEL = BASE_PACKAGES_DEFAULT_BUSINESS + "model.";
    public static final String BASE_PACKAGES_DEFAULT_REPOSITORY = BASE_PACKAGES_DEFAULT_BUSINESS + "repository.";

    public static final String BASE_PACKAGES_DEFAULT_H2 = BASE_PACKAGES_DEFAULT_REPOSITORY + H2;
    public static final String BASE_PACKAGES_DEFAULT_POSTGRES = BASE_PACKAGES_DEFAULT_REPOSITORY + POSTGRESQL;
    public static final String BASE_PACKAGES_DEFAULT_ORACLE = BASE_PACKAGES_DEFAULT_REPOSITORY + ORACLE;
    public static final String BASE_PACKAGES_DEFAULT_MYSQL = BASE_PACKAGES_DEFAULT_REPOSITORY + MYSQL;
    public static final String BASE_PACKAGES_DEFAULT_SQLSERVER = BASE_PACKAGES_DEFAULT_REPOSITORY + SQLSERVER;
    //hibernate
    public static final String HIBERNATE_SETTING = "hibernate.";
    public static final String HIBERNATE_SETTING_IMPLICIT_NAMING_STRATEGY = HIBERNATE_SETTING+"implicit_naming_strategy";
    public static final String HIBERNATE_SETTING_PHYSICAL_NAMING_STRATEGY = HIBERNATE_SETTING+"physical_naming_strategy";
    public static final String HIBERNATE_SETTING_HBM_2_DDL_AUTO = HIBERNATE_SETTING+"hbm2ddl.auto";
    public static final String HIBERNATE_SETTING_FORMAT_SQL = HIBERNATE_SETTING+"format_sql";
    public static final String HIBERNATE_SETTING_USE_SQL_COMMENTS = HIBERNATE_SETTING+"use_sql_comments";
    public static final String HIBERNATE_SETTING_SHOW_SQL = HIBERNATE_SETTING+"show_sql";
    public static final String HIBERNATE_SETTING_IMPLICIT_NAMING_STRATEGY_VALUE = org.hibernate.boot.model.naming.ImplicitNamingStrategyLegacyHbmImpl.class.getName();
    public static final String HIBERNATE_SETTING_PHYSICAL_NAMING_STRATEGY_VALUE = org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy.class.getName();
    //setting
    public static final String DS_BASE = "spring.datasource.";
    public static final String DS_H2 = DS_BASE+H2;
    public static final String DS_MYSQL = DS_BASE+MYSQL;
    public static final String DS_POSTGRES = DS_BASE+POSTGRESQL;
    public static final String DS_SQLSERVER = DS_BASE+SQLSERVER;
    public static final String DS_ORACLE = DS_BASE+ORACLE;
    private final String dbType;

    public String getDbType(){
        return PrimitiveUtil.isEmpty(this.dbType)?"":this.dbType;
    }

    public DataSourceBase(String dbType) {
        this.dbType = dbType;
    }

    public DataSourceEnv getDataSourceEnv() {
        return new DataSourceEnv(this);
    }

    public LocalContainerEntityManagerFactoryBean managerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(this.getDataSourceEnv().getPackageModel());
        em.setJpaVendorAdapter(vendorAdapter());
        em.setJpaProperties(additionalProperties());
        return em;
    }

    public DataSource dataSource() {
        var dataSourceEnv=this.getDataSourceEnv();
        if(dataSourceEnv==null)
            throw ExceptionBuilder.ofNoImplemented(DataSourceEnv.class);

        var setting = dataSourceEnv.getSetting();
        if(setting==null)
            throw ExceptionBuilder.ofNoImplemented(DataSourceSetting.class);

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        if (setting.getLoginTimeout() != 0) {
            try {
                dataSource.setLoginTimeout(setting.getLoginTimeout());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        if (!PrimitiveUtil.isEmpty(setting.getDriverClassName()))
            dataSource.setDriverClassName(setting.getDriverClassName());
        if (!PrimitiveUtil.isEmpty(setting.getCatalog()))
            dataSource.setCatalog(setting.getCatalog());
        if (!PrimitiveUtil.isEmpty(setting.getUrl()))
            dataSource.setUrl(setting.getUrl());
        if (!PrimitiveUtil.isEmpty(setting.getUsername()))
            dataSource.setUsername(setting.getUsername());
        if (!PrimitiveUtil.isEmpty(setting.getPassword()))
            dataSource.setPassword(setting.getPassword());
        if (!PrimitiveUtil.isEmpty(setting.getSchema()))
            dataSource.setSchema(setting.getSchema());
        return dataSource;
    }

    public HibernateJpaVendorAdapter vendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setShowSql(false);
        vendorAdapter.setGenerateDdl(true);
        vendorAdapter.setDatabase(Database.valueOf(this.getDataSourceEnv().getDbType().toUpperCase()));
        return vendorAdapter;
    }

    public Properties additionalProperties() {
        Properties properties = new Properties();
        var hibernateSettings = this.getDataSourceEnv().getSetting().hibernateSettings();
        properties.putAll(hibernateSettings);
        return properties;
    }

    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(managerFactory().getObject());
        return transactionManager;
    }

    @Builder
    @Data
    @AllArgsConstructor
    public static class DataSourceEnv {
        private final DataSourceBase dataSourceBase;
        private final DataSourceSetting setting;
        private String dbType;
        private String packageModel;
        private String dataSourceSetting;

        public DataSourceEnv(DataSourceBase dataSourceBase) {
            this.dataSourceBase = dataSourceBase;

            if (dataSourceBase == null)
                throw ExceptionBuilder.ofNoImplemented(DataSourceBase.class);

            this.dbType = dataSourceBase.getDbType();


            if (PrimitiveUtil.isEmpty(this.dataSourceSetting))
                this.dataSourceSetting = DS_BASE + dbType;
            if (PrimitiveUtil.isEmpty(this.packageModel))
                this.packageModel = BASE_PACKAGES_DEFAULT_MODEL + dbType;
            setting = new DataSourceSetting(dataSourceBase.environment, this.getDbType(), this.getDataSourceSetting());
        }
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DataSourceSetting {
        private String hibernateHbm2DdlAuto;
        private String hibernateFormatSql;
        private String hibernateUseSqlComments;
        private String hibernateShowSql;

        private int loginTimeout;
        private String driverClassName;
        private String catalog;
        private String url;
        private String username;
        private String password;
        private String schema;


        public DataSourceSetting(Environment environment, String dbType, String DS) {
            if (environment == null)
                throw ExceptionBuilder.ofNoImplemented(Environment.class);
            //hibernate
            var e_hibernateHbm2DdlAuto = HB_BASE + ".ddl-auto";
            var e_hibernateFormatSql = HB_BASE + ".format_sql";
            var e_hibernateUseSqlComments = HB_BASE + ".use_sql_comments";
            var e_hibernateShowSql = HB_BASE + ".show_sql";

            hibernateHbm2DdlAuto = environment.getProperty(e_hibernateHbm2DdlAuto, "none");
            hibernateFormatSql = environment.getProperty(e_hibernateFormatSql, "");
            hibernateUseSqlComments = environment.getProperty(e_hibernateUseSqlComments, "");
            hibernateShowSql = environment.getProperty(e_hibernateShowSql, "");

            //datasource
            var e_loginTimeout = DS + ".loginTimeout";
            var e_driverClassName = DS + ".driverClassName";
            var e_catalog = DS + ".catalog";
            var e_url = DS + ".url";
            var e_username = DS + ".username";
            var e_password = DS + ".password";
            var e_schema = DS + ".schema";

            this.loginTimeout = PrimitiveUtil.toInt(environment.getProperty(e_loginTimeout, "0"));
            this.driverClassName = environment.getProperty(e_driverClassName, "");
            this.catalog = environment.getProperty(e_catalog, "");
            this.url = environment.getProperty(e_url, "");
            this.username = environment.getProperty(e_username, "");
            this.password = environment.getProperty(e_password, "");
            this.schema = environment.getProperty(e_schema, "");

            if (this.driverClassName.isEmpty()) {//TODO CHECK TO REMOVE
                if (!PrimitiveUtil.isEmpty(dbType) && dbType.equalsIgnoreCase(ORACLE))
                    driverClassName = "oracle.jdbc.OracleDriver";
            }
        }

        public Map<String, String> hibernateSettings() {


            return Map.of(
                    HIBERNATE_SETTING_IMPLICIT_NAMING_STRATEGY, HIBERNATE_SETTING_IMPLICIT_NAMING_STRATEGY_VALUE,
                    HIBERNATE_SETTING_PHYSICAL_NAMING_STRATEGY, HIBERNATE_SETTING_PHYSICAL_NAMING_STRATEGY_VALUE,
                    HIBERNATE_SETTING_HBM_2_DDL_AUTO, hibernateHbm2DdlAuto,
                    HIBERNATE_SETTING_FORMAT_SQL, hibernateFormatSql,
                    HIBERNATE_SETTING_USE_SQL_COMMENTS, hibernateUseSqlComments,
                    HIBERNATE_SETTING_SHOW_SQL, hibernateShowSql
            );

        }

    }


}