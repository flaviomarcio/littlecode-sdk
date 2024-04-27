package com.littlecode.exceptions;

public class UnAuthorizationException extends RuntimeException {
    public UnAuthorizationException(String msg) {
        super(msg);
    }
}
