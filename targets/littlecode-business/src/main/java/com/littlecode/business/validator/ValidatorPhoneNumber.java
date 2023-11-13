package com.littlecode.business.validator;

import com.littlecode.business.validator.privates.ValidatorBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorPhoneNumber extends ValidatorBase {
    private static final String REGEX = "^\\d{13}$";
    private static final Pattern pattern = Pattern.compile(REGEX);

    public static boolean isValid(String validateValue) {
        Matcher matcher = pattern.matcher(validateValue);
        return matcher.matches();
    }

    @Override
    public boolean canValid() {
        if (isValid(this.getValue()))
            return true;
        this.setMessage(String.format("Numero do telefone invalido: [%s]", this.getValue()));
        return false;
    }
}
