package com.littlecode.web.core;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RequestContextHolderTest {

    @Test
    public void UT_000_CHECK_Constructor_GETTER_SETTER() {
        Assertions.assertDoesNotThrow(()->RequestContextHolder.setRequestPath(""));
        Assertions.assertDoesNotThrow(()->RequestContextHolder.setRequestToken(""));
        Assertions.assertDoesNotThrow(()->RequestContextHolder.setConstRequestContext(null));
        Assertions.assertDoesNotThrow(()->RequestContextHolder.setScopeId(""));
        Assertions.assertDoesNotThrow(()->RequestContextHolder.setUserId(""));
        Assertions.assertDoesNotThrow(()->RequestContextHolder.setUserName(""));

        Assertions.assertDoesNotThrow(RequestContextHolder::getRequestPath);
        Assertions.assertDoesNotThrow(RequestContextHolder::getRequestToken);
        Assertions.assertDoesNotThrow(RequestContextHolder::getRequestContext);
        Assertions.assertDoesNotThrow(RequestContextHolder::getScopeId);
        Assertions.assertDoesNotThrow(RequestContextHolder::getUserId);
        Assertions.assertDoesNotThrow(RequestContextHolder::getUserName);
    }

    @Getter
    @NoArgsConstructor
    public static class ObjectTest{
        public UUID uuid=UUID.randomUUID();
    }

}
