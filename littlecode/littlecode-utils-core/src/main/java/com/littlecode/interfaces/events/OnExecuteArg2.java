package com.littlecode.interfaces.events;

@FunctionalInterface
public interface OnExecuteArg2<A1, A2> {
    void call(A1 a1, A2 a2);
}
