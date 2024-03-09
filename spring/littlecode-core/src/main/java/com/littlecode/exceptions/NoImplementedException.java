package com.littlecode.exceptions;

public class NoImplementedException extends RuntimeException {
    @SuppressWarnings("unused")
    public NoImplementedException(Throwable t) {
        super(t);
    }

    @SuppressWarnings("unused")
    public NoImplementedException(Throwable t, String msg) {
        super(msg, t);
    }

    public NoImplementedException(String msg) {
        super(msg);
    }
}
