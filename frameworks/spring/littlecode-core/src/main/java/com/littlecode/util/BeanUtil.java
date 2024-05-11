package com.littlecode.util;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.parsers.ExceptionBuilder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class BeanUtil {
    private final ApplicationContext context;
    @Getter
    @Setter
    private String bean;

    public BeanUtil(){
        this.context=UtilCoreConfig.getApplicationContext();
        if(this.context==null)
            throw ExceptionBuilder.ofFrameWork("context is null");
    }

    public BeanUtil(ApplicationContext context){
        this.context=context;
        if(this.context==null)
            throw ExceptionBuilder.ofFrameWork("context is null");
    }

    public BeanUtil(String bean){
        this.context=UtilCoreConfig.getApplicationContext();
        if(this.context==null)
            throw ExceptionBuilder.ofFrameWork("context is null");
        this.bean=bean;
    }

    public BeanUtil(ApplicationContext context, String bean){
        this.context=context;
        if(this.context==null)
            throw ExceptionBuilder.ofFrameWork("context is null");
        this.bean=bean;
    }

    public static BeanUtil of(ApplicationContext context) {
        return new BeanUtil(context);
    }

    public static BeanUtil of(String bean) {
        return new BeanUtil(bean);
    }

    public static BeanUtil of(ApplicationContext context, String bean) {
        return new BeanUtil(context, bean);
    }

    public String bean() {
        return this.bean;
    }

    public BeanUtil bean(String bean) {
        this.bean = bean;
        return this;
    }

    public List<String> getBeanNames() {
        return List.of(context.getBeanDefinitionNames());
    }

    public List<String> asAnnotation(Class<? extends Annotation> valueType) {
        var beans=context.getBeanNamesForAnnotation(valueType);
        return (beans.length==0)
                ?new ArrayList<>()
                :List.of(beans);
    }

    public <T> T getBean(String beanName, Class<T> valueType) {
        try{
            if(beanName==null || valueType==null)
                throw new FrameworkException("");
            return context.getBean(beanName, valueType);
        }catch (Exception e){
            return null;
        }
    }

    public <T> T getBean(Class<T> valueType) {
        return this.getBean(this.bean, valueType);
    }

    public <T> T as(Class<T> valueType) {
        return this.getBean(this.bean, valueType);
    }

}