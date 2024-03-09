package com.littlecode.scheduler;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface Schedule {
    boolean enable() default true;

    String expression() default "";

    Class<Scheduler.Checker> checker();

    int order() default 0;
}
