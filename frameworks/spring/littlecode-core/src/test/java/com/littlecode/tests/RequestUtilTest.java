package com.littlecode.tests;

import com.littlecode.network.RequestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URI;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class RequestUtilTest {

    @Test
    public void UT_000_CHECK_RESPONSE() {
        Assertions.assertDoesNotThrow(()-> RequestUtil.Response.builder().status(-1).build());
        Assertions.assertDoesNotThrow(()->new RequestUtil.Response());
        var response=new RequestUtil.Response();
        Assertions.assertDoesNotThrow(response::isOK);
        Assertions.assertDoesNotThrow(response::status);
        Assertions.assertDoesNotThrow(response::reasonPhrase);
        Assertions.assertDoesNotThrow(response::request);
        Assertions.assertDoesNotThrow(response::body);
        Assertions.assertDoesNotThrow(() -> response.bodyAs(Object.class));
        Assertions.assertDoesNotThrow(() -> response.bodyAs(Object.class));
        Assertions.assertDoesNotThrow(() -> response.bodyAsList(List.class));

    }

    @Test
    public void UT_000_CHECK_REQUEST_UTIL() {

        Assertions.assertDoesNotThrow(RequestUtil::new);
        Assertions.assertDoesNotThrow(() -> RequestUtil.builder().build());
        var request=RequestUtil.builder().build();
        Assertions.assertDoesNotThrow(request::toString);
        Assertions.assertDoesNotThrow(() -> request.printOnFail());
        Assertions.assertDoesNotThrow(() -> request.printOnFail(false));
        Assertions.assertDoesNotThrow(request::POST);
        Assertions.assertDoesNotThrow(request::PUT);
        Assertions.assertDoesNotThrow(request::GET);
        Assertions.assertDoesNotThrow(request::DELETE);
        Assertions.assertDoesNotThrow(() -> request.method());
        Assertions.assertDoesNotThrow(() -> request.method(RequestUtil.Method.POST));
        Assertions.assertDoesNotThrow(() -> request.uri());
        Assertions.assertDoesNotThrow(() -> request.uri("https://littlecode.com"));
        Assertions.assertDoesNotThrow(() -> request.uri(URI.create("https://littlecode.com")));
        Assertions.assertDoesNotThrow(request::url);
        Assertions.assertDoesNotThrow(request::print);
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
        Assertions.assertDoesNotThrow(() -> request.body());
        Assertions.assertDoesNotThrow(() -> request.body(new Object()));
        Assertions.assertDoesNotThrow(() -> request.body(""));
        Assertions.assertDoesNotThrow(() -> request.body((String)null ));
        Assertions.assertDoesNotThrow(() -> request.body((Object) null));
        Assertions.assertDoesNotThrow(() -> request.body("teste"));
        Assertions.assertDoesNotThrow(request::response);
        Assertions.assertDoesNotThrow(request::responseBody);
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

}
