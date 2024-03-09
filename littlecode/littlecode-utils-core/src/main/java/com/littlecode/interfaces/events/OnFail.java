package com.littlecode.interfaces.events;

@FunctionalInterface
public interface OnFail<CODE> {
    void call(CODE code, Exception e);
}
