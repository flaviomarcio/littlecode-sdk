package com.littlecode.exceptions;

public class InvalidException extends RuntimeException {
    @SuppressWarnings("unused")
    public InvalidException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public InvalidException(Throwable t) {
        super(t);
    }

    public InvalidException(String msg) {
        super(msg);
    }
}
