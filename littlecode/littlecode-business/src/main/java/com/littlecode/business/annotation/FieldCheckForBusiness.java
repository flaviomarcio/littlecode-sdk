package com.littlecode.business.annotation;

import com.littlecode.business.annotation.constraint.FieldCheckConstraint;
import jakarta.validation.Constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {FieldCheckConstraint.class})
public @interface FieldCheckForBusiness {
    String message() default "";

    boolean nullable() default false;

    Class<?>[] validator();
}
