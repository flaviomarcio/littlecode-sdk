package com.littlecode.exceptions;

public class InvalidObjectException extends RuntimeException {
    @SuppressWarnings("unused")
    public InvalidObjectException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public InvalidObjectException(Throwable t) {
        super(t);
    }

    public InvalidObjectException(String msg) {
        super(msg);
    }
}
