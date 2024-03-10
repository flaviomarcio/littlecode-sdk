package com.littlecode.business.validator.privates;

public interface ValidatorInterface<T> {
    boolean isNull();

    boolean isValid();

    boolean canValid();

    boolean isNullable();

    void setNullable(boolean nullable);

    String message();

    void setMessage(String message);

    T getValue();

    void setValue(T value);
}
