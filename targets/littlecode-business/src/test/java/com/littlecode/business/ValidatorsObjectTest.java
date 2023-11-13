package com.littlecode.business;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.littlecode.business.annotation.FieldCheckForBusiness;
import com.littlecode.business.validator.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ValidatorsObjectTest {

    private static String toJson(Object o) {
        var objectMapper = newObjectMapper();
        if (o == null)
            return "";
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    private static ObjectMapper newObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return objectMapper;
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        var objectMapper = newObjectMapper();
        try {
            return objectMapper.readValue(json, classOfT);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Test
    public void UT_CHECK_VALIDATORS() {
        Map<Object, Class<?>> valuesCheck = Map.of(
                "07021871297", CheckerCNH.class,
                "23181252000160", CheckerCNPJ.class,
                "73481324057", CheckerCPF.class,
                "teste@org.test.com", CheckerEmail.class,
                "1901-01-01T23:59:59", CheckerDateTime.class,
                "1901-01-01", CheckerDate.class,
                "23:23:59", CheckerTime.class,
                "5511198761425", CheckerPhoneNumber.class,
                "91150-000", CheckerZipCode.class
        );

        Map<String, Class<?>> jsonCheck = new HashMap<>();
        valuesCheck
                .forEach((value, aClass) ->
                {
                    var json = toJson(Map.of("value", value));
                    jsonCheck.put(json, aClass);
                });

        for (Map.Entry<String, Class<?>> entry : jsonCheck.entrySet()) {
            var json = entry.getKey();
            var aClass = entry.getValue();
            log.debug("class: {}, value: {}", aClass, json);
            Assertions.assertDoesNotThrow(() -> fromJson(json, aClass));
            //Assertions.assertThrows(BusinessValidatorException.class, () -> fromJson(json, aClass));
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CheckerCNH {
        @FieldCheckForBusiness(nullable = true, validator = ValidatorCNH.class)
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CheckerCNPJ {
        @FieldCheckForBusiness(nullable = true, validator = ValidatorCNPJ.class)
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CheckerCPF {
        @FieldCheckForBusiness(nullable = true, validator = ValidatorCPF.class)
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CheckerEmail {
        @FieldCheckForBusiness(nullable = true, validator = ValidatorEMail.class)
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CheckerDateTime {
        @FieldCheckForBusiness(nullable = true, validator = ValidatorLocalDateTimeValidator.class)
        private LocalDateTime value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CheckerDate {
        @FieldCheckForBusiness(nullable = true, validator = ValidatorLocalDateValidator.class)
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CheckerTime {
        @FieldCheckForBusiness(nullable = true, validator = ValidatorLocalDateTimeValidator.class)
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CheckerPhoneNumber {
        @FieldCheckForBusiness(nullable = true, validator = ValidatorPhoneNumber.class)
        private String value;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @SuperBuilder
    private static class CheckerZipCode {
        @FieldCheckForBusiness(nullable = true, validator = ValidatorZipCode.class)
        private String value;
    }

}
