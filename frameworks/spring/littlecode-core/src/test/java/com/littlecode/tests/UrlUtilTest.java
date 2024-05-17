package com.littlecode.tests;

import java.util.List;
import com.littlecode.parsers.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UrlUtilTest {

    @Test
    @DisplayName("Deve validar parsers")
    public void UT_pathParse() {
        Assertions.assertEquals(UrlUtil.pathParse("/api"), "/api");
        Assertions.assertEquals(UrlUtil.pathParse("api/"), "/api");
        Assertions.assertEquals(UrlUtil.pathParse("api//"), "/api");
        Assertions.assertEquals(UrlUtil.pathParse("api//a"), "/api/a");
        Assertions.assertEquals(UrlUtil.pathParse("//api//a"), "/api/a");
        Assertions.assertEquals(UrlUtil.pathParse("/api/a"), "/api/a");
    }

    @Test
    @DisplayName("Deve validar path Match")
    public void UT_pathMatch() {
        UrlUtil.setContextPath("");
        Assertions.assertDoesNotThrow(() -> UrlUtil.pathMatch(List.of("/path/method"), "/path/method"));
        Assertions.assertDoesNotThrow(() -> UrlUtil.pathMatch(List.of("/path/method1"), "/path/method2"));
        Assertions.assertDoesNotThrow(() -> UrlUtil.pathMatch(List.of("/path/**"), "/path/method"));

    }


    @Test
    @DisplayName("Deve validar path maker")
    public void UT_pathMaker() {
        UrlUtil.setContextPath("");
        Assertions.assertEquals(UrlUtil.pathMaker("/path/method"), "/path/method");

        UrlUtil.setContextPath("/api");
        Assertions.assertEquals(UrlUtil.pathMaker("/path/method"), "/api/path/method");

    }

    @Test
    @DisplayName("Deve validar contextPath")
    public void UT_contextPath() {
        UrlUtil.setContextPath("/");
        Assertions.assertNotNull(UrlUtil.getTrustedOpenUrl());
        Assertions.assertNotNull(UrlUtil.getTrustedUrl());
        Assertions.assertTrue(UrlUtil.isTrustedOpenUrl("/swagger-ui"));
        Assertions.assertTrue(UrlUtil.isTrustedUrl("/swagger-ui"));
    }

}
