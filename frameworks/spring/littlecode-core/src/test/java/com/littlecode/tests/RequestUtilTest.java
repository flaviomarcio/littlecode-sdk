package com.littlecode.tests;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.network.RequestUtil;
import com.littlecode.network.clients.Http;
import com.littlecode.network.clients.RequestClient;
import com.littlecode.parsers.ObjectUtil;
import com.sun.net.httpserver.HttpServer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.service.annotation.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RequestUtilTest {

    public static HttpClient mock_httpClient;

    @BeforeAll()
    public static void setup(){
        mock_httpClient= Mockito.mock(HttpClient.class);


    }

    @Test
    @DisplayName("Deve validar methods")
    public void UT_000_CHECK_CONSTRUCTOR_GETTER() {
        Assertions.assertDoesNotThrow(() -> RequestUtil.builder().build());
        var request=RequestUtil.builder().build();
        Assertions.assertDoesNotThrow(request::getClient);
        Assertions.assertDoesNotThrow(request::getOnFinished);
        Assertions.assertDoesNotThrow(request::getOnFail);
        Assertions.assertDoesNotThrow(request::getOnStarted);
        Assertions.assertDoesNotThrow(request::getOnSuccessful);

        Assertions.assertDoesNotThrow(request::isPrintOnFail);
        Assertions.assertDoesNotThrow(() -> request.setPrintOnFail(false));
        Assertions.assertDoesNotThrow(request::isExceptionOnFail);
        Assertions.assertDoesNotThrow(() -> request.setExceptionOnFail(false));
        Assertions.assertDoesNotThrow(request::getBody);

        Assertions.assertDoesNotThrow(()->request.client(new Http()));
        Assertions.assertDoesNotThrow(()->request.onFinished(null));
        Assertions.assertDoesNotThrow(()->request.onFail(null));
        Assertions.assertDoesNotThrow(()->request.onStarted(null));
        Assertions.assertDoesNotThrow(()->request.onSuccessful(null));
    }

    @Test
    @DisplayName("Deve validar methods")
    public void UT_000_CHECK_METHODS() {
        var request=RequestUtil.builder().build();
        Assertions.assertEquals(request.GET().method(), RequestUtil.Method.GET);
        Assertions.assertEquals(request.POST().method(), RequestUtil.Method.POST);
        Assertions.assertEquals(request.PUT().method(), RequestUtil.Method.PUT);
        Assertions.assertEquals(request.DELETE().method(), RequestUtil.Method.DELETE);
    }


    @Test
    @DisplayName("Deve validar Response")
    public void UT_000_CHECK_RESPONSE() {
        Assertions.assertDoesNotThrow(()-> new RequestUtil.Response(null));
        var response=(new RequestUtil()).response();
        response.setStatus(-1);
        Assertions.assertDoesNotThrow(response::toString);
        response.setStatus(200);
        Assertions.assertDoesNotThrow(response::toString);
        response.setStatus(201);
        Assertions.assertDoesNotThrow(response::toString);
        response.setStatus(202);
        Assertions.assertDoesNotThrow(response::toString);
        Assertions.assertDoesNotThrow(response::isOK);
        Assertions.assertDoesNotThrow(response::status);
        Assertions.assertDoesNotThrow(response::reasonPhrase);
        Assertions.assertDoesNotThrow(response::request);
        Assertions.assertDoesNotThrow(response::body);
        Assertions.assertDoesNotThrow(() -> response.bodyAs(Object.class));
        Assertions.assertDoesNotThrow(() -> response.bodyAs(Object.class));
        Assertions.assertDoesNotThrow(() -> response.bodyAsList(List.class));


        response.setBody("{}");
        Assertions.assertNotNull(response.bodyAs(PrivateObject.class));
        Assertions.assertFalse(response.bodyAsList(PrivateObject.class).isEmpty());

        response.setBody("[]");
        Assertions.assertNull(response.bodyAs(PrivateObject.class));
        Assertions.assertTrue(response.bodyAsList(PrivateObject.class).isEmpty());

        response.setBody("");
        Assertions.assertNull(response.bodyAs(PrivateObject.class));
        Assertions.assertTrue(response.bodyAsList(PrivateObject.class).isEmpty());;

        response.setBody(null);
        Assertions.assertNull(response.bodyAs(PrivateObject.class));
        Assertions.assertTrue(response.bodyAsList(PrivateObject.class).isEmpty());

        var itemA=new PrivateObject(UUID.randomUUID());
        var itemB=new PrivateObject(UUID.randomUUID());

        response.setBody(ObjectUtil.toString(itemA));
        Assertions.assertNotNull(response.bodyAs(PrivateObject.class));
        Assertions.assertEquals(response.bodyAs(PrivateObject.class).getId(),itemA.getId());
        Assertions.assertFalse(response.bodyAsList(PrivateObject.class).isEmpty());
        Assertions.assertEquals(response.bodyAsList(PrivateObject.class).get(0).getId(),itemA.getId());

        response.setBody(ObjectUtil.toString(List.of(itemA, itemB)));
        Assertions.assertDoesNotThrow(() -> response.bodyAsList(PrivateObject.class));
        Assertions.assertEquals(response.bodyAsList(PrivateObject.class).get(0).getId(),itemA.getId());
        Assertions.assertEquals(response.bodyAsList(PrivateObject.class).get(1).getId(),itemB.getId());

    }



    @Test
    @DisplayName("Deve validar methods")
    public void UT_000_CHECK_REQUEST_UTIL() {

        Assertions.assertDoesNotThrow(RequestUtil::new);
        Assertions.assertDoesNotThrow(() -> RequestUtil.builder().build());
        var request=RequestUtil.builder().build();
        Assertions.assertDoesNotThrow(request::clear);
        Assertions.assertDoesNotThrow(request::toString);
        Assertions.assertDoesNotThrow(() -> request.response().getRequest());
        Assertions.assertDoesNotThrow(() -> request.response().getUrl());
        Assertions.assertDoesNotThrow(() -> request.response().getBody());
        Assertions.assertDoesNotThrow(() -> request.response().getHeaders());
        Assertions.assertDoesNotThrow(() -> request.response().getStatus());
        Assertions.assertDoesNotThrow(() -> request.response().getReasonPhrase());

        Assertions.assertDoesNotThrow(() -> request.response().setUrl(null));
        Assertions.assertDoesNotThrow(() -> request.response().setBody(null));
        Assertions.assertDoesNotThrow(() -> request.response().setHeaders(null));
        Assertions.assertDoesNotThrow(() -> request.response().setStatus(200));
        Assertions.assertDoesNotThrow(() -> request.response().setReasonPhrase(null));

        Assertions.assertDoesNotThrow(() -> {
            request.response().setBody("{\"id\":\"teste\"}");
            request.response().bodyAs(null);
            request.response().bodyAsList(null);
            request.response().bodyAs(Object.class);
            request.response().bodyAsList(Object.class);
            request.response().bodyAsList(UUID.class);
            request.response().setBody("[{\"id\":\"teste\"}]");
            request.response().bodyAsList(Object.class);
            request.response().bodyAsList(Object.class);
        });

        Assertions.assertDoesNotThrow(() -> request.response().setHeaders(Map.of("header","value")));
        Assertions.assertDoesNotThrow(() -> request.response().clear());
        Assertions.assertDoesNotThrow(() -> request.response().toString());
        Assertions.assertDoesNotThrow(() -> {
            request.response().toString();
            request.response().setStatus(200);
            request.response().toString();
        });
        Assertions.assertDoesNotThrow(request::CONNECT);
        Assertions.assertDoesNotThrow(request::DELETE);
        Assertions.assertDoesNotThrow(request::GET);
        Assertions.assertDoesNotThrow(request::HEAD);
        Assertions.assertDoesNotThrow(request::LIST);
        Assertions.assertDoesNotThrow(request::OPTIONS);
        Assertions.assertDoesNotThrow(request::PATCH);
        Assertions.assertDoesNotThrow(request::POST);
        Assertions.assertDoesNotThrow(request::PUT);
        Assertions.assertDoesNotThrow(request::TRACE);        Assertions.assertDoesNotThrow(() -> request.method());
        Assertions.assertDoesNotThrow(() -> request.method(RequestUtil.Method.POST));
        Assertions.assertDoesNotThrow(() -> request.method(null));
        Assertions.assertDoesNotThrow(() -> request.uri());
        Assertions.assertDoesNotThrow(() -> request.uri("https://littlecode.com"));
        Assertions.assertDoesNotThrow(() -> request.uri(URI.create("https://littlecode.com")));
        Assertions.assertDoesNotThrow(request::url);
        Assertions.assertDoesNotThrow(() -> request.headers());
        Assertions.assertDoesNotThrow(() -> request.headers("a","b"));
        Assertions.assertDoesNotThrow(() -> request.headers("a",null));
        Assertions.assertDoesNotThrow(() -> request.headers(null,"b"));
        Assertions.assertDoesNotThrow(() -> request.headers(null,null));
        Assertions.assertDoesNotThrow(() -> request.headers(Map.of("teste","teste")));
        Assertions.assertDoesNotThrow(request::headersJSON);
        Assertions.assertDoesNotThrow(() -> request.headersAuthBasic(""));
        Assertions.assertDoesNotThrow(() -> request.headersAuthBasic(null));
        Assertions.assertDoesNotThrow(() -> request.headersAuthBasic("a","b"));
        Assertions.assertDoesNotThrow(() -> request.headersAuthBasic("a",null));
        Assertions.assertDoesNotThrow(() -> request.headersAuthBasic(null,"b"));
        Assertions.assertDoesNotThrow(() -> request.headersAuthBasic(null,null));
        Assertions.assertDoesNotThrow(() -> request.headersAuthBearer(""));
        Assertions.assertDoesNotThrow(() -> request.headersAuthBearer(null));
        Assertions.assertDoesNotThrow(request::getBody);
        Assertions.assertDoesNotThrow(() -> request.body(new Object()));
        Assertions.assertDoesNotThrow(() -> request.body(""));
        Assertions.assertDoesNotThrow(() -> request.timeout(10));
        Assertions.assertDoesNotThrow(() -> request.body((String)null ));
        Assertions.assertDoesNotThrow(() -> request.body((Object) null));
        Assertions.assertDoesNotThrow(() -> request.body("teste"));
        Assertions.assertDoesNotThrow(() -> request.response().getBody());
        Assertions.assertDoesNotThrow(() -> request.onStarted(null));
        Assertions.assertDoesNotThrow(() -> request.onFail(null));
        Assertions.assertDoesNotThrow(() -> request.onSuccessful(null));
        Assertions.assertDoesNotThrow(() -> request.onFinished(null));
        Assertions.assertDoesNotThrow(request::isOK);
        Assertions.assertDoesNotThrow(() -> request.exceptionOnFail());
        Assertions.assertDoesNotThrow(() -> request.exceptionOnFail(false));
        Assertions.assertDoesNotThrow(() -> request.path("/teste"));
        Assertions.assertDoesNotThrow(() -> request.path(null));
        Assertions.assertDoesNotThrow(() -> request.path());


    }

    @Test
    @DisplayName("Deve validar metodo prints")
    public void UT_CHECK_METHOD_PRINT() {
        var request=RequestUtil.builder().build();
        Assertions.assertDoesNotThrow(() -> request.printOnFail());
        Assertions.assertDoesNotThrow(() -> request.headers("a","b"));
        Assertions.assertDoesNotThrow(request::POST);
        Assertions.assertDoesNotThrow(request::printLines);
        Assertions.assertDoesNotThrow(() -> request.printOnFail(false));
        Assertions.assertDoesNotThrow(request::printLines);
        Assertions.assertDoesNotThrow(request::print);
    }

    @Test
    @DisplayName("Deve validar metodo call com sucesso")
    public void UT_CHECK_METHOD_CALL_COM_SUCESSO() {
        var request=RequestUtil.builder().build();
        request
                .client(new PrivateRequestClient())
                .uri("http://localhost:8080")
                .path("/test")
                .onStarted(() -> {})
                .onFail(() -> {})
                .onSuccessful(() -> {})
                .onFinished(() -> {});
        Assertions.assertDoesNotThrow(request::getClient);
        Assertions.assertDoesNotThrow(() -> request.uri((URI)null).uri());
        Assertions.assertDoesNotThrow(() -> request.path(" ").path());
        Assertions.assertDoesNotThrow(() -> request.path("").path());
        Assertions.assertDoesNotThrow(() -> request.path(null).path());
        Assertions.assertDoesNotThrow(() -> request.uri(" ").uri());
        Assertions.assertDoesNotThrow(() -> request.uri("http://localhost:8080").uri());
        Assertions.assertDoesNotThrow(() -> request.uri((String)null).uri());
        Assertions.assertDoesNotThrow(() -> request.client(new PrivateRequestClient()));
        Assertions.assertDoesNotThrow(request::call);
        Assertions.assertDoesNotThrow(request::response);
    }

    @Test
    @DisplayName("Deve validar metodo call com falha")
    public void UT_CHECK_METHOD_CALL_COM_FALHA() {
        var request=RequestUtil.builder().build();
        request
                .uri("http://localhost:8080")
                .path("/test")
                .onStarted(() -> {})
                .onFail(() -> {})
                .onSuccessful(() -> {})
                .onFinished(() -> {});
        Assertions.assertDoesNotThrow(request::getClient);
        Assertions.assertDoesNotThrow(() -> request.uri((URI)null).uri());
        Assertions.assertDoesNotThrow(() -> request.path(" ").path());
        Assertions.assertDoesNotThrow(() -> request.path("").path());
        Assertions.assertDoesNotThrow(() -> request.path(null).path());
        Assertions.assertDoesNotThrow(() -> request.uri(" ").uri());
        Assertions.assertDoesNotThrow(() -> request.uri("http://localhost:8080").uri());
        Assertions.assertDoesNotThrow(() -> request.uri((String)null).uri());

        request.exceptionOnFail(false);
        request.printOnFail(false);
        Assertions.assertDoesNotThrow(request::call);
        Assertions.assertDoesNotThrow(request::response);

        request.exceptionOnFail(true);
        request.printOnFail(false);
        Assertions.assertThrows(FrameworkException.class, ()-> request.call());

        request.exceptionOnFail(false);
        request.printOnFail(true);
        Assertions.assertDoesNotThrow(()-> request.CONNECT().call());

        for(var method: RequestUtil.Method.values()){
            Assertions.assertDoesNotThrow(()-> request.body("[1,2,3,4]").method(method).call());
            Assertions.assertDoesNotThrow(()-> request.body(null).method(method).call());

        }

    }

    @Test
    @DisplayName("Deve validar class Http com sucesso")
    public void UT_CHECK_CLASS_HTTP_COM_SUCESSO() {
        Assertions.assertDoesNotThrow(Http::new);
        {
            var request=RequestUtil.builder().build();
            Assertions.assertDoesNotThrow(() -> request.client(new Http()));
            Assertions.assertThrows(NullPointerException.class, () -> request.client(null));
        }
        var rq=RequestUtil.builder().build();

        rq
                .client(new PrivateRequestClient())
                .uri("http://localhost:8080")
                .path("/test")
                .onStarted(() -> {})
                .onFail(() -> {})
                .onSuccessful(() -> {})
                .onFinished(() -> {});

        Assertions.assertDoesNotThrow(() -> rq.GET().call());
        Assertions.assertDoesNotThrow(() -> rq.POST().call());
        Assertions.assertDoesNotThrow(() -> rq.POST().body(null).call());
        Assertions.assertDoesNotThrow(() -> rq.POST().body("{}").call());
        Assertions.assertDoesNotThrow(() -> rq.PUT().call());
        Assertions.assertDoesNotThrow(() -> rq.DELETE().call());
    }

    @Test
    @DisplayName("Deve validar class Execute")
    public void UT_CHECK_CLASS_EXECUTE() {
        Assertions.assertDoesNotThrow(() -> {
            RequestUtil.Executable e=new RequestUtil.Executable() {
                @Override
                public void execute() {

                }
            };
            e.execute();
        });
    }

    public static class PrivateRequestClient implements RequestClient{
        @Override
        public void call(RequestUtil rqUtil) {
            rqUtil.response().setStatus(200);
        }

        @Override
        public HttpClient createHttpClient(RequestUtil rqUtil) {
            return mock_httpClient;
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrivateObject{
        private UUID id;
    }

}
