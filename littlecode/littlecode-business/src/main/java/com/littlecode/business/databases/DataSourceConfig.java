package com.littlecode.business.databases;

import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SuppressWarnings("unused")
public class DataSourceConfig {

    @ConfigurationProperties(DataSourceBase.DS_BASE)
    public static class Properties implements BeanClassLoaderAware, InitializingBean {
        @Override
        public void setBeanClassLoader(@SuppressWarnings("NullableProblems") ClassLoader classLoader) {
        }

        @Override
        public void afterPropertiesSet() {
        }
    }

}
