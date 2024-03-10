package com.littlecode.exceptions;

public class ArithmeticException extends RuntimeException {
    @SuppressWarnings("unused")
    public ArithmeticException(Throwable t) {
        super(t);
    }

    @SuppressWarnings("unused")
    public ArithmeticException(Throwable t, String msg) {
        super(msg, t);
    }

    public ArithmeticException(String msg) {
        super(msg);
    }
}
