package com.littlecode.tests;

import com.littlecode.exceptions.ArithmeticException;
import com.littlecode.exceptions.*;
import com.littlecode.parsers.ExceptionBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ExceptionBuilderTest {

    @Test
    public void UT_CHECK_OF_EXPTIONS() {
        Assertions.assertDoesNotThrow(() -> new ArithmeticException("teste"));
        Assertions.assertDoesNotThrow(() -> new ArithmeticException("teste"));
        Assertions.assertDoesNotThrow(() -> new BadRequestException("teste"));
        Assertions.assertDoesNotThrow(() -> new ConflictException("teste"));
        Assertions.assertDoesNotThrow(() -> new ConversionException("teste"));
        Assertions.assertDoesNotThrow(() -> new FrameworkException("teste"));
        Assertions.assertDoesNotThrow(() -> new InvalidException("teste"));
        Assertions.assertDoesNotThrow(() -> new InvalidObjectException("teste"));
        Assertions.assertDoesNotThrow(() -> new InvalidSettingException("teste"));
        Assertions.assertDoesNotThrow(() -> new NetworkException("teste"));
        Assertions.assertDoesNotThrow(() -> new NoContentException("teste"));
        Assertions.assertDoesNotThrow(() -> new NoImplementedException("teste"));
        Assertions.assertDoesNotThrow(() -> new NotFoundException("teste"));
        Assertions.assertDoesNotThrow(() -> new ParserException("teste"));
        Assertions.assertDoesNotThrow(() -> new UnAuthorizationException("teste"));
        Assertions.assertDoesNotThrow(() -> new UnknownException("teste"));

        Assertions.assertThrows(ArithmeticException.class,() -> {throw new ArithmeticException("teste");});
        Assertions.assertThrows(BadRequestException.class,() -> {throw new BadRequestException("teste");});
        Assertions.assertThrows(ConflictException.class,() -> {throw new ConflictException("teste");});
        Assertions.assertThrows(ConversionException.class,() -> {throw new ConversionException("teste");});
        Assertions.assertThrows(FrameworkException.class,() -> {throw new FrameworkException("teste");});
        Assertions.assertThrows(InvalidException.class,() -> {throw new InvalidException("teste");});
        Assertions.assertThrows(InvalidObjectException.class,() -> {throw new InvalidObjectException("teste");});
        Assertions.assertThrows(InvalidSettingException.class,() -> {throw new InvalidSettingException("teste");});
        Assertions.assertThrows(NetworkException.class,() -> {throw new NetworkException("teste");});
        Assertions.assertThrows(NoContentException.class,() -> {throw new NoContentException("teste");});
        Assertions.assertThrows(NoImplementedException.class,() -> {throw new NoImplementedException("teste");});
        Assertions.assertThrows(NotFoundException.class,() -> {throw new NotFoundException("teste");});
        Assertions.assertThrows(ParserException.class,() -> {throw new ParserException("teste");});
        Assertions.assertThrows(UnAuthorizationException.class,() -> {throw new UnAuthorizationException("teste");});
        Assertions.assertThrows(UnknownException.class,() -> {throw new UnknownException("teste");});

        Assertions.assertNotNull(new ArithmeticException("teste"));
        Assertions.assertNotNull(new ArithmeticException("teste"));
        Assertions.assertNotNull(new BadRequestException("teste"));
        Assertions.assertNotNull(new ConflictException("teste"));
        Assertions.assertNotNull(new ConversionException("teste"));
        Assertions.assertNotNull(new FrameworkException("teste"));
        Assertions.assertNotNull(new InvalidException("teste"));
        Assertions.assertNotNull(new InvalidObjectException("teste"));
        Assertions.assertNotNull(new InvalidSettingException("teste"));
        Assertions.assertNotNull(new NetworkException("teste"));
        Assertions.assertNotNull(new NoContentException("teste"));
        Assertions.assertNotNull(new NoImplementedException("teste"));
        Assertions.assertNotNull(new NotFoundException("teste"));
        Assertions.assertNotNull(new ParserException("teste"));
        Assertions.assertNotNull(new UnAuthorizationException("teste"));
        Assertions.assertNotNull(new UnknownException("teste"));

    }

    @Test
    public void UT_CHECK_SET() {
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
    public void UT_CHECK_OF_CLASS() {
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
            throw ExceptionBuilder.of(ExceptionBuilder.Type.FrameWork, "teste");
        });
        Assertions.assertThrows(FrameworkException.class, () -> {
            throw ExceptionBuilder.of(ExceptionBuilder.Type.FrameWork, "teste", "teste");
        });
    }


    @Test
    public void UT_CHECK_OF_TYPE() {
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
    }

    @Test
    public void UT_CHECK_OF_MESSAGE() {

        Assertions.assertThrows(RuntimeException.class, () -> {
            throw ExceptionBuilder.ofDefault("test");
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
    }

    @Test
    public void UT_CHECK_OF_FORMAT() {


        Assertions.assertThrows(RuntimeException.class, () -> {
            throw ExceptionBuilder.ofDefault("%s: %s", "test", Object.class);
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
    }

    @Test
    public void UT_CHECK_OF_CLASS_MESSAGE() {
        Assertions.assertThrows(RuntimeException.class, () -> {
            throw ExceptionBuilder.ofDefault(Object.class, "test");
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
    }

    @Test
    public void UT_CHECK_MAKE_MESSAGE() {

        Assertions.assertEquals(ExceptionBuilder.makeMessage(ExceptionBuilder.Type.Default, Object.class, ""), "[Default]: class " + Object.class.getName());
        Assertions.assertEquals(ExceptionBuilder.makeMessage(ExceptionBuilder.Type.Default, "test", ""), "[Default]: test");
        Assertions.assertEquals(ExceptionBuilder.makeMessage(ExceptionBuilder.Type.Default, "test: %s", LocalDate.now()), String.format("[Default]: test: %s", LocalDate.now()));
        Assertions.assertEquals(ExceptionBuilder.makeMessage(ExceptionBuilder.Type.Default, "test: %s", Object.class), String.format("[Default]: test: %s", Object.class));
        Assertions.assertEquals(ExceptionBuilder.makeMessage(ExceptionBuilder.Type.Default, "test: %s", 1), String.format("[Default]: test: %s", 1));
        Assertions.assertEquals(ExceptionBuilder.makeMessage(ExceptionBuilder.Type.Default, "test: %s", 0.01), String.format("[Default]: test: %s", 0.01));
    }


}
