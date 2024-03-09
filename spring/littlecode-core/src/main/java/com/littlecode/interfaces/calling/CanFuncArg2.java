package com.littlecode.interfaces.calling;

@FunctionalInterface
public interface CanFuncArg2<R1, A1, A2> {
    R1 call(A1 a1, A2 a2);
}
