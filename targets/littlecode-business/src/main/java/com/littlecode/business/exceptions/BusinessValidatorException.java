package com.littlecode.business.exceptions;

public class BusinessValidatorException extends RuntimeException {
    public BusinessValidatorException(String msg, Throwable t) {
        super(msg, t);
    }

    public BusinessValidatorException(String msg) {
        super(msg);
    }

    public BusinessValidatorException(String format, Object... args) {
        super(String.format(format, args));
    }

    public BusinessValidatorException(Class<?> classType) {
        super(String.format("Business validator error: [%s]", classType.getName()));
    }
}
