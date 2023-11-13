package com.littlecode.exceptions;

public class ConversionException extends RuntimeException {
    @SuppressWarnings("unused")
    public ConversionException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public ConversionException(Throwable t) {
        super(t);
    }

    public ConversionException(String msg) {
        super(msg);
    }

}
