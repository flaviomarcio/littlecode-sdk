package com.littlecode.business.validator;

import com.littlecode.business.validator.privates.ValidatorBase;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatorCNH extends ValidatorBase {

    public static boolean isValid(String validateValue) {
        char char1 = validateValue.charAt(0);

        if (validateValue.replaceAll("\\D+", "").length() != 11
                || String.format("%0" + 11 + "d", 0).replace('0', char1).equals(validateValue)) {
            return false;
        }

        long v = 0, j = 9;

        for (int i = 0; i < 9; ++i, --j) {
            v += ((validateValue.charAt(i) - 48) * j);
        }

        long dsc = 0, vl1 = v % 11;

        if (vl1 >= 10) {
            vl1 = 0;
            dsc = 2;
        }

        v = 0;
        j = 1;

        for (int i = 0; i < 9; ++i, ++j) {
            v += ((validateValue.charAt(i) - 48) * j);
        }

        long x = v % 11;
        long vl2 = (x >= 10) ? 0 : x - dsc;

        return (String.valueOf(vl1) + String.valueOf(vl2)).equals(validateValue.substring(validateValue.length() - 2));

    }

    @Override
    public boolean canValid() {
        if (isValid(this.getValue()))
            return true;
        this.setMessage(String.format("Numero da CNH invalido: [%s]", this.getValue()));
        return false;
    }

}
