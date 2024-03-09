package com.littlecode.exceptions;

public class UnAuthorizationException extends RuntimeException {
    @SuppressWarnings("unused")
    public UnAuthorizationException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public UnAuthorizationException(Throwable t) {
        super(t);
    }

    public UnAuthorizationException(String msg) {
        super(msg);
    }
}
