package com.littlecode.tests;

import com.littlecode.parsers.UrlUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UrlUtilTest {

    @Test
    @DisplayName("Deve validar parsers")
    public void UT_CONSTRUCTOR_GETTER() {
        Assertions.assertDoesNotThrow(() -> UrlUtil.builder().build());
        Assertions.assertDoesNotThrow(UrlUtil::new);
        var urlUtil = UrlUtil.builder().build();
        Assertions.assertDoesNotThrow(urlUtil::getTrustedUrl);
        Assertions.assertDoesNotThrow(urlUtil::getTrustedOpenUrl);
        Assertions.assertDoesNotThrow(urlUtil::getContextPath);
        urlUtil.contextPath(null);
        Assertions.assertDoesNotThrow(urlUtil::getContextPath);

        Assertions.assertDoesNotThrow(() -> urlUtil.contextPath("/api"));
        Assertions.assertDoesNotThrow(() -> urlUtil.trustedUrls(new ArrayList<>()));
        Assertions.assertDoesNotThrow(() -> urlUtil.trustedUrlsOpen(new ArrayList<>()));
    }


    @Test
    @DisplayName("Deve validar parsers")
    public void UT_pathParse() {
        var urlUtil = UrlUtil.builder().build();
        Assertions.assertEquals(urlUtil.pathParse("/api"), "/api");
        Assertions.assertEquals(urlUtil.pathParse("api/"), "/api");
        Assertions.assertEquals(urlUtil.pathParse("api//"), "/api");
        Assertions.assertEquals(urlUtil.pathParse("api//a"), "/api/a");
        Assertions.assertEquals(urlUtil.pathParse("//api//a"), "/api/a");
        Assertions.assertEquals(urlUtil.pathParse("/api/a"), "/api/a");
    }

    @Test
    @DisplayName("Deve validar path Match")
    public void UT_pathMatch() {
        var urlUtil = UrlUtil.builder().contextPath("").build();
        Assertions.assertDoesNotThrow(() -> urlUtil.pathMatch(List.of("/path/method"), "/path/method"));
        Assertions.assertDoesNotThrow(() -> urlUtil.pathMatch(List.of("/path/method1"), "/path/method2"));
        Assertions.assertDoesNotThrow(() -> urlUtil.pathMatch(List.of("/path/**"), "/path/method"));

    }


    @Test
    @DisplayName("Deve validar path maker")
    public void UT_pathMaker() {
        {
            var urlUtil = UrlUtil.builder().contextPath("/").build();
            Assertions.assertEquals(urlUtil.pathMaker("/path/method"), "/path/method");
        }

        {
            var urlUtil = UrlUtil.builder().contextPath("/api").build();
            Assertions.assertEquals(urlUtil.pathMaker("/path/method"), "/api/path/method");
        }

    }

    @Test
    @DisplayName("Deve validar contextPath")
    public void UT_contextPath() {
        var urlUtil = UrlUtil.builder().contextPath("/").build();
        Assertions.assertNotNull(urlUtil.getTrustedOpenUrl());
        Assertions.assertNotNull(urlUtil.getTrustedUrl());
        Assertions.assertTrue(urlUtil.isTrustedOpenUrl("/swagger-ui"));
        Assertions.assertTrue(urlUtil.isTrustedUrl("/swagger-ui"));
    }

}
