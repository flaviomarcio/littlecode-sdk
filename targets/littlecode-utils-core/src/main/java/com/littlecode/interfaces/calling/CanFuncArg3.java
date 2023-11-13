package com.littlecode.interfaces.calling;

@FunctionalInterface
public interface CanFuncArg3<R1, A1, A2, A3> {
    R1 call(A1 a1, A2 a2, A3 a3);
}
