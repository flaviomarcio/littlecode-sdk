package com.littlecode.network;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
public class RestTemplateUtil {
    @Getter
    @Setter
    private RestTemplate restTemplate;
    private String url;
    private HttpMethod method;
    @Setter
    private MultiValueMap<String, String> headers;
    @Getter
    @Setter
    private Object body;
    @Getter
    private HttpEntity<String> entity;
    @Getter
    private ResponseEntity<String> response;

    public static RestTemplateUtilBuilder builder() {
        return new  RestTemplateUtilBuilder();
    }

    public HttpMethod getMethod() {
        return this.method=(this.method==null?HttpMethod.GET:this.method);
    }

    public void setMethod(HttpMethod method) {
        this.method = method;
    }

    public void setMethod(Object method) {
        var v=PrimitiveUtil.toString(method).trim().toUpperCase();
        this.method= Stream.of(HttpMethod.values())
                .filter(x-> v.equalsIgnoreCase(x.name()))
                .findFirst()
                .orElse(null);
    }

    public MultiValueMap<String, String> getHeaders(){
        return this.headers==null?new HttpHeaders():this.headers;
    }

    public String getUrl() {
        return this.url = PrimitiveUtil.isEmpty(this.url)
                ?"http://localhost"
                :this.url.trim();
    }

    public void setUrl(Object url) {
        this.url = PrimitiveUtil.toString(url).trim();
    }

    public ResponseEntity<String> exchange() {
        return this.exchange(this.getMethod(), this.getRestTemplate());
    }

    public ResponseEntity<String> exchange(HttpMethod method) {
        return this.exchange(method, this.getRestTemplate());
    }

    public ResponseEntity<String> exchange(RestTemplate restTemplate){
        return this.exchange(this.getMethod(), restTemplate);
    }

    public ResponseEntity<String> exchange(HttpMethod method, RestTemplate restTemplate){
        this.method=method;
        if(HttpMethod.POST.equals(this.method) || HttpMethod.PUT.equals(this.method) || HttpMethod.PATCH.equals(this.method))
            return this.exchangePOST_PUT_PATCH(this.getRestTemplate());
        return this.exchange_OPTION_GET_DELETE(restTemplate);
    }

    private ResponseEntity<String> exchangePOST_PUT_PATCH(RestTemplate restTemplate){
        HttpEntity<String> entity = new HttpEntity<>(ObjectUtil.toString(this.getBody()), headers);
        return this.exchangeInternal(restTemplate, entity);
    }

    private ResponseEntity<String> exchange_OPTION_GET_DELETE(RestTemplate restTemplate){
        HttpEntity<String> entity=new HttpEntity<>(headers);
        return this.exchangeInternal(restTemplate, entity);
    }

    private ResponseEntity<String> exchangeInternal(RestTemplate restTemplate, HttpEntity<String> entity){
        try{
            this.entity=entity;
            return this.response=restTemplate.exchange(this.getUrl(), this.getMethod(), entity, String.class);
        } catch (Exception e) {
            log.error(e.getMessage());
            this.print();
            throw new FrameworkException(e.getMessage());
        }
    }

    public void print(){

        var reqUri=PrimitiveUtil.toString(this.url);
        var httpMethod= this.getMethod().name();
        var httpHeaders=new StringBuilder();
        var reqBody= ObjectUtil.toString(this.getBody());
        this.getHeaders()
                .forEach((k, list) -> {
                    for(var v: list){
                        httpHeaders.append("--header '%s: %s' ".formatted(k,v));
                    }
                });

        if(this.getResponse()!=null && !this.getResponse().getStatusCode().is2xxSuccessful()) {
            log.error("Error on request statsCode:{}", response.getStatusCode());
        }
        if(HttpMethod.POST.equals(this.method) || HttpMethod.PUT.equals(this.method) || HttpMethod.PATCH.equals(this.method))
            log.warn("  curl -X {} {} --location '{}' --data '{}'",httpMethod, httpHeaders, reqUri, reqBody);
        else
            log.warn("  curl -X {} {} --location '{}'",httpMethod, httpHeaders, reqUri);
        if(this.getResponse()!=null) {
            log.warn("          statusCode: {}",response.getStatusCode());
            log.warn("          response: {}",response.getBody());
        }
    }

    public static class RestTemplateUtilBuilder{
        private RestTemplate restTemplate;
        private HttpMethod method;
        private final HttpHeaders headers;
        private String scheme;
        private String host;
        private int port;
        private String path;
        private final MultiValueMap<String, String> queryParams;
        private Object body;

