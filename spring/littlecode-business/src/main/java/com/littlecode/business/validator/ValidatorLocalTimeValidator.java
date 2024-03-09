package com.littlecode.business.validator;

import com.littlecode.business.validator.privates.ValidatorBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorLocalTimeValidator extends ValidatorBase {
    private static final String FORMAT = "HH:mm:ss";

    public static boolean isValid(String validateValue) {
        try {
            var formatter = DateTimeFormatter.ofPattern(FORMAT);
            var value = LocalTime.parse(validateValue, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean canValid() {
        if (isValid(this.getValue()))
            return true;
        this.setMessage(String.format("Hora invalida: [%s]", this.getValue()));
        return false;
    }
}
