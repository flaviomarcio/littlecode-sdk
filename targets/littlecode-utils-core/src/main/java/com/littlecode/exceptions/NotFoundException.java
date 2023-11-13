package com.littlecode.exceptions;

public class NotFoundException extends RuntimeException {
    @SuppressWarnings("unused")
    public NotFoundException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public NotFoundException(Throwable t) {
        super(t);
    }

    public NotFoundException(String msg) {
        super(msg);
    }
}
