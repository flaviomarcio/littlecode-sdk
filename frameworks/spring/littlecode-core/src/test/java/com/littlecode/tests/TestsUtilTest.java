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

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class TestsUtilTest {
    @Test
    @DisplayName("Deve validar class")
    void deveValidarClass() {
        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(new DTO_Class_Clean()));
        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(new DTO_Class_GetId_IsEnabled_001()));
        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(new DTO_Class_GetId_IsEnabled_002()));
        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(new DTO_Class_GetId_IsEnabled_003()));
        Assertions.assertDoesNotThrow(() -> TestsUtil.checkObject(new DTO_Class_B()));
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

    @Data
    public static class DTO_Class_GetId_IsEnabled_001 {
        private final boolean enabled = false;
        private final Boolean active = false;
        private Object id;
    }

    @Data
    public static class DTO_Class_GetId_IsEnabled_002 {
        private Object id;
        private Boolean argBoolean;
        private boolean argBool;

        private UUID argUUID;
        private URI argURI;
        private LocalDate argLocalDate;
        private LocalTime argLocalTime;
        private LocalDateTime argLocalDateTime;
        private int argInt;
        private Integer argInteger;
        private Long argLong;
        private Double argDouble;
        private double argDbl;
    }

    @Data
    public static class DTO_Class_GetId_IsEnabled_003 {
        private Object id;
        private Integer enabld;
        private Double active;
    }

    @Data
    @Getter
    public static class DTO_Class_B {
        private UUID field123;
    }


}
