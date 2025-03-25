package com.littlecode.tests;

import com.littlecode.util.SmartValidatorUtil;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import org.springframework.validation.SmartValidator;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@ExtendWith(MockitoExtension.class)
public class SmartValidatorUtilTest {
    @Test
    @DisplayName("deve validar metodo validateDTO")
    void deveValidatCheck() {
        {
            var smartValidator = Mockito.mock(SmartValidator.class);

            Assertions.assertDoesNotThrow(() -> new SmartValidatorUtil(smartValidator));
        }
        {//sem erros
            var smartValidator = Mockito.mock(SmartValidator.class);

            var service = new SmartValidatorUtil(smartValidator);
            var dto = new PrivateDTO();
            Assertions.assertNotNull(service.check(dto));
        }

        {//com erros
            var smartValidator = Mockito.mock(SmartValidator.class);


            // Configure o mock para adicionar erros ao objeto Errors
            doAnswer(invocation -> {
                Errors e = invocation.getArgument(1);
                e.rejectValue("uuid", "error.code", "Error message");
                return null;
            }).when(smartValidator).validate(any(), any(Errors.class));
            var service = new SmartValidatorUtil(smartValidator);
            var dto = new PrivateDTO();
            service.check(dto);
        }

    }

//    @Test
//    @DisplayName("deve validar dto Error")
//    void deveValidarDTOError() {
//        Assertions.assertDoesNotThrow(() -> new SmartValidatorUtil.Error());
//        Assertions.assertDoesNotThrow(() -> {
//            var dto=SmartValidatorUtil.Error.builder().build();
//            Assertions.assertDoesNotThrow(() -> dto.getCodeError());
//            Assertions.assertDoesNotThrow(() -> dto.getField());
//            Assertions.assertDoesNotThrow(() -> dto.getMessage());
//            Assertions.assertDoesNotThrow(() -> dto.getMessageDetail());
//
//            Assertions.assertDoesNotThrow(() -> dto.setCodeError(null));
//            Assertions.assertDoesNotThrow(() -> dto.setField(null));
//            Assertions.assertDoesNotThrow(() -> dto.setMessage(null));
//            Assertions.assertDoesNotThrow(() -> dto.setMessageDetail(null));
//        });
//
//    }

    @Data
    @AllArgsConstructor
    @Builder
    public static class PrivateDTO {
        private UUID uuid;
        @NotNull
        private LocalDate localDate;

        public PrivateDTO() {
            this.uuid = UUID.randomUUID();
            this.localDate = null;
        }

    }
}
