package com.littlecode.exceptions;

public class ParserException extends RuntimeException {
    @SuppressWarnings("unused")
    public ParserException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public ParserException(Throwable t) {
        super(t);
    }

    public ParserException(String msg) {
        super(msg);
    }
}
