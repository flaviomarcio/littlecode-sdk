package com.littlecode.interfaces.events;

@FunctionalInterface
public interface OnExecute<A1> {
    void call(A1 a1);
}
