package com.littlecode.business.validator;

import com.littlecode.business.validator.privates.ValidatorBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorLocalDateValidator extends ValidatorBase {
    private static final String FORMAT = "yyyy-MM-dd";

    public static boolean isValid(String validateValue) {
        try {
            var formatter = DateTimeFormatter.ofPattern(FORMAT);
            var value = LocalDate.parse(validateValue, formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean canValid() {
        if (isValid(this.getValue()))
            return true;
        this.setMessage(String.format("Data invalida: [%s]", this.getValue()));
        return false;
    }
}
