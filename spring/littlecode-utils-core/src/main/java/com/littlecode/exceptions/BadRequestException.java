package com.littlecode.exceptions;

public class BadRequestException extends RuntimeException {
    @SuppressWarnings("unused")
    public BadRequestException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public BadRequestException(Throwable t) {
        super(t);
    }

    public BadRequestException(String msg) {
        super(msg);
    }
}
