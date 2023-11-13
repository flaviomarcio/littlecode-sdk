package com.littlecode.business.validator;

import com.littlecode.business.validator.privates.ValidatorBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorLocalDateTimeValidator extends ValidatorBase {
    private static final String FORMAT_DATE_TIME = "yyyy-MM-dd'T'HH:mm:ss";

    public static boolean isValid(String validateValue) {
        try {
            var formatter = DateTimeFormatter.ofPattern(FORMAT_DATE_TIME);
            var value = LocalDateTime.parse(validateValue, formatter);
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public boolean canValid() {
        if (isValid(this.getValue()))
            return true;
        this.setMessage(String.format("Data/hora invalida: [%s]", this.getValue()));
        return false;
    }
}
