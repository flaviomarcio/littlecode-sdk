package com.littlecode.exceptions;

public class NetworkException extends RuntimeException {
    @SuppressWarnings("unused")
    public NetworkException(Throwable t) {
        super(t);
    }

    @SuppressWarnings("unused")
    public NetworkException(Throwable t, String msg) {
        super(msg, t);
    }

    public NetworkException(String msg) {
        super(msg);
    }
}
