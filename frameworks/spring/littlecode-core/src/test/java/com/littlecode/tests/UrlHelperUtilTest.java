package com.littlecode.tests;

import com.littlecode.parsers.UrlHelperUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UrlHelperUtilTest {


    @Test
    @DisplayName("deve validar condições iniciais")
    void deveValidarCondicoesIniciais() {
        var urlHelperUtil = new UrlHelperUtil();
        Assertions.assertEquals("/", urlHelperUtil.getBasePath());
        Assertions.assertTrue(urlHelperUtil.getOpenPaths().isEmpty());
        Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().isEmpty());
    }

    @Test
    @DisplayName("deve validar isValidUrl")
    void deveValidar_isValidUrl() {
        {
            Assertions.assertFalse(UrlHelperUtil.isValidUrl("/"));
            Assertions.assertFalse(UrlHelperUtil.isValidUrl(" / "));
            Assertions.assertFalse(UrlHelperUtil.isValidUrl(""));
            Assertions.assertFalse(UrlHelperUtil.isValidUrl(" "));
            Assertions.assertFalse(UrlHelperUtil.isValidUrl("/"));
            Assertions.assertFalse(UrlHelperUtil.isValidUrl((String) null));
            Assertions.assertFalse(UrlHelperUtil.isValidUrl((URI) null));
        }

        {
            var uri = URI.create("http://localhost:8080/path/method");
            Assertions.assertTrue(UrlHelperUtil.isValidUrl(uri.toString()));
            Assertions.assertTrue(UrlHelperUtil.isValidUrl(uri));
        }

        {
            var uri = URI.create("http://localhost:8080");
            Assertions.assertTrue(UrlHelperUtil.isValidUrl(uri.toString()));
            Assertions.assertTrue(UrlHelperUtil.isValidUrl(uri));
        }

        {
            var uri = URI.create("/localhost:8080/");
            Assertions.assertFalse(UrlHelperUtil.isValidUrl(uri.toString()));
            Assertions.assertFalse(UrlHelperUtil.isValidUrl(uri));
        }

        {
            var uri = URI.create("/");
            Assertions.assertFalse(UrlHelperUtil.isValidUrl(uri.toString()));
            Assertions.assertFalse(UrlHelperUtil.isValidUrl(uri));
        }
    }

    @Test
    @DisplayName("deve validar setter getter de basePath")
    void deveValidarSetterGetterBasePath() {
        var urlHelperUtil = new UrlHelperUtil();

        urlHelperUtil.setBasePath("");
        Assertions.assertEquals("/", urlHelperUtil.getBasePath());

        urlHelperUtil.setBasePath(" ");
        Assertions.assertEquals("/", urlHelperUtil.getBasePath());

        urlHelperUtil.setBasePath(null);
        Assertions.assertEquals("/", urlHelperUtil.getBasePath());

        urlHelperUtil.setBasePath("//");
        Assertions.assertEquals("//", urlHelperUtil.getBasePath());

        urlHelperUtil.setBasePath(" / / ");
        Assertions.assertEquals("/ /", urlHelperUtil.getBasePath());
    }

    @Test
    @DisplayName("deve validar parserPaths")
    void deveValidar_parserPaths() {
        var urlHelperUtil = new UrlHelperUtil();

        {
            var list = urlHelperUtil.parserPaths("/path/method", "/path/method", "");
            Assertions.assertTrue(list.stream().anyMatch(s -> s.equals("/path/method")));
            Assertions.assertEquals(1L, list.size());
        }

        {
            urlHelperUtil.setAcceptedPaths(List.of("", " ", "/path/method-a", "/path/method-b"));
            Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().stream().anyMatch(s -> s.equals("/path/method-a")));
            Assertions.assertTrue(urlHelperUtil.getAcceptedPaths().stream().anyMatch(s -> s.equals("/path/method-b")));
            Assertions.assertEquals(2L, urlHelperUtil.getAcceptedPaths().size());
        }
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
    @DisplayName("deve validar metodo matchPath")
    void deveValidarMetodo_matchPath() {

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            List<String> paths = new ArrayList<>();
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, ""));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, " "));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, null));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, "/"));
        }

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            var paths = List.of("/");
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/"));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, "/v1"));
        }

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            var paths = List.of("/**");
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/"));
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/v1"));
        }

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            var paths = List.of("/v1/**");
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, "/"));
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/v1"));
        }

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            var paths = List.of("/**");
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/"));
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/v1"));
        }

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            var paths = List.of("/v1/**");
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, null));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, ""));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, " "));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, " / "));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, "/"));
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/v1/"));
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/v1/test"));
        }

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("/");
            var paths = List.of("/v2/**");
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, null));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, ""));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, " "));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, " / "));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, "/"));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, "/v1/"));
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/v2/test"));
        }

        {
            var urlHelperUtil = new UrlHelperUtil();
            urlHelperUtil.setBasePath("");
            var paths = List.of("/v1");
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, null));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, ""));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, " "));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, " / "));
            Assertions.assertFalse(urlHelperUtil.matchPath(paths, "/"));
            Assertions.assertTrue(urlHelperUtil.matchPath(paths, "/v1/"));
        }
    }


    @Test
    @DisplayName("deve validar metodo isOpenPath")
    void deveValidarMetodo_isOpenPath() {
        UrlHelperUtil urlHelperUtil = new UrlHelperUtil();
        urlHelperUtil.setOpenPaths(List.of("/**"));
        Assertions.assertTrue(urlHelperUtil.isOpenPath("/"));

    }

    @Test
    @DisplayName("deve validar metodo setAcceptedPaths")
    void deveValidarMetodo_setAcceptedPaths() {
        UrlHelperUtil urlHelperUtil = new UrlHelperUtil();
        urlHelperUtil.setAcceptedPaths(List.of("/**"));
        Assertions.assertTrue(urlHelperUtil.isAcceptedPath("/"));
    }

    @Test
    @DisplayName("deve validar metodo isBlockedPath")
    void deveValidarMetodo_isBlockedPath() {
        UrlHelperUtil urlHelperUtil = new UrlHelperUtil();
        urlHelperUtil.setBlockedPaths(List.of("/**"));
        Assertions.assertTrue(urlHelperUtil.isBlockedPath("/"));
    }

    @Test
    @DisplayName("deve validar metodo isBackendPath")
    void deveValidarMetodo_isBackendPath() {
        UrlHelperUtil urlHelperUtil = new UrlHelperUtil();
        urlHelperUtil.setBackendPaths(List.of("/**"));
        Assertions.assertTrue(urlHelperUtil.isBackendPath("/"));
    }

    @Test
    @DisplayName("deve validar metodo isBackendOpenPath")
    void deveValidarMetodo_isBackendOpenPath() {
        UrlHelperUtil urlHelperUtil = new UrlHelperUtil();
        urlHelperUtil.setBackendOpenPaths(List.of("/**"));
        Assertions.assertTrue(urlHelperUtil.isBackendOpenPath("/"));
    }

}
