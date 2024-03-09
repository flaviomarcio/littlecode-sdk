package com.littlecode.exceptions;

public class FrameworkException extends RuntimeException {
    @SuppressWarnings("unused")
    public FrameworkException(Throwable t) {
        super(t);
    }

    @SuppressWarnings("unused")
    public FrameworkException(Throwable t, String msg) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public FrameworkException(String msg) {
        super(msg);
    }
}
