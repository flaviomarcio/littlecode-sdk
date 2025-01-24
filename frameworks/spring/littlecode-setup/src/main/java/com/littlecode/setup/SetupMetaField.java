package com.littlecode.setup;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated(since = "Descontinuar o uso")
public @interface SetupMetaField {
    boolean ignore() default false;

    boolean primaryKeyIgnore() default false;

    boolean foreignKeyIgnore() default false;

    String defaultValue() default "";
    //boolean incremental() default false;
}

