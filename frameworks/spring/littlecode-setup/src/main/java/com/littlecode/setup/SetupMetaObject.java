package com.littlecode.setup;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated(since = "Descontinuar o uso")
public @interface SetupMetaObject {
    boolean metaDataIgnore() default false;
}

