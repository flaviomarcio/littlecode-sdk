package com.littlecode.tests;

import com.littlecode.parsers.AnonymizeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AnonymizeUtilTest {
    @Test
    @DisplayName("Deve validar email")
    void testValidEmail() {
        Assertions.assertEquals(AnonymizeUtil.value(null).asMail(), "");
        Assertions.assertEquals(AnonymizeUtil.value("").asMail(), "");
        Assertions.assertEquals(AnonymizeUtil.value("-").asMail(), "");
        Assertions.assertEquals(AnonymizeUtil.value("@").asMail(), "");
        Assertions.assertEquals(AnonymizeUtil.value("@email.com").asMail(), "");
        Assertions.assertEquals(AnonymizeUtil.value("t@email.com").asMail(), "t****t@email.com");
        Assertions.assertEquals(AnonymizeUtil.value("test@email.com").asMail(), "t****t@email.com");
        Assertions.assertEquals(AnonymizeUtil.value("test123@email.com").asMail(), "test****3@email.com");

    }

    @Test
    @DisplayName("Deve validar phone")
    void testValidPhone() {
        Assertions.assertEquals(AnonymizeUtil.value(null).asPhoneNumber(), "");
        Assertions.assertEquals(AnonymizeUtil.value("").asPhoneNumber(), "");
        Assertions.assertEquals(AnonymizeUtil.value("1").asPhoneNumber(), "");
        Assertions.assertEquals(AnonymizeUtil.value("12").asPhoneNumber(), "");
        Assertions.assertEquals(AnonymizeUtil.value("123").asPhoneNumber(), "");
        Assertions.assertEquals(AnonymizeUtil.value("1234").asPhoneNumber(), "");
        Assertions.assertEquals(AnonymizeUtil.value("12345").asPhoneNumber(), "");
        Assertions.assertEquals(AnonymizeUtil.value("123456").asPhoneNumber(), "");
        Assertions.assertEquals(AnonymizeUtil.value("1234567").asPhoneNumber(), "");
        Assertions.assertEquals(AnonymizeUtil.value("12345678").asPhoneNumber(), "");

        Assertions.assertEquals(AnonymizeUtil.value("+5598012345678").asPhoneNumber(), "0123****78");
        Assertions.assertEquals(AnonymizeUtil.value("98012345678").asPhoneNumber(), "0123****78");
        Assertions.assertEquals(AnonymizeUtil.value("012345678").asPhoneNumber(), "0123****78");
    }

    @Test
    @DisplayName("Deve validar userName")
    void testValidUserName() {
        Assertions.assertEquals(AnonymizeUtil.value(null).asUserName(), "");
        Assertions.assertEquals(AnonymizeUtil.value("").asUserName(), "");
        Assertions.assertEquals(AnonymizeUtil.value("u").asUserName(), "*");
        Assertions.assertEquals(AnonymizeUtil.value("us").asUserName(), "**");
        Assertions.assertEquals(AnonymizeUtil.value("user").asUserName(), "u****r");
        Assertions.assertEquals(AnonymizeUtil.value("user.na").asUserName(), "user****a");
        Assertions.assertEquals(AnonymizeUtil.value("user.name").asUserName(), "user****e");
    }

    @Test
    @DisplayName("Deve validar Document")
    void testValidDocument() {
        Assertions.assertEquals(AnonymizeUtil.value(null).asDocument(), "");
        Assertions.assertEquals(AnonymizeUtil.value("").asDocument(), "");
        Assertions.assertEquals(AnonymizeUtil.value("1").asDocument(), "");
        Assertions.assertEquals(AnonymizeUtil.value("12").asDocument(), "");
        Assertions.assertEquals(AnonymizeUtil.value("123").asDocument(), "");
        Assertions.assertEquals(AnonymizeUtil.value("1234").asDocument(), "1****34");
        Assertions.assertEquals(AnonymizeUtil.value("12345").asDocument(), "12****45");
        Assertions.assertEquals(AnonymizeUtil.value("123456").asDocument(), "123****56");
        Assertions.assertEquals(AnonymizeUtil.value("1234567").asDocument(), "1234****67");
        Assertions.assertEquals(AnonymizeUtil.value("12345678").asDocument(), "1234****78");
        Assertions.assertEquals(AnonymizeUtil.value("123456789").asDocument(), "1234****89");
        Assertions.assertEquals(AnonymizeUtil.value("1234567890").asDocument(), "1234****90");
        Assertions.assertEquals(AnonymizeUtil.value("12345678901").asDocument(), "1234****01");
        Assertions.assertEquals(AnonymizeUtil.value("123456789012").asDocument(), "1234****12");
        Assertions.assertEquals(AnonymizeUtil.value("1234567890123").asDocument(), "1234****23");
        Assertions.assertEquals(AnonymizeUtil.value("12345678901234").asDocument(), "1234****34");

    }

    @Test
    @DisplayName("Deve validar String")
    void testValidString() {
        Assertions.assertEquals(AnonymizeUtil.value(null).asString(), "");
        Assertions.assertEquals(AnonymizeUtil.value("").asString(), "");
        Assertions.assertEquals(AnonymizeUtil.value("1").asString(), "");
        Assertions.assertEquals(AnonymizeUtil.value("12").asString(), "");
        Assertions.assertEquals(AnonymizeUtil.value("123").asString(), "");
        Assertions.assertEquals(AnonymizeUtil.value("1234").asString(), "1****34");
        Assertions.assertEquals(AnonymizeUtil.value("12345").asString(), "12****45");
        Assertions.assertEquals(AnonymizeUtil.value("123456").asString(), "123****56");
        Assertions.assertEquals(AnonymizeUtil.value("1234567").asString(), "1234****67");
        Assertions.assertEquals(AnonymizeUtil.value("12345678").asString(), "1234****78");
        Assertions.assertEquals(AnonymizeUtil.value("123456789").asString(), "1234****89");
        Assertions.assertEquals(AnonymizeUtil.value("1234567890").asString(), "1234****90");
        Assertions.assertEquals(AnonymizeUtil.value("12345678901").asString(), "1234****01");
        Assertions.assertEquals(AnonymizeUtil.value("123456789012").asString(), "1234****12");
        Assertions.assertEquals(AnonymizeUtil.value("1234567890123").asString(), "1234****23");
        Assertions.assertEquals(AnonymizeUtil.value("12345678901234").asString(), "1234****34");

    }
}
