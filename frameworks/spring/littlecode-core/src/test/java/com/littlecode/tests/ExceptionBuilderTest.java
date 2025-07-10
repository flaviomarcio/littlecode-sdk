package com.littlecode.tests;

import com.littlecode.exceptions.ArithmeticException;
import com.littlecode.exceptions.*;
import com.littlecode.parsers.ExceptionBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ExceptionBuilderTest {

    @Test
    @DisplayName("Deve validar constructor")
    void UT_CHECK_OF_EXPTIONS() {
        Assertions.assertDoesNotThrow(() -> new ArithmeticException("test"));
        Assertions.assertDoesNotThrow(() -> new BadRequestException("test"));
        Assertions.assertDoesNotThrow(() -> new ConflictException("test"));
        Assertions.assertDoesNotThrow(() -> new ConversionException("test"));
        Assertions.assertDoesNotThrow(() -> new FrameworkException("test"));
        Assertions.assertDoesNotThrow(() -> new InvalidException("test"));
        Assertions.assertDoesNotThrow(() -> new InvalidObjectException("test"));
        Assertions.assertDoesNotThrow(() -> new InvalidSettingException("test"));
        Assertions.assertDoesNotThrow(() -> new NetworkException("test"));
        Assertions.assertDoesNotThrow(() -> new NoContentException("test"));
        Assertions.assertDoesNotThrow(() -> new NoImplementedException("test"));
        Assertions.assertDoesNotThrow(() -> new NotFoundException("test"));
        Assertions.assertDoesNotThrow(() -> new ParserException("test"));
        Assertions.assertDoesNotThrow(() -> new UnAuthorizationException("test"));
        Assertions.assertDoesNotThrow(() -> new UnknownException("test"));

        Assertions.assertThrows(ArithmeticException.class, () -> {
            throw new ArithmeticException("test");
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException("test");
        });
        Assertions.assertThrows(ConflictException.class, () -> {
            throw new ConflictException("test");
        });
        Assertions.assertThrows(ConversionException.class, () -> {
            throw new ConversionException("test");
        });
        Assertions.assertThrows(FrameworkException.class, () -> {
            throw new FrameworkException("test");
        });
        Assertions.assertThrows(InvalidException.class, () -> {
            throw new InvalidException("test");
        });
        Assertions.assertThrows(InvalidObjectException.class, () -> {
            throw new InvalidObjectException("test");
        });
        Assertions.assertThrows(InvalidSettingException.class, () -> {
            throw new InvalidSettingException("test");
        });
        Assertions.assertThrows(NetworkException.class, () -> {
            throw new NetworkException("test");
        });
        Assertions.assertThrows(NoContentException.class, () -> {
            throw new NoContentException("test");
        });
        Assertions.assertThrows(NoImplementedException.class, () -> {
            throw new NoImplementedException("test");
        });
        Assertions.assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException("test");
        });
        Assertions.assertThrows(ParserException.class, () -> {
            throw new ParserException("test");
        });
        Assertions.assertThrows(UnAuthorizationException.class, () -> {
            throw new UnAuthorizationException("test");
        });
        Assertions.assertThrows(UnknownException.class, () -> {
            throw new UnknownException("test");
        });

    }

    @Test
    @DisplayName("Deve validar getters e setters")
    void UT_CHECK_SET() {
        Assertions.assertDoesNotThrow(() -> new ExceptionBuilder());
        Assertions.assertDoesNotThrow(() -> new ExceptionBuilder().getTarget());
        Assertions.assertDoesNotThrow(() -> new ExceptionBuilder().getType());
        Assertions.assertDoesNotThrow(() -> new ExceptionBuilder().getArgs());
        Assertions.assertDoesNotThrow(() -> ExceptionBuilder.builder().build().setArgs(null));
        Assertions.assertDoesNotThrow(() -> ExceptionBuilder.builder().build().setType(ExceptionBuilder.Type.NoContent));

        var eBuilder = ExceptionBuilder.builder().build();
        Assertions.assertNull(eBuilder.getArgs());
        eBuilder.setArgs(null);
        Assertions.assertNull(eBuilder.getArgs());
        eBuilder.setType(ExceptionBuilder.Type.NoContent);
        Assertions.assertNotNull(eBuilder.getType());
        eBuilder.setType(ExceptionBuilder.Type.NoContent);
        Assertions.assertNotNull(eBuilder.getType());
        Assertions.assertEquals(eBuilder.getType(), ExceptionBuilder.Type.NoContent);

    }

    @Test
    @DisplayName("Deve validar classes")
    void UT_CHECK_OF_CLASS() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            throw ExceptionBuilder.of(new Object());
        });
        Assertions.assertThrows(RuntimeException.class, () -> {
            throw ExceptionBuilder.of(new RuntimeException());
        });
        Assertions.assertThrows(FrameworkException.class, () -> {
            throw ExceptionBuilder.of(ExceptionBuilder.Type.FrameWork);
        });
        Assertions.assertThrows(FrameworkException.class, () -> {
            throw ExceptionBuilder.of(ExceptionBuilder.Type.FrameWork, "test");
        });
        Assertions.assertThrows(FrameworkException.class, () -> {
            throw ExceptionBuilder.of(ExceptionBuilder.Type.FrameWork, "test", "test");
        });
    }

    @Test
    @DisplayName("Deve validar exceptions of types")
    void UT_CHECK_OF_TYPE() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            try {
                Integer.parseInt("A");
            } catch (NumberFormatException e) {
                throw ExceptionBuilder.of(e);
            }
        });

        Assertions.assertThrows(RuntimeException.class, () -> {
            throw ExceptionBuilder.ofDefault(Object.class);
        });
        Assertions.assertThrows(NullPointerException.class, () -> {
            throw ExceptionBuilder.ofNullPointer(Object.class);
        });
        Assertions.assertThrows(ArithmeticException.class, () -> {
            throw ExceptionBuilder.ofArithmetical(Object.class);
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            throw ExceptionBuilder.ofBadRequest(Object.class);
        });
        Assertions.assertThrows(ConflictException.class, () -> {
            throw ExceptionBuilder.ofConflict(Object.class);
        });
        Assertions.assertThrows(ConversionException.class, () -> {
            throw ExceptionBuilder.ofConversion(Object.class);
        });
        Assertions.assertThrows(InvalidSettingException.class, () -> {
            throw ExceptionBuilder.ofSetting(Object.class);
        });
        Assertions.assertThrows(InvalidObjectException.class, () -> {
            throw ExceptionBuilder.ofObject(Object.class);
        });
        Assertions.assertThrows(NotFoundException.class, () -> {
            throw ExceptionBuilder.ofNotFound(Object.class);
        });
        Assertions.assertThrows(NoContentException.class, () -> {
            throw ExceptionBuilder.ofNoContent(Object.class);
        });
        Assertions.assertThrows(ParserException.class, () -> {
            throw ExceptionBuilder.ofParser(Object.class);
        });
        Assertions.assertThrows(InvalidException.class, () -> {
            throw ExceptionBuilder.ofInvalid(Object.class);
        });
        Assertions.assertThrows(UnknownException.class, () -> {
            throw ExceptionBuilder.ofUnknown(Object.class);
        });
        Assertions.assertThrows(NoImplementedException.class, () -> {
            throw ExceptionBuilder.ofNoImplemented(Object.class);
        });
        Assertions.assertThrows(FrameworkException.class, () -> {
            throw ExceptionBuilder.ofFrameWork(Object.class);
        });
        Assertions.assertThrows(UnAuthorizationException.class, () -> {
            throw ExceptionBuilder.ofUnAuthorization(Object.class);
        });
        Assertions.assertThrows(NetworkException.class, () -> {
            throw ExceptionBuilder.ofNetwork(Object.class);
        });
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            throw ExceptionBuilder.ofResponse(HttpStatus.UNAUTHORIZED, Object.class);
        });
    }

    @Test
    @DisplayName("Deve validar exceptions of messages")
    void UT_CHECK_OF_MESSAGE() {

        Assertions.assertThrows(RuntimeException.class, () -> {
            throw ExceptionBuilder.ofDefault("test");
        });
        Assertions.assertThrows(NullPointerException.class, () -> {
            throw ExceptionBuilder.ofNullPointer("test");
        });
        Assertions.assertThrows(ArithmeticException.class, () -> {
            throw ExceptionBuilder.ofArithmetical("test");
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            throw ExceptionBuilder.ofBadRequest("test");
        });
        Assertions.assertThrows(ConflictException.class, () -> {
            throw ExceptionBuilder.ofConflict("test");
        });
        Assertions.assertThrows(ConversionException.class, () -> {
            throw ExceptionBuilder.ofConversion("test");
        });
        Assertions.assertThrows(InvalidSettingException.class, () -> {
            throw ExceptionBuilder.ofSetting("test");
        });
        Assertions.assertThrows(InvalidObjectException.class, () -> {
            throw ExceptionBuilder.ofObject("test");
        });
        Assertions.assertThrows(NotFoundException.class, () -> {
            throw ExceptionBuilder.ofNotFound("test");
        });
        Assertions.assertThrows(NoContentException.class, () -> {
            throw ExceptionBuilder.ofNoContent("test");
        });
        Assertions.assertThrows(ParserException.class, () -> {
            throw ExceptionBuilder.ofParser("test");
        });
        Assertions.assertThrows(InvalidException.class, () -> {
            throw ExceptionBuilder.ofInvalid("test");
        });
        Assertions.assertThrows(UnknownException.class, () -> {
            throw ExceptionBuilder.ofUnknown("test");
        });
        Assertions.assertThrows(NoImplementedException.class, () -> {
            throw ExceptionBuilder.ofNoImplemented("test");
        });
        Assertions.assertThrows(FrameworkException.class, () -> {
            throw ExceptionBuilder.ofFrameWork("test");
        });
        Assertions.assertThrows(UnAuthorizationException.class, () -> {
            throw ExceptionBuilder.ofUnAuthorization("test");
        });
        Assertions.assertThrows(NetworkException.class, () -> {
            throw ExceptionBuilder.ofNetwork("test");
        });
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            throw ExceptionBuilder.ofResponse(HttpStatus.UNAUTHORIZED);
        });
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            throw ExceptionBuilder.ofResponse(HttpStatus.UNAUTHORIZED);
        });
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            throw ExceptionBuilder.ofResponse(HttpStatus.UNAUTHORIZED,"test");
        });
    }

    @Test
    @DisplayName("Deve validar exceptions of string format")
    void UT_CHECK_OF_FORMAT() {


        Assertions.assertThrows(RuntimeException.class, () -> {
            throw ExceptionBuilder.ofDefault("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(NullPointerException.class, () -> {
            throw ExceptionBuilder.ofNullPointer("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(ArithmeticException.class, () -> {
            throw ExceptionBuilder.ofArithmetical("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            throw ExceptionBuilder.ofBadRequest("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(ConflictException.class, () -> {
            throw ExceptionBuilder.ofConflict("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(ConversionException.class, () -> {
            throw ExceptionBuilder.ofConversion("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(InvalidSettingException.class, () -> {
            throw ExceptionBuilder.ofSetting("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(InvalidObjectException.class, () -> {
            throw ExceptionBuilder.ofObject("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(NotFoundException.class, () -> {
            throw ExceptionBuilder.ofNotFound("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(NoContentException.class, () -> {
            throw ExceptionBuilder.ofNoContent("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(ParserException.class, () -> {
            throw ExceptionBuilder.ofParser("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(UnAuthorizationException.class, () -> {
            throw ExceptionBuilder.ofUnAuthorization("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(InvalidException.class, () -> {
            throw ExceptionBuilder.ofInvalid("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(UnknownException.class, () -> {
            throw ExceptionBuilder.ofUnknown("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(NoImplementedException.class, () -> {
            throw ExceptionBuilder.ofNoImplemented("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(FrameworkException.class, () -> {
            throw ExceptionBuilder.ofFrameWork("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(NetworkException.class, () -> {
            throw ExceptionBuilder.ofNetwork("%s: %s", "test", Object.class);
        });
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            throw ExceptionBuilder.ofResponse(HttpStatus.UNAUTHORIZED,"%s: %s", "test", Object.class);
        });
    }

    @Test
    @DisplayName("Deve validar exceptions of class messages")
    void UT_CHECK_OF_CLASS_MESSAGE() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            throw ExceptionBuilder.ofDefault(Object.class, "test");
        });
        Assertions.assertThrows(NullPointerException.class, () -> {
            throw ExceptionBuilder.ofNullPointer(Object.class, "test");
        });
        Assertions.assertThrows(ArithmeticException.class, () -> {
            throw ExceptionBuilder.ofArithmetical(Object.class, "test");
        });
        Assertions.assertThrows(BadRequestException.class, () -> {
            throw ExceptionBuilder.ofBadRequest(Object.class, "test");
        });
        Assertions.assertThrows(ConflictException.class, () -> {
            throw ExceptionBuilder.ofConflict(Object.class, "test");
        });
        Assertions.assertThrows(ConversionException.class, () -> {
            throw ExceptionBuilder.ofConversion(Object.class, "test");
        });
        Assertions.assertThrows(InvalidSettingException.class, () -> {
            throw ExceptionBuilder.ofSetting(Object.class, "test");
        });
        Assertions.assertThrows(InvalidObjectException.class, () -> {
            throw ExceptionBuilder.ofObject(Object.class, "test");
        });
        Assertions.assertThrows(NotFoundException.class, () -> {
            throw ExceptionBuilder.ofNotFound(Object.class, "test");
        });
        Assertions.assertThrows(NoContentException.class, () -> {
            throw ExceptionBuilder.ofNoContent(Object.class, "test");
        });
        Assertions.assertThrows(ParserException.class, () -> {
            throw ExceptionBuilder.ofParser(Object.class, "test");
        });
        Assertions.assertThrows(UnAuthorizationException.class, () -> {
            throw ExceptionBuilder.ofUnAuthorization(Object.class, "test");
        });
        Assertions.assertThrows(InvalidException.class, () -> {
            throw ExceptionBuilder.ofInvalid(Object.class, "test");
        });
        Assertions.assertThrows(UnknownException.class, () -> {
            throw ExceptionBuilder.ofUnknown(Object.class, "test");
        });
        Assertions.assertThrows(NoImplementedException.class, () -> {
            throw ExceptionBuilder.ofNoImplemented(Object.class, "test");
        });
        Assertions.assertThrows(FrameworkException.class, () -> {
            throw ExceptionBuilder.ofFrameWork(Object.class, "test");
        });
        Assertions.assertThrows(NetworkException.class, () -> {
            throw ExceptionBuilder.ofNetwork(Object.class, "test");
        });
        Assertions.assertThrows(ResponseStatusException.class, () -> {
            throw ExceptionBuilder.ofResponse(HttpStatus.UNAUTHORIZED,Object.class, "test");
        });
    }

    @Test
    @DisplayName("Deve validar exceptions of make messages")
    void UT_CHECK_MAKE_MESSAGE() {

        Assertions.assertEquals(ExceptionBuilder.makeMessage(Object.class, ""), "class " + Object.class.getName());
        Assertions.assertEquals(ExceptionBuilder.makeMessage("test", ""), "test");
        Assertions.assertEquals(ExceptionBuilder.makeMessage("test: %s", LocalDate.now()), String.format("test: %s", LocalDate.now()));
        Assertions.assertEquals(ExceptionBuilder.makeMessage("test: %s", Object.class), String.format("test: %s", Object.class));
        Assertions.assertEquals(ExceptionBuilder.makeMessage("test: %s", 1), String.format("test: %s", 1));
        Assertions.assertEquals(ExceptionBuilder.makeMessage("test: %s", 0.01), String.format("test: %s", 0.01));
    }


}
