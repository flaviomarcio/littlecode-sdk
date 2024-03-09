package com.littlecode.exceptions;

public class InvalidSettingException extends RuntimeException {
    @SuppressWarnings("unused")
    public InvalidSettingException(String msg, Throwable t) {
        super(msg, t);
    }

    @SuppressWarnings("unused")
    public InvalidSettingException(Throwable t) {
        super(t);
    }

    public InvalidSettingException(String msg) {
        super(msg);
    }
}
