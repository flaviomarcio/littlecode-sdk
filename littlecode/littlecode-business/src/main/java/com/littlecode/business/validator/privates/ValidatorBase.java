package com.littlecode.business.validator.privates;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidatorBase implements ValidatorInterface<String> {
    private boolean nullable;
    private String value;
    private String message;

    public ValidatorBase() {
    }

    @Override
    public boolean isNull() {
        return this.value == null || this.value.trim().isEmpty();
    }

    @Override
    public boolean isValid() {
        if (this.isNullable() && this.isNull())
            return true;
        return this.canValid();
    }

    @Override
    public boolean canValid() {
        throw new RuntimeException("No implemented method");
    }

    @Override
    public boolean isNullable() {
        return this.nullable;
    }

    @Override
    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    @Override
    public String message() {
        if (this.message == null || this.message.trim().isEmpty())
            return String.format("Invalid value %s", this.value);
        return this.message;
    }

    @Override
    public void setMessage(String message) {
        if (this.message == null || this.message.trim().isEmpty())
            this.message = message;
        else
            log.debug("discard a new message: [{}]", message);
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public void setValue(String value) {
        this.value = value;
    }

}