        public RestTemplateUtilBuilder(){
            this.restTemplate=null;
            this.method=HttpMethod.GET;
            this.headers=new HttpHeaders();
            this.scheme="http";
            this.host="localhost";
            this.port=-1;
            this.path="";
            this.queryParams=new LinkedMultiValueMap<>();
            this.body=null;
        }

        public RestTemplateUtilBuilder restTemplate(RestTemplate restTemplate){
            this.restTemplate = restTemplate;
            return this;
        }

        public RestTemplateUtilBuilder headers(HttpHeaders headers){
            headers
                    .forEach(this::header);
            return this;
        }

        public RestTemplateUtilBuilder headers(Map<String, String> headers){
            headers.forEach(this::header);
            return this;
        }

        public RestTemplateUtilBuilder header(String headerName, Object headerValue){
            List<String> listV=new ArrayList<>();
            if(headerValue instanceof List<?> lst){
                lst.forEach(item->{
                    listV.add(PrimitiveUtil.toString(item));
                });
            }
            else{
                listV.add(PrimitiveUtil.toString(headerValue));
            }

            var list=new ArrayList<>(this.headers.getValuesAsList(headerName));
            listV
                    .forEach(v -> {
                        if (!list.contains(v))
                            list.add(v);
                    });
            this.headers.remove(headerName);
            this.headers.put(headerName, list);
            return this;
        }

        public RestTemplateUtilBuilder scheme(Object scheme) {
            this.scheme = PrimitiveUtil.toString(scheme).trim().toLowerCase();
            return this;
        }

        public RestTemplateUtilBuilder method(HttpMethod method) {
            this.method = method==null?HttpMethod.GET:method;
            return this;
        }

        public RestTemplateUtilBuilder method(Object method) {
            var v=PrimitiveUtil.toString(method).trim().toUpperCase();
            this.method = HttpMethod.valueOf(v.isEmpty()?HttpMethod.GET.name():v);
            return this;
        }

        public RestTemplateUtilBuilder host(String host) {
            this.host = PrimitiveUtil.toString(host).trim();
            return this;
        }

        public RestTemplateUtilBuilder url(Object url){
            return this.uri(url);
        }

        public RestTemplateUtilBuilder uri(Object uri) {
            var v=PrimitiveUtil.toString(uri);
            UriComponents components = UriComponentsBuilder.fromUriString(v).build();
            this.scheme = components.getScheme();
            this.host=components.getHost();
            this.path=components.getPath();
            this.port=components.getPort();
            this.queryParams.clear();
            this.queryParams.putAll(components.getQueryParams());
            return this;
        }

        public RestTemplateUtilBuilder port(int port) {
            this.port = port;
            return this;
        }

        public RestTemplateUtilBuilder path(String path){
            this.path=PrimitiveUtil.toString(path).trim();
            return this;
        }

        public RestTemplateUtilBuilder queryParams(MultiValueMap<String, String> queryParams) {
            this.queryParams.clear();
            queryParams.forEach(this::queryParam);
            return this;
        }

        public RestTemplateUtilBuilder queryParams(Map<String, String> queryParams) {
            this.queryParams.clear();
            queryParams.forEach(this::queryParam);
            return this;
        }

        public RestTemplateUtilBuilder queryParam(String paramName, Object paramValue) {
            if(!PrimitiveUtil.isEmpty(paramName)){
                List<String> lst=this.queryParams.getOrDefault(paramName, new ArrayList<>());
                var v=PrimitiveUtil.toString(paramValue);
                if(!v.isEmpty())
                    lst.add(v);
                this.queryParams.put(paramName, lst);
            }
            return this;
        }

        public RestTemplateUtilBuilder body(Object body) {
            this.body = body;
            return this;
        }

        public RestTemplateUtil build(){
            var restTemplateUtil= new RestTemplateUtil();
            restTemplateUtil.setRestTemplate(restTemplate);
            restTemplateUtil.setMethod(this.method);
            if(PrimitiveUtil.isEmpty(this.scheme))
                this.scheme="http";
            if(PrimitiveUtil.isEmpty(this.host))
                this.host="localhost";
            if(PrimitiveUtil.isEmpty(this.path))
                this.path="";
            if(this.path.equals("/"))
                this.path="";
            var url=UriComponentsBuilder
                    .fromHttpUrl("%s://%s".formatted(this.scheme,this.host))
                    .port(this.port)
                    .path(this.path)
                    .queryParams(this.queryParams)
                    .toUriString();
            restTemplateUtil.setUrl(url);
            restTemplateUtil.setHeaders(this.headers);
            restTemplateUtil.setBody(this.body);
            return restTemplateUtil;
        }
    }
}
