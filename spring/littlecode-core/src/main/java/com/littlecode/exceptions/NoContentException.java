package com.littlecode.exceptions;

public class NoContentException extends RuntimeException {
    @SuppressWarnings("unused")
    public NoContentException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public NoContentException(Throwable t) {
        super(t);
    }

    public NoContentException(String msg) {
        super(msg);
    }
}
