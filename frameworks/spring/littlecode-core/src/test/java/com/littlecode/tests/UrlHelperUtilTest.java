package com.littlecode.tests;

import com.littlecode.parsers.UrlHelperUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UrlHelperUtilTest {



    @Test
    @DisplayName("deve validar condições iniciais")
    void deveValidarCondicoesIniciais() {
        var urlHelperUtil = new UrlHelperUtil();
        Assertions.assertEquals("/",urlHelperUtil.getBasePath());
        Assertions.assertTrue(urlHelperUtil.getOpenPaths().isEmpty());
        Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().isEmpty());
    }

    @Test
    @DisplayName("deve validar setter getter de basePath")
    void deveValidarSetterGetterBasePath() {
        var urlHelperUtil = new UrlHelperUtil();

        urlHelperUtil.setBasePath("");
        Assertions.assertEquals("/",urlHelperUtil.getBasePath());

        urlHelperUtil.setBasePath(" ");
        Assertions.assertEquals("/",urlHelperUtil.getBasePath());

        urlHelperUtil.setBasePath(null);
        Assertions.assertEquals("/",urlHelperUtil.getBasePath());

        urlHelperUtil.setBasePath("//");
        Assertions.assertEquals("//",urlHelperUtil.getBasePath());

        urlHelperUtil.setBasePath(" / / ");
        Assertions.assertEquals("/ /",urlHelperUtil.getBasePath());
    }

    @Test
    @DisplayName("deve validar setter getter de openPaths")
    void deveValidarSetterGetterOpenPaths() {
        var urlHelperUtil = new UrlHelperUtil();

        {
            urlHelperUtil.setOpenPaths(null);
            Assertions.assertTrue(urlHelperUtil.getOpenPaths().isEmpty());
        }

        {
            urlHelperUtil.setOpenPaths(List.of());
            Assertions.assertTrue(urlHelperUtil.getOpenPaths().isEmpty());
        }

        {
            urlHelperUtil.setOpenPaths(new ArrayList<>());
            Assertions.assertTrue(urlHelperUtil.getOpenPaths().isEmpty());
        }

        {
            urlHelperUtil.setOpenPaths(List.of("/path/method", "/path/method"));
            Assertions.assertTrue(urlHelperUtil.getOpenPaths().stream().anyMatch(s -> s.equals("/path/method")));
            Assertions.assertEquals(1L, urlHelperUtil.getOpenPaths().size());
        }

        {
            urlHelperUtil.setOpenPaths(List.of("/path/method-a", "/path/method-b"));
            Assertions.assertTrue(urlHelperUtil.getOpenPaths().stream().anyMatch(s -> s.equals("/path/method-a")));
            Assertions.assertTrue(urlHelperUtil.getOpenPaths().stream().anyMatch(s -> s.equals("/path/method-b")));
            Assertions.assertEquals(2L, urlHelperUtil.getOpenPaths().size());
        }
    }

    @Test
    @DisplayName("deve validar setter getter de AcceptedPaths")
    void deveValidarSetterGetterAcceptedPaths() {
        var urlHelperUtil = new UrlHelperUtil();

        {
            urlHelperUtil.setAcceptedPaths(null);
            Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().isEmpty());
        }

        {
            urlHelperUtil.setAcceptedPaths(List.of());
            Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().isEmpty());
        }

        {
            urlHelperUtil.setAcceptedPaths(new ArrayList<>());
            Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().isEmpty());
        }

        {
            urlHelperUtil.setAcceptedPaths(List.of("/path/method", "/path/method"));
            Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().stream().anyMatch(s -> s.equals("/path/method")));
            Assertions.assertEquals(1L, urlHelperUtil.getAcceptedPaths().size());
        }

        {
            urlHelperUtil.setAcceptedPaths(List.of("/path/method-a", "/path/method-b"));
            Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().stream().anyMatch(s -> s.equals("/path/method-a")));
            Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().stream().anyMatch(s -> s.equals("/path/method-b")));
            Assertions.assertEquals(2L, urlHelperUtil.getAcceptedPaths().size());
        }
    }

    @Test
    @DisplayName("deve validar metodo extractContextPath")
    void deveValidarMetodoExtractContextPath() {
        var urlHelperUtil = new UrlHelperUtil();
        Assertions.assertEquals("/",urlHelperUtil.extractContextPath("test"));
        Assertions.assertEquals("/",urlHelperUtil.extractContextPath("/"));
        Assertions.assertEquals("/",urlHelperUtil.extractContextPath(""));
        Assertions.assertEquals("/",urlHelperUtil.extractContextPath(null));
        Assertions.assertEquals("/",urlHelperUtil.extractContextPath(" / "));
        Assertions.assertEquals("/",urlHelperUtil.extractContextPath(" / / "));
        Assertions.assertEquals("/",urlHelperUtil.extractContextPath("//"));
        Assertions.assertEquals("/ /",urlHelperUtil.extractContextPath(" / / // "));
        Assertions.assertEquals("/",urlHelperUtil.extractContextPath("/api"));
        Assertions.assertEquals("/",urlHelperUtil.extractContextPath("/api/"));
        Assertions.assertEquals("/api",urlHelperUtil.extractContextPath("/api/v1"));
        Assertions.assertEquals("/api",urlHelperUtil.extractContextPath("/api/v1/"));
    }

    @Test
    @DisplayName("deve validar metodo isOpenPaths")
    void deveValidarMetodoIsOpenPaths() {

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            urlHelperUtil.setOpenPaths(List.of("/"));
            Assertions.assertTrue(urlHelperUtil.isOpenPath("/"));
//            Assertions.assertTrue(urlHelperUtil.isOpenPath("/v1"));
        }

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            urlHelperUtil.setOpenPaths(List.of("/**"));
            Assertions.assertTrue(urlHelperUtil.isOpenPath("/"));
            Assertions.assertTrue(urlHelperUtil.isOpenPath("/v1"));
        }

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            urlHelperUtil.setOpenPaths(List.of("/v1/**"));
            Assertions.assertFalse(urlHelperUtil.isOpenPath(null));
            Assertions.assertFalse(urlHelperUtil.isOpenPath(""));
            Assertions.assertFalse(urlHelperUtil.isOpenPath(" "));
            Assertions.assertFalse(urlHelperUtil.isOpenPath(" / "));
            Assertions.assertFalse(urlHelperUtil.isOpenPath("/"));
            Assertions.assertFalse(urlHelperUtil.isOpenPath("/v1/"));
            Assertions.assertTrue(urlHelperUtil.isOpenPath("/v1/test"));
        }
    }
}
