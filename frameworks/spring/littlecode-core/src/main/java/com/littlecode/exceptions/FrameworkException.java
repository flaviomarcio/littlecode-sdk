package com.littlecode.exceptions;

public class FrameworkException extends RuntimeException {

    public FrameworkException(Throwable t) {
        super(t);
    }

    public FrameworkException(Throwable t, String msg) {
        super(msg, t);
    }

    public FrameworkException(String msg) {
        super(msg);
    }
}
