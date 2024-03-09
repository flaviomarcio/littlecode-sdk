package com.littlecode.business;

import com.littlecode.business.generator.DataGenerator;
import com.littlecode.business.validator.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.function.Function;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DataTypeGeneratorTest {

    @Test
    public void UT_CHECK_GENERATORS() {

        var charList = List.of(".", "-", "/", "\\");
        var documentList = DataGenerator.DataType.values();

        for (DataGenerator.DataType dataType : documentList) {
            var valueList = DataGenerator
                    .builder()
                    .count(10)
                    .dataType(dataType)
                    .maskInclude(false)
                    .build()
                    .clear()
                    .count(10)
                    .dataType(dataType)
                    .maskInclude(false)
                    .generate();

            Function<String, Boolean> chkStr = new Function<String, Boolean>() {
                @Override
                public Boolean apply(String s) {
                    return (s != null && !s.trim().isEmpty());
                }
            };

            for (Object documentObject : valueList) {

                if (dataType.equals(DataGenerator.DataType.PersonPF) ||
                        dataType.equals(DataGenerator.DataType.PersonPJ) ||
                        dataType.equals(DataGenerator.DataType.PersonPFAndPJ)
                ) {
                    DataGenerator.Person person = (DataGenerator.Person) documentObject;
                    Assertions.assertNotNull(person);
                    Assertions.assertNotNull(person.getId());
                    Assertions.assertTrue(person.getIdNumber() > 0);
                    Assertions.assertTrue(chkStr.apply(person.getName()));
                    Assertions.assertNotNull(person.getDtBirt());
                    Assertions.assertTrue(chkStr.apply(person.getPhoneNumber()));
                    Assertions.assertTrue(chkStr.apply(person.getPhoneCountry()));
                    Assertions.assertTrue(chkStr.apply(person.getPhoneAreaCode()));
                    Assertions.assertTrue(chkStr.apply(person.getPhoneSingleNumber()));
                    Assertions.assertTrue(chkStr.apply(person.getPhoneShortNumber()));
                } else {
                    String document = (String) documentObject;
                    Assertions.assertNotNull(document);
                    Assertions.assertFalse(document.trim().isEmpty());
                    if (!dataType.equals(DataGenerator.DataType.eMail))
                        charList.forEach(invalidChr -> Assertions.assertFalse(document.contains(invalidChr)));

                    if (dataType.equals(DataGenerator.DataType.CPF)) {
                        Assertions.assertDoesNotThrow(() -> ValidatorCPF.isValid(document));
                        Assertions.assertTrue(ValidatorCPF.isValid(document));
                    } else if (dataType.equals(DataGenerator.DataType.CNPJ)) {
                        Assertions.assertDoesNotThrow(() -> ValidatorCNPJ.isValid(document));
                        Assertions.assertTrue(ValidatorCNPJ.isValid(document));
                    } else if (dataType.equals(DataGenerator.DataType.CNH)) {
                        Assertions.assertDoesNotThrow(() -> ValidatorCNH.isValid(document));
                        Assertions.assertEquals(document.length(), 11);
                        //Assertions.assertTrue(ValidatorCNH.isValid(document));
                    } else if (dataType.equals(DataGenerator.DataType.CHASSIS)) {
                        //Assertions.assertDoesNotThrow(() -> ValidatorCHASSIS.isValid(document));
                        //Assertions.assertTrue(ValidatorCHASSIS.isValid(document));
                        Assertions.assertEquals(document.length(), 17);
                    } else if (dataType.equals(DataGenerator.DataType.eMail)) {
                        Assertions.assertDoesNotThrow(() -> ValidatorEMail.isValid(document));
                        Assertions.assertTrue(ValidatorEMail.isValid(document));
                    } else if (dataType.equals(DataGenerator.DataType.PhoneNumber)) {
                        Assertions.assertDoesNotThrow(() -> ValidatorPhoneNumber.isValid(document));
                        Assertions.assertTrue(ValidatorPhoneNumber.isValid(document));
                    } else if (dataType.equals(DataGenerator.DataType.Names)) {
                        Assertions.assertDoesNotThrow(() -> ValidatorEMail.isValid(document));
                        var docSplit = document.split(" ");
                        Assertions.assertNotNull(docSplit);
                        Assertions.assertEquals(docSplit.length, 2);
                    }
                }
            }
        }

    }

}