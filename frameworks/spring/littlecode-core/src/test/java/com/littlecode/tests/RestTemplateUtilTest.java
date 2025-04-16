package com.littlecode.tests;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.network.RestTemplateUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class RestTemplateUtilTest {

    @Test
    @DisplayName("Deve validar Method Url Uri")
    public void deveValidarMethod_Url() {
        {//uri url
            var util = RestTemplateUtil.builder().build();
            Assertions.assertDoesNotThrow(() -> {
                util.setUrl(null);
                Assertions.assertEquals(util.getUrl(), "http://localhost");

                util.setUrl("http://localhost:8080");
                Assertions.assertEquals(util.getUrl(), "http://localhost:8080");
            });
        }
    }

    @Test
    @DisplayName("Deve validar Method Headers")
    public void deveValidarMethod_Headers() {
        {
            var util = RestTemplateUtil.builder().build();
            util.setHeaders(null);
            Assertions.assertNotNull(util.getHeaders());
            Assertions.assertTrue(util.getHeaders().isEmpty());
        }

        {
            var util = RestTemplateUtil.builder().build();
            var httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.ACCEPT, "application/json");
            util.setHeaders(httpHeaders);
            Assertions.assertNotNull(util.getHeaders());
            Assertions.assertFalse(util.getHeaders().isEmpty());
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .build();
            Assertions.assertTrue(util.getHeaders().isEmpty());
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .headers(new HttpHeaders())
                    .headers(new HashMap<>())
                    .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .build();
            Assertions.assertTrue(util.getHeaders().containsKey(HttpHeaders.ACCEPT));
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .header(HttpHeaders.ACCEPT, List.of(MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE))
                    .build();
            Assertions.assertTrue(util.getHeaders().containsKey(HttpHeaders.ACCEPT));
        }
    }

    @Test
    @DisplayName("Deve validar Method host")
    public void deveValidarMethod_host() {
        {
            var util=RestTemplateUtil
                    .builder()
                    .host("remotehost")
                    .build();
            Assertions.assertTrue(util.getUrl().startsWith("http://remotehost"));
        }
        {
            var util = RestTemplateUtil
                    .builder()
                    .host("")
                    .build();
            Assertions.assertTrue(util.getUrl().startsWith("http://localhost"));
        }
        {
            var util = RestTemplateUtil
                    .builder()
                    .host(null)
                    .build();
            Assertions.assertTrue(util.getUrl().startsWith("http://localhost"));
        }
    }

    @Test
    @DisplayName("Deve validar Method port")
    public void deveValidarMethod_port() {
        {
            var util = RestTemplateUtil
                    .builder()
                    .port(8181)
                    .build();
            Assertions.assertTrue(util.getUrl().startsWith("http://localhost:8181"));
        }
    }

    @Test
    @DisplayName("Deve validar Method queryParams")
    public void deveValidarMethod_queryParams() {
        {
            var util = RestTemplateUtil
                    .builder()
                    .queryParams(Map.of("key","value"))
                    .build();
            var uri=UriComponentsBuilder.fromHttpUrl(util.getUrl()).build();

            Assertions.assertTrue(uri.getQueryParams().containsKey("key"));
            Assertions.assertEquals(uri.getQueryParams().get("key").stream().findFirst().get(),"value");
            Assertions.assertTrue(util.getUrl().startsWith("http://localhost?key=value"));
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .queryParam("key","value")
                    .build();
            var uri=UriComponentsBuilder.fromHttpUrl(util.getUrl()).build();

            Assertions.assertTrue(uri.getQueryParams().containsKey("key"));
            Assertions.assertEquals(uri.getQueryParams().get("key").stream().findFirst().get(),"value");
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .queryParam("key","")
                    .build();
            var uri=UriComponentsBuilder.fromHttpUrl(util.getUrl()).build();

            Assertions.assertTrue(uri.getQueryParams().containsKey("key"));
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .queryParam("","value")
                    .build();
            var uri=UriComponentsBuilder.fromHttpUrl(util.getUrl()).build();

            Assertions.assertTrue(uri.getQueryParams().isEmpty());
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .queryParams(new HttpHeaders())//new MultiValueMap
                    .build();
            var uri=UriComponentsBuilder.fromHttpUrl(util.getUrl()).build();
            Assertions.assertTrue(uri.getQueryParams().isEmpty());
        }
    }

    @Test
    @DisplayName("Deve validar Method Schema")
    public void deveValidarMethod_Schema() {

        {
            var util = RestTemplateUtil
                    .builder()
                    .scheme("http")
                    .build();
            Assertions.assertTrue(util.getUrl().startsWith("http://"));
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .scheme("https")
                    .build();
            Assertions.assertTrue(util.getUrl().startsWith("https://"));
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .scheme(null)
                    .build();
            Assertions.assertTrue(util.getUrl().startsWith("http://"));
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .scheme("")
                    .build();
            Assertions.assertTrue(util.getUrl().startsWith("http://"));
        }
    }

    @Test
    @DisplayName("Deve validar Method Path")
    public void deveValidarMethod_Path() {
        {
            var util = RestTemplateUtil
                    .builder()
                    .path("/v1")
                    .build();
            Assertions.assertEquals(util.getUrl(),"http://localhost/v1");
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .path("")
                    .build();
            Assertions.assertEquals(util.getUrl(),"http://localhost");
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .path("/")
                    .build();
            Assertions.assertEquals(util.getUrl(),"http://localhost");
        }

        {
            var util = RestTemplateUtil
                    .builder()
                    .path(null)
                    .build();
            Assertions.assertEquals(util.getUrl(),"http://localhost");
        }
    }

    @Test
    @DisplayName("Deve validar Method Methods")
    public void deveValidarMethod_Methods() {
        {//methods
            Assertions.assertDoesNotThrow(() -> {
                var util = RestTemplateUtil
                        .builder()
                        .method((HttpMethod)null)
                        .method((Object) null)
                        .build();
                util.setMethod(null);
                Assertions.assertEquals(util.getMethod(), HttpMethod.GET);
            });

            Assertions.assertDoesNotThrow(() -> {
                var util = RestTemplateUtil.builder().build();
                util.setMethod(null);
                Assertions.assertEquals(util.getMethod(), HttpMethod.GET);
            });

            Assertions.assertDoesNotThrow(() -> {
                var util = RestTemplateUtil.builder().build();
                util.setMethod("");
                Assertions.assertEquals(util.getMethod(), HttpMethod.GET);
            });

            Assertions.assertDoesNotThrow(() -> {
                var util = RestTemplateUtil.builder().build();
                util.setMethod("????");
                Assertions.assertEquals(util.getMethod(), HttpMethod.GET);
            });

            Assertions.assertDoesNotThrow(() -> {
                {
                    var util = RestTemplateUtil
                            .builder()
                            .method(HttpMethod.POST)
                            .build();
                    Assertions.assertEquals(util.getMethod(), HttpMethod.POST);
                }

                {
                    var util = RestTemplateUtil
                            .builder()
                            .method(HttpMethod.POST.name())
                            .build();
                    Assertions.assertEquals(util.getMethod(), HttpMethod.POST);
                }
            });

            for (var method : HttpMethod.values()) {
                Assertions.assertDoesNotThrow(() -> {
                    var util = RestTemplateUtil.builder().build();

                    util.setMethod(method);
                    Assertions.assertEquals(util.getMethod(), method);

                    util.setMethod(method.name());
                    Assertions.assertEquals(util.getMethod(), method);

                    util.setMethod(method.name().toLowerCase());
                    Assertions.assertEquals(util.getMethod(), method);

                    util.setMethod(method.name().toUpperCase());
                    Assertions.assertEquals(util.getMethod(), method);
                });
            }
        }
    }

    @Test
    @DisplayName("Deve validar Method Body")
    public void deveValidarMethod_Body() {
        {
            var util = RestTemplateUtil
                    .builder()
                    .body("?")
                    .build();
            Assertions.assertEquals(util.getUrl(), "http://localhost");
            Assertions.assertEquals(util.getBody(), "?");
        }

        {
            var lst=List.of(1,2,3,4);
            var util = RestTemplateUtil
                    .builder()
                    .body(lst)
                    .build();
            Assertions.assertEquals(util.getUrl(), "http://localhost");
            Assertions.assertEquals(util.getBody(), lst);
        }
    }

    @Test
    @DisplayName("Deve validar Method exchange")
    public void deveValidarMethods_exchange() {
        var restTemplate=Mockito.mock(RestTemplate.class);
        for(var method: HttpMethod.values()){
            var util=RestTemplateUtil
                    .builder()
                    .restTemplate(restTemplate)
                    .header(HttpHeaders.AUTHORIZATION,"Beare TOKEN")
                    .headers(HttpHeaders.EMPTY)
                    .headers(Map.of(HttpHeaders.AUTHORIZATION,"Beare TOKEN"))
                    .uri("http://localhost:8080")
                    .url("http://localhost:8080")
                    .build();
            for(var statusCode: List.of(HttpStatus.OK,HttpStatus.BAD_GATEWAY)){
                ResponseEntity<String> response=new ResponseEntity("[1,2,3,4]", statusCode);

                Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(method), Mockito.any(HttpEntity.class), Mockito.eq(String.class)))
                        .thenReturn(response);

                Assertions.assertDoesNotThrow(() -> {
                    util.setMethod(method.name());
                    Assertions.assertEquals(util.getMethod(),method);
                });
                Assertions.assertNotNull(util.exchange());
                Assertions.assertNotNull(util.exchange(method));
                Assertions.assertNotNull(util.exchange(restTemplate));
                Assertions.assertNotNull(util.exchange(method, restTemplate));
                Assertions.assertNotNull(util.getEntity());
                Assertions.assertNotNull(util.getResponse());
                Assertions.assertDoesNotThrow(util::print);
            }
        }
    }


    @Test
    @DisplayName("Deve validar metodo print")
    public void deveValidarMetodoPrint() {
        var restTemplate=Mockito.mock(RestTemplate.class);
        for(var method: HttpMethod.values()){
            var util=RestTemplateUtil
                    .builder()
                    .restTemplate(restTemplate)
                    .method(method)
                    .header(HttpHeaders.AUTHORIZATION,"Beare TOKEN")
                    .headers(HttpHeaders.EMPTY)
                    .headers(Map.of(HttpHeaders.AUTHORIZATION,"Beare TOKEN"))
                    .uri("http://localhost:8080")
                    .url("http://localhost:8080")
                    .build();

            Assertions.assertDoesNotThrow(util::print);

            for(var statusCode: List.of(HttpStatus.OK,HttpStatus.BAD_GATEWAY)){
                {
                    ResponseEntity<String> response = new ResponseEntity("[1,2,3,4]", statusCode);

                    Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(method), Mockito.any(HttpEntity.class), Mockito.eq(String.class)))
                            .thenReturn(response);

                    Assertions.assertNotNull(util.exchange());
                    Assertions.assertNotNull(util.getEntity());
                    Assertions.assertNotNull(util.getResponse());
                    Assertions.assertDoesNotThrow(util::print);
                }
                {
                    Mockito.when(restTemplate.exchange(Mockito.anyString(), Mockito.eq(method), Mockito.any(HttpEntity.class), Mockito.eq(String.class)))
                            .thenThrow(new RuntimeException());

                    Assertions.assertThrows(FrameworkException.class, util::exchange);
                    Assertions.assertNotNull(util.getEntity());
                    Assertions.assertNotNull(util.getResponse());
                    Assertions.assertDoesNotThrow(util::print);
                }

            }

        }
    }
}
