package com.littlecode.business;

import com.littlecode.business.validator.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ValidatorsTest {
    private final Random rand = new Random();

    @Test
    public void UT_CHECK_VALIDATORS() {
        Assertions.assertNotNull(Validators.CNH);
        Assertions.assertNotNull(Validators.CNPJ);
        Assertions.assertNotNull(Validators.CPF);
        Assertions.assertNotNull(Validators.eMail);
        Assertions.assertNotNull(Validators.PhoneNumber);
        Assertions.assertNotNull(Validators.ZipCode);
        Assertions.assertNotNull(Validators.Date);
        Assertions.assertNotNull(Validators.Time);
        Assertions.assertNotNull(Validators.DateTime);

        Assertions.assertNull(createObject(Validators.CNH));//check @NoArgsConstructor(access = AccessLevel.PRIVATE)
        Assertions.assertNull(createObject(Validators.CNPJ));//check @NoArgsConstructor(access = AccessLevel.PRIVATE)
        Assertions.assertNull(createObject(Validators.CPF));//check @NoArgsConstructor(access = AccessLevel.PRIVATE)
        Assertions.assertNull(createObject(Validators.eMail));//check @NoArgsConstructor(access = AccessLevel.PRIVATE)
        Assertions.assertNull(createObject(Validators.PhoneNumber));//check @NoArgsConstructor(access = AccessLevel.PRIVATE)
        Assertions.assertNull(createObject(Validators.ZipCode));//check @NoArgsConstructor(access = AccessLevel.PRIVATE)
        Assertions.assertNull(createObject(Validators.Date));//check @NoArgsConstructor(access = AccessLevel.PRIVATE)
        Assertions.assertNull(createObject(Validators.Time));//check @NoArgsConstructor(access = AccessLevel.PRIVATE)
        Assertions.assertNull(createObject(Validators.DateTime));//check @NoArgsConstructor(access = AccessLevel.PRIVATE)

    }


    //ref https://www.4devs.com.br/gerador_de_cnh
    @Test
    public void UT_CHECK_VALIDATOR_CNH() {
        var list = List.of(
                "07021871297", "97468296681", "96528182392"
        );
        for (var value : list) {
            log.debug("value: {}", value);
            Assertions.assertTrue(ValidatorCNH.isValid(value));
            Assertions.assertFalse(ValidatorCNH.isValid(value.substring(value.length() / 2)));
            Assertions.assertFalse(ValidatorCNH.isValid(value + "A"));
            Assertions.assertFalse(ValidatorCNH.isValid(+rand.nextInt() + value + rand.nextInt()));
            Assertions.assertFalse(ValidatorCNH.isValid(value + " "));
            Assertions.assertFalse(ValidatorCNH.isValid(value + "-"));
            Assertions.assertFalse(ValidatorCNH.isValid(value + "@"));

        }
    }

    //ref https://www.4devs.com.br/gerador_de_cpf
    @Test
    public void UT_CHECK_VALIDATOR_CNPJ() {
        var list = List.of(
                "23.181.252/0001-60",
                "47.711.574/0001-31",
                "89.405.411/0001-70",
                "07.069.901/0001-89"//start with zero
        );
        for (var value : list) {
            log.debug("value: {}", value);
            var valueOK = value
                    .replace(".", "")
                    .replace("-", "")
                    .replace("/", "");
            Assertions.assertTrue(ValidatorCNPJ.isValid(valueOK));
            Assertions.assertFalse(ValidatorCNPJ.isValid(valueOK + valueOK));
            Assertions.assertFalse(ValidatorCNPJ.isValid(value));
            Assertions.assertFalse(ValidatorCNPJ.isValid(value.replace(".", "@")));
            Assertions.assertFalse(ValidatorCNPJ.isValid(value.replace(".", " ")));
            Assertions.assertFalse(ValidatorCNPJ.isValid(value.replace(".", "/")));
        }
    }


    //ref https://www.4devs.com.br/gerador_de_cnpj
    @Test
    public void UT_CHECK_VALIDATOR_CPF() {
        var list = List.of(
                "734.813.240-57",
                "382.939.220-60",
                "229.288.070-98",
                "034.968.020-57"//start with zero
        );
        for (var value : list) {
            log.debug("value: {}", value);
            var valueOK = value
                    .replace(".", "")
                    .replace("-", "");
            Assertions.assertTrue(ValidatorCPF.isValid(valueOK));
            Assertions.assertFalse(ValidatorCPF.isValid(valueOK + valueOK));
            Assertions.assertFalse(ValidatorCPF.isValid(value));
            Assertions.assertFalse(ValidatorCPF.isValid(value.replace(".", "@")));
            Assertions.assertFalse(ValidatorCPF.isValid(value.replace(".", " ")));
            Assertions.assertFalse(ValidatorCPF.isValid(value.replace(".", "/")));
        }
    }

    //ref https://www.4devs.com.br/gerador_de_cnpj
    @Test
    public void UT_CHECK_VALIDATOR_EMAIL() {
        var list = List.of(
                "john.macdonald@domain.com", "maichael.smith@domain.com", "clark.taylor@org.com"
        );
        for (var value : list) {
            log.debug("value: {}", value);
            Assertions.assertTrue(ValidatorEMail.isValid(value));
            Assertions.assertFalse(ValidatorEMail.isValid(value + ";"));
            Assertions.assertFalse(ValidatorEMail.isValid(value.replace("@", ";")));
            Assertions.assertFalse(ValidatorEMail.isValid(value.replace("@", " ")));
            Assertions.assertFalse(ValidatorEMail.isValid(value.replace("@", "-")));
            Assertions.assertFalse(ValidatorEMail.isValid(value.replace(".", ";")));
            Assertions.assertFalse(ValidatorEMail.isValid(""));
        }
    }

    @Test
    public void UT_CHECK_VALIDATOR_PHONE_NUMBER() {
        var list = List.of(
                "(55)88 9 9999-7977",
                "55 88 9 9999 7911",
                "55 23 3 4455-7778"
        );
        for (var value : list) {
            log.debug("value: {}", value);
            var valueOK = value
                    .replace(" ", "")
                    .replace("(", "")
                    .replace(")", "")
                    .replace("-", "");
            Assertions.assertTrue(ValidatorPhoneNumber.isValid(valueOK));
            Assertions.assertFalse(ValidatorPhoneNumber.isValid(value));
            Assertions.assertFalse(ValidatorPhoneNumber.isValid(valueOK.substring(valueOK.length() - 2)));
            Assertions.assertFalse(ValidatorPhoneNumber.isValid(valueOK + "@"));
            Assertions.assertFalse(ValidatorPhoneNumber.isValid(valueOK + " "));
        }
    }

    //ref https://www.4devs.com.br/gerador_de_cep
    @Test
    public void UT_CHECK_VALIDATOR_ZIPCODE() {
        var list = List.of(
                "73270-085", "58067-257", "65040-873"
        );
        for (var value : list) {
            log.debug("value: {}", value);
            Assertions.assertTrue(ValidatorZipCode.isValid(value));
            Assertions.assertFalse(ValidatorZipCode.isValid(value + rand.nextInt()));
            Assertions.assertFalse(ValidatorZipCode.isValid(value.substring(value.length() - 2)));
        }
    }

    @Test
    public void UT_CHECK_VALIDATOR_DATE() {
        var list = List.of(
                "2021-01-01", "1901-02-02", LocalDate.now().toString()
        );
        for (var value : list) {
            log.debug("value: {}", value);
            Assertions.assertTrue(ValidatorLocalDateValidator.isValid(value));
            Assertions.assertFalse(ValidatorLocalDateValidator.isValid(value + rand.nextInt()));
            Assertions.assertFalse(ValidatorLocalDateValidator.isValid(value.substring(value.length() - 2)));
        }
    }

    @Test
    public void UT_CHECK_VALIDATOR_TIME() {
        var list = List.of(
                "00:00:00", "01:01:01", "23:59:59"
        );
        for (var value : list) {
            log.debug("value: {}", value);
            Assertions.assertTrue(ValidatorLocalTimeValidator.isValid(value));
            Assertions.assertFalse(ValidatorLocalTimeValidator.isValid(value + rand.nextInt()));
            Assertions.assertFalse(ValidatorLocalTimeValidator.isValid(value.substring(value.length() - 2)));
        }
    }

    @Test
    public void UT_CHECK_VALIDATOR_DATE_TIME() {
        var list = List.of(
                "1901-01-01T00:00:00", "2021-01-01T00:00:00", "1901-02-02T23:59:59"
        );
        for (var value : list) {
            log.debug("value: {}", value);
            Assertions.assertTrue(ValidatorLocalDateTimeValidator.isValid(value));
            Assertions.assertFalse(ValidatorLocalDateTimeValidator.isValid(value + rand.nextInt()));
            Assertions.assertFalse(ValidatorLocalDateTimeValidator.isValid(value.substring(value.length() - 2)));
        }
    }


    private Object createObject(Class<?> aClass) {
        try {
            var c = aClass.getConstructor();
            return c.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            return null;
        }
    }


}
