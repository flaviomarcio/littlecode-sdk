package com.littlecode.business.databases;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@SuppressWarnings("unused")
public class DataSourceConfig {

//    @Bean
//    @ConfigurationProperties(DataSourceBase.DS_h2)
//    public DSProperties h2DataSourceProperties() {
//        return new DSProperties();
//    }


    @ConfigurationProperties(DataSourceBase.DS_BASE)
    public static class Properties implements BeanClassLoaderAware, InitializingBean {
        @Override
        public void setBeanClassLoader(@SuppressWarnings("NullableProblems") ClassLoader classLoader) {}
        @Override
        public void afterPropertiesSet(){}
    }

}
