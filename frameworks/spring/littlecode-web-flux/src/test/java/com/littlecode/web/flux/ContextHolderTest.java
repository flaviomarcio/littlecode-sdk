//package com.littlecode.jpa;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.UUID;
//
//@ExtendWith(MockitoExtension.class)
//public class ContextHolderTest {
//
//    @Test
//    public void UT_000_CHECK_Constructor_GETTER_SETTER() {
//        Assertions.assertDoesNotThrow(()->RequestContextHolder.setRequestPath(""));
//        Assertions.assertDoesNotThrow(()->RequestContextHolder.setRequestToken(""));
//        Assertions.assertDoesNotThrow(()->RequestContextHolder.setPrincipal(null));
//        Assertions.assertDoesNotThrow(()->RequestContextHolder.setPrincipal(new ObjectTest()));
//        Assertions.assertDoesNotThrow(()->RequestContextHolder.setScopeId(""));
//        Assertions.assertDoesNotThrow(()->RequestContextHolder.setUserId(""));
//        Assertions.assertDoesNotThrow(()->RequestContextHolder.setUserName(""));
//
//        Assertions.assertDoesNotThrow(RequestContextHolder::getRequestPath);
//        Assertions.assertDoesNotThrow(RequestContextHolder::getRequestToken);
//        Assertions.assertDoesNotThrow(RequestContextHolder::getPrincipal);
//        Assertions.assertDoesNotThrow(RequestContextHolder::getScopeId);
//        Assertions.assertDoesNotThrow(RequestContextHolder::getUserId);
//        Assertions.assertDoesNotThrow(RequestContextHolder::getUserName);
//
//        Assertions.assertDoesNotThrow(()-> RequestContextHolder.objectToString(UUID.randomUUID()));
//        Assertions.assertDoesNotThrow(()-> RequestContextHolder.objectToString(UUID.randomUUID().toString()));
//        Assertions.assertDoesNotThrow(()-> RequestContextHolder.objectToString(null));
//
//    }
//
//    @Getter
//    @NoArgsConstructor
//    public static class ObjectTest{
//        public UUID uuid=UUID.randomUUID();
//    }
//
//}
