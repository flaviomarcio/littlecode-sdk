package com.littlecode.business.validator;

import com.littlecode.business.validator.privates.ValidatorBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorCPF extends ValidatorBase {
    private static final int[] weightDocument = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    public static boolean isValid(String validateValue) {
        if ((validateValue == null) || (validateValue.length() != 11)) return false;
        validateValue = validateValue.trim().replace(".", "").replace("-", "");

        for (int j = 0; j < 10; j++)
            if (padLeft(Integer.toString(j), Character.forDigit(j, 10)).equals(validateValue))
                return false;

        int digitOne = digitCalc(validateValue.substring(0, 9));
        int digitTwo = digitCalc(validateValue.substring(0, 9) + digitOne);
        return validateValue.equals(validateValue.substring(0, 9) + digitOne + digitTwo);
    }

    private static int digitCalc(String str) {
        int sum = 0;
        for (int idx = str.length() - 1, digit; idx >= 0; idx--) {
            digit = Integer.parseInt(str.substring(idx, idx + 1));
            sum += digit * weightDocument[weightDocument.length - str.length() + idx];
        }
        sum = 11 - sum % 11;
        return sum > 9 ? 0 : sum;
    }

    private static String padLeft(String value, char chr) {
        return String.format("%11s", value).replace(' ', chr);
    }

    @Override
    public boolean canValid() {
        if (isValid(this.getValue()))
            return true;
        this.setMessage(String.format("Numero do CPF invalido: [%s]", this.getValue()));
        return false;
    }
}
