package com.littlecode.exceptions;

public class ConflictException extends RuntimeException {
    @SuppressWarnings("unused")
    public ConflictException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public ConflictException(Throwable t) {
        super(t);
    }

    public ConflictException(String msg) {
        super(msg);
    }
}
