package com.littlecode.tests;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.littlecode.util.TestsUtil;
import lombok.Data;
import lombok.Getter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TestsUtilTest {
    @Test
    @DisplayName("Deve validar getter|setter")
    void deveValidarClasses() {
        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(new DTO_Class_AllTypes()));
//        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(new DTO_Class_Clean()));
    }

    @Test
    @DisplayName("Deve validar enums")
    void deveValidarEnums() {
        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(Enum_A.values()));
        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(Enum_B.values()));
        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(Enum_C.values()));

    }

    public enum Enum_A {
        None, Low, High
    }

    @Getter
    public enum Enum_B {
        None(1), Low(2), High(3);

        @JsonValue
        private final Integer value;

        Enum_B(int value) {
            this.value = value;
        }

        @JsonCreator
        public static Enum_B of(Object id) {
            if (id instanceof String) {
                return Arrays.stream(values())
                        .filter(t -> t.name().equalsIgnoreCase((String) id))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid type: " + id));
            } else if (id instanceof Integer) {
                return Arrays.stream(values())
                        .filter(t -> t.getValue().equals(id))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid type value: " + id));
            } else {
                return Arrays.stream(values())
                        .filter(t -> t.equals(id))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid type value: " + id));
            }
        }
    }

    @Getter
    public enum Enum_C {
        None(1), Low(2), High(3);

        @JsonValue
        private final Integer value;

        Enum_C(int value) {
            this.value = value;
        }
    }

    public static class DTO_Class_Clean {
    }

    @Service
    @Data
    public static class DTO_Class_AllTypes {
        private Object id;
        private Boolean argBoolean;
        private static Object argObject;

        private UUID argUUID;
        private URI argURI;
        private LocalDate argLocalDate;
        private LocalTime argLocalTime;
        private LocalDateTime argLocalDateTime;
        private int argInt;
        private Integer argInteger;
        private Long argLong;
        private static Object argObject2;
        private boolean argBoolean2;
        private long argLong2;
        private Double argDouble;
        private double argDbl;
        private Short aShort;
        private short aShort2;
        private BigDecimal bigDecimal;

        public static Object getArgsStatic(Object arg1) {
            return argObject;
        }

        public static void setArgsStatic1(Object arg1) {
            argObject = arg1;
        }

        public static void setArgsStatic2(Object arg1, Object arg2) {
            argObject = arg1;
            argObject2 = arg2;
        }

        public Object getArgs() {
            return argObject;
        }

        public void setArgs1(Object arg1) {
            argObject = arg1;
        }

        public void setArgs2(Object arg1, Object arg2) {
            argObject = arg1;
            argObject2 = arg2;
        }
    }
}
