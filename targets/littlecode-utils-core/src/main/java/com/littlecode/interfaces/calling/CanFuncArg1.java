package com.littlecode.interfaces.calling;

@FunctionalInterface
public interface CanFuncArg1<R1, A1> {
    R1 call(A1 a1);
}
