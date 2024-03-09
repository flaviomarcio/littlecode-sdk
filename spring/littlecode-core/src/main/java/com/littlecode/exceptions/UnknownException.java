package com.littlecode.exceptions;

public class UnknownException extends RuntimeException {
    @SuppressWarnings("unused")
    public UnknownException(Throwable t) {
        super(t);
    }

    @SuppressWarnings("unused")
    public UnknownException(Throwable t, String msg) {
        super(msg, t);
    }

    public UnknownException(String msg) {
        super(msg);
    }
}
