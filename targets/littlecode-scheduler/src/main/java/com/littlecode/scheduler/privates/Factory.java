package com.littlecode.scheduler.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.Configuration;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class Factory {
    private final Setting setting;

    public static <T> T objectCreate(Class<?> classType) {
        try {
            var c = classType.getConstructor();
            //noinspection unchecked
            return (T) c.newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException ignored) {
            return null;
        }
    }

    public Setting setting() {
        return setting;
    }

    public List<String> getBeanNames(Class<? extends Annotation> valueType) {
        try {
            var beanNames = setting.getApplicationContext().getBeanNamesForAnnotation(valueType);
            return List.of(beanNames);
        } catch (BeansException ignore) {
            return new ArrayList<>();
        }
    }

    public <T> T getBean(Class<T> valueType) {
        try {
            return setting.getApplicationContext().getBean(valueType);
        } catch (BeansException ignore) {
            return null;
        }
    }

}

