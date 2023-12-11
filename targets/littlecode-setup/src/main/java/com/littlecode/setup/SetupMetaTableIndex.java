package com.littlecode.setup;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SetupMetaTableIndex {
    String[] fields();
    SetupMetaIndexType type() default SetupMetaIndexType.Normal;
    String using() default "";
    String name() default "";

    String description() default "";
}

