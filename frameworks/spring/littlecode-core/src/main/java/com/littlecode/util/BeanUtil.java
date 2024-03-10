package com.littlecode.util;

import com.littlecode.config.UtilCoreConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class BeanUtil {

    @Setter
    private ApplicationContext context;
    private String bean;

    public static BeanUtil of(ApplicationContext context) {
        return BeanUtil.builder().context(context).build();
    }

    public static BeanUtil of(String bean) {
        return BeanUtil.builder().bean(bean).build();
    }

    public static BeanUtil of(ApplicationContext context, String bean) {
        return BeanUtil.builder().context(context).bean(bean).build();
    }

    public ApplicationContext getContext() {
        if (context == null)
            this.context = UtilCoreConfig.getApplicationContext(this.getClass());
        return this.context;
    }

    public String bean() {
        return this.bean;
    }

    public BeanUtil bean(String bean) {
        this.bean = bean;
        return this;
    }

    public List<String> getBeanNames() {
        try {
            return List.of(getContext().getBeanDefinitionNames());
        } catch (BeansException ignore) {
            return new ArrayList<>();
        }
    }

    public List<String> asAnnotation(Class<? extends Annotation> valueType) {
        try {
            var beanNames = getContext().getBeanNamesForAnnotation(valueType);
            return List.of(beanNames);
        } catch (BeansException ignore) {
            return new ArrayList<>();
        }
    }

    public <T> T getBean(Class<T> valueType) {
        try {
            return getContext().getBean(valueType);
        } catch (BeansException ignore) {
            return null;
        }
    }

    public <T> T getBean(String beanName, Class<T> valueType) {
        try {
            return getContext().getBean(beanName, valueType);
        } catch (BeansException ignore) {
            return null;
        }
    }

    public <T> T as(Class<T> tClass) {
        return this.getBean(this.bean, tClass);
    }

}

