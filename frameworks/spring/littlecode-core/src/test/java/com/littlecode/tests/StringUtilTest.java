package com.littlecode.tests;

import com.littlecode.parsers.StringUtil;
import com.littlecode.util.TestsUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

//ref
// https://textedit.tools/snakecase
// https://textedit.tools/camelcase
@Slf4j
@ExtendWith(MockitoExtension.class)
class StringUtilTest {


    @Test
    @DisplayName("Deve validar constructores")
    void deveValidarConstructores() {
        Assertions.assertDoesNotThrow(() -> new StringUtil("0"));
        Assertions.assertDoesNotThrow(() -> new StringUtil("#",""));
    }

    @Test
    @DisplayName("Deve validar getter setter")
    void deveValidarGetterSetter() {
        Assertions.assertEquals(new StringUtil("0").getValue(),"0");
        Assertions.assertEquals(new StringUtil("0").getPadChar()," ");

        Assertions.assertEquals(new StringUtil("0").getValue(),"0");
        Assertions.assertEquals(new StringUtil("0","?").getPadChar(),"?");
    }

    @Test
    @DisplayName("Deve validar toString")
    void deveValidarToString() {
        Assertions.assertEquals(StringUtil.toString("0"),"0");
        Assertions.assertEquals(StringUtil.of("0").asString(),"0");
        Assertions.assertEquals(StringUtil.of("0").toString(),"0");
    }

    @Test
    @DisplayName("Deve validar toNumber")
    void deveValidarToNumber() {
        Assertions.assertEquals(StringUtil.toNumber("A0B1C2"),"012");
        Assertions.assertEquals(StringUtil.toNumber("abc"),"");
        Assertions.assertEquals(StringUtil.of("0").asString(),"0");
        Assertions.assertEquals(StringUtil.of("A0B1C2").asNumber(),"012");
    }

    @Test
    @DisplayName("Deve validar toAlpha")
    void deveValidarToAlpha() {
        Assertions.assertEquals(StringUtil.toAlpha("A0B1C2"),"ABC");
        Assertions.assertEquals(StringUtil.toAlpha("012"),"");
        Assertions.assertEquals(StringUtil.of("A0B1C2").asAlpha(),"ABC");
    }

    @Test
    @DisplayName("Deve validar toAlphaNumber")
    void deveValidarToAlphaNumber() {
        Assertions.assertEquals(StringUtil.toAlphaNumber("A0B1C2"),"A0B1C2");
        Assertions.assertEquals(StringUtil.toAlphaNumber("012"),"012");
        Assertions.assertEquals(StringUtil.of("A0B1C2").asAlphaNumber(),"A0B1C2");
    }


    @Test
    @DisplayName("Deve validar metodo pad left")
    void deveValidarPadLeft() {
        String value = "#";
        Assertions.assertDoesNotThrow(() -> StringUtil.toLeftPad(10, "0", null));
        Assertions.assertDoesNotThrow(() -> StringUtil.toLeftPad(10, "0", value));

        {//deprecated
            var v = StringUtil.leftPad(10, "0", value);
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "000000000#");
        }

        {//aling abaixo do length
            var v = StringUtil.toLeftPad(10, "0", value);
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "000000000#");
        }

        {//aling acima do length
            var v = StringUtil.toLeftPad(10, "0", "#000000000");
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "#000000000");
        }

        {//builder
            var v = StringUtil.of(value).padChar("0").asLeftPad(10);
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "000000000#");
        }

    }

    @Test
    @DisplayName("Deve validar metodo pad right")
    void deveValidarPadRight() {
        String value = "#";
        Assertions.assertDoesNotThrow(() -> StringUtil.toRightPad(10, "0", null));
        Assertions.assertDoesNotThrow(() -> StringUtil.toRightPad(10, "0", value));
        {//deprecated
            var v = StringUtil.rightPad(10, "0", value);
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "#000000000");
        }

        {//aling abaixo do length
            var v = StringUtil.toRightPad(10, "0", value);
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "#000000000");
        }

        {//acima do length
            var v = StringUtil.toRightPad(10, "0", "#000000000");
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "#000000000");
        }

        {//builder
            var v = StringUtil.of(value).padChar("0").asRightPad(10);
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "#000000000");
        }
    }

    @Test
    @DisplayName("Deve validar metodo pad center")
    void deveValidarPadCenter() {
        String value = "#";
        Assertions.assertDoesNotThrow(() -> StringUtil.toCenterPad(10, "0", null));
        Assertions.assertDoesNotThrow(() -> StringUtil.toCenterPad(10, "0", value));
        {//deprecated
            var v = StringUtil.centerPad(10, "0", value);
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "0000#00000");
        }

        {//aling abaixo do length
            var v = StringUtil.toCenterPad(10, "0", value);
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "0000#00000");
        }

        {//aling acima do length
            var v = StringUtil.toCenterPad(10, "0", "0000#00000");
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "0000#00000");
        }

        {//builder
            var v = StringUtil.of(value).padChar("0").asCenterPad(10);
            Assertions.assertNotNull(v);
            Assertions.assertEquals(v, "0000#00000");
        }
    }

}
