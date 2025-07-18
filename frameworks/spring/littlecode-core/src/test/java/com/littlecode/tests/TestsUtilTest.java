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
import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class TestsUtilTest {
    @Test
    @DisplayName("Deve validar getter|setter")
    void deveValidarClasses() {
        for (var vBool : List.of(true, false)) {
            TestsUtil.setPrintLog(vBool);
            Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(new Person()));
            Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(new DTO_Class_AllTypes()));
        }
    }

    @Test
    @DisplayName("Deve validar enums")
    void deveValidarEnums() {
        for (var vBool : List.of(true, false)) {
            TestsUtil.setPrintLog(vBool);
            Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(UUID.class, UUID.randomUUID()));
            Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(Enum_A.values()));
            Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(List.of(Enum_B.values(), Enum_C.values())));
            Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(Enum_E.values(), Enum_D.values()));
        }
    }

    enum Enum_A {
        None, Low, High
    }

    @Getter
    enum Enum_B {
        None(1), Low(2), High(3);

        @JsonValue
        private final Integer value;

        Enum_B(int value) {
            this.value = value;
        }

        @JsonCreator
        static Enum_B of(Object id) {
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
    enum Enum_C {
        None(1), Low(2), High(3);

        @JsonValue
        private final Integer value;

        Enum_C(int value) {
            this.value = value;
        }
    }

    @Getter
    enum Enum_D {
        CREDIT(0), DEBIT(1);
        private final Integer value;

        Enum_D(int value) {
            this.value = value;
        }

        @JsonCreator
        static Enum_D of(Object id) {
            if (id instanceof Integer v) {
                return Arrays.stream(values())
                        .filter(t -> t.getValue().equals(v))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid type value: " + v));
            } else {
                return Arrays.stream(values())
                        .filter(t -> t.equals(id))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid type value: " + id));
            }
        }
    }

    @Getter
    enum Enum_E {
        PROVIDER(0), PROFESSIONAL(1), CUSTOMER(2), SEGMENT(3), PRODUCT(4), PRODUCT_AVA(5), PROFESSION(6), ALL_CUSTOMER(7), ALL_PROFESSIONAL(8);
        @JsonValue
        private final Integer value;

        Enum_E(int value) {
            this.value = value;
        }

        @JsonCreator
        static Enum_E of(Object id) {
            if (id instanceof Integer v) {
                return Arrays.stream(values())
                        .filter(t -> t.getValue().equals(v))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid type value: " + v));
            } else {
                return Arrays.stream(values())
                        .filter(t -> t.equals(id))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Invalid type value: " + id));
            }
        }
    }

    @Service
    @Data
    static class DTO_Class_AllTypes {
        private static Object argObject;
        private static Object argObject2;
        private Object id;
        private Boolean argBoolean;
        private UUID argUUID;
        private URI argURI;
        private LocalDate argLocalDate;
        private LocalTime argLocalTime;
        private LocalDateTime argLocalDateTime;
        private int argInt;
        private Integer argInteger;
        private Long argLong;
        private boolean argBoolean2;
        private long argLong2;
        private Double argDouble;
        private double argDbl;
        private Short aShort;
        private short aShort2;
        private BigDecimal bigDecimal;

        static Object getArgsStatic(Object arg1) {
            return argObject;
        }

        static void setArgsStatic1(Object arg1) {
            argObject = arg1;
        }

        static void setArgsStatic2(Object arg1, Object arg2) {
            argObject = arg1;
            argObject2 = arg2;
        }

        Object getArgs() {
            return argObject;
        }

        void setArgs1(Object arg1) {
            argObject = arg1;
        }

        void setArgs2(Object arg1, Object arg2) {
            argObject = arg1;
            argObject2 = arg2;
        }
    }

    @Data
    static class Person {
        private UUID id;
        private LocalDateTime dt;
        private UUID rev;

        Person() {
        }

        static Person of(UUID personId) {
            return new Person();
        }

    }
}
