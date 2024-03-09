package com.littlecode.network;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.FileFormat;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

@Slf4j
public class RequestUtil {
    private static final FileFormat FILE_FORMAT_DEFAULT = FileFormat.JSON;
    private Executable privateOnStarted = null;
    private Executable privateOnSuccessful = null;
    private Executable privateOnFail = null;
    private Executable privateOnFinished = null;
    private Method privateMethod;
    private URI privateUri;
    private String privatePath;
    private Map<String, String> privateHeaders;
    private Response response;
    private String body;
    private boolean privatePrintOnFail;
    private boolean privateExceptionOnFail;

    public static RequestUtil builder() {
        return new RequestUtil();
    }

    public String toString() {
        if (this.response == null)
            return super.toString();
        if (this.response.isOK())
            return super.toString();

        return response.toString();
    }

    public RequestUtil build() {
        return this;
    }

    public RequestUtil onStarted(Executable executable) {
        this.privateOnStarted = executable;
        return this;
    }

    public RequestUtil onSuccessful(Executable executable) {
        this.privateOnSuccessful = executable;
        return this;
    }

    public RequestUtil onFail(Executable executable) {
        this.privateOnFail = executable;
        return this;
    }

    public RequestUtil onFinished(Executable executable) {
        this.privateOnFinished = executable;
        return this;
    }

    public RequestUtil clear() {
        this.privateUri = null;
        this.privateMethod = Method.GET;
        this.privateHeaders = new HashMap<>();
        this.body = null;
        this.response = null;
        this.privateOnSuccessful = null;
        this.privateOnFail = null;
        return this;
    }

    private List<String> printMake() {
        List<String> str = new ArrayList<>();
        str.add(String.format("curl -i -X %s \\", this.method().name()));
        this.headers().forEach((key, value) -> {
            str.add(String.format("             --header '%s: %s' \\", key, value));
        });
        str.add(String.format("             --location '%s' \\", this.url()));
        if (this.method() == Method.POST || this.method() == Method.PUT)
            str.add(String.format("             --data '%s' ", this.body()));
        return str;
    }

    public RequestUtil print() {
        this.printMake().forEach(log::info);
        return this;
    }

    public boolean isOK() {
        return response != null && response.isOK();
    }


    public boolean printOnFail() {
        return this.privatePrintOnFail;
    }

    public RequestUtil printOnFail(boolean newValue) {
        this.privatePrintOnFail = newValue;
        return this;
    }

    public boolean exceptionOnFail() {
        return this.privateExceptionOnFail;
    }

    public RequestUtil exceptionOnFail(boolean newValue) {
        this.privateExceptionOnFail = newValue;
        return this;
    }

    public Map<String, String> headers() {
        if (this.privateHeaders == null)
            this.privateHeaders = new HashMap<>();
        return privateHeaders;
    }

    public RequestUtil headers(Map<String, String> newHeaders) {
        this.privateHeaders = newHeaders;
        return this;
    }

    public RequestUtil headersJSON() {
        this.headers().put("Content-Type", "application/json");
        return this;
    }

    public RequestUtil headersAuthBearer(String token) {
        this.headers().put("Authorization", String.format("Bearer %s", token));
        return this;
    }

    public RequestUtil headersAuthBasic(String token) {
        this.headers().put("Authorization", String.format("Basic %s", token));
        return this;
    }

    public RequestUtil headersAuthBasic(String clientId, String secret) {
        String credentials = String.format("%s:%s", clientId, secret);
        return this.headersAuthBasic(Base64.getEncoder().encodeToString(credentials.getBytes()));
    }

    public RequestUtil headers(String headerName, String headerValue) {
        this.headers().put(headerName, headerValue);
        return this;
    }

    public String body() {
        return body;
    }

    public String responseBody() {
        if (response == null)
            return null;
        return this.response.body();
    }

    public RequestUtil body(String newBody) {
        this.body = newBody;
        return this;
    }

    public RequestUtil body(Object newBody) {
        if (newBody == null) {
            this.body = null;
            return this;
        }
        var mapper = UtilCoreConfig.newObjectMapper(FILE_FORMAT_DEFAULT);
        try {
            this.body = mapper.writeValueAsString(newBody);
        } catch (JsonProcessingException e) {
            this.body = null;
        }
        return this;
    }

    public RequestUtil GET() {
        this.privateMethod = Method.GET;
        return this;
    }

    public RequestUtil POST() {
        this.privateMethod = Method.POST;
        return this;
    }

    public RequestUtil PUT() {
        this.privateMethod = Method.PUT;
        return this;
    }

    public RequestUtil DELETE() {
        this.privateMethod = Method.DELETE;
        return this;
    }

    public Method method() {
        if (this.privateMethod == null)
            this.privateMethod = Method.GET;
        return privateMethod;
    }

    public RequestUtil method(Method newMethod) {
        this.privateMethod = newMethod == null ? Method.GET : newMethod;
        return this;
    }

    public RequestUtil method(String newMethod) {
        this.privateMethod = Method.valueOf(newMethod.toLowerCase());
        return this;
    }

    public String url() {
        var newUri = this.uri().toString() + this.path();
        return URI.create(newUri).toString();
    }

    public URI uri() {
        return privateUri;
    }

    public RequestUtil uri(String newUri) {
        this.privateUri = URI.create(newUri);
        return this;
    }

    public RequestUtil uri(URI newUri) {
        this.privateUri = newUri;
        return this;
    }

    public String path() {
        return privatePath;
    }

    public RequestUtil path(String newPath) {
        this.privatePath = (newPath == null || newPath.trim().equals("/"))
                ? null
                : newPath.trim();
        return this;
    }

    public Response response() {
        return response;
    }

    public RequestUtil call() {
        response = null;
        var requestBody = (this.body() == null || this.body.trim().isEmpty())
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(this.body());

        try {

            if (privateOnStarted != null) {
                try {
                    privateOnStarted.execute();
                } catch (Throwable ex) {
                    throw new FrameworkException(ex);
                }
            }

            HttpClient httpClient = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(5)) // Configura um tempo limite de conexão de 5 segundos
                    .build();

            HttpRequest.Builder requestBuilder;

            switch (this.method()) {
                case POST:
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(this.url()))
                            .timeout(Duration.ofSeconds(10))
                            .POST(requestBody);
                    ; // Configura um tempo limite de leitura de 10 segundos
                    break;
                case PUT:
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(this.url()))
                            .timeout(Duration.ofSeconds(10))
                            .PUT(requestBody);
                    ; // Configura um tempo limite de leitura de 10 segundos
                    break;
                case DELETE:
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(this.url()))
                            .timeout(Duration.ofSeconds(10))
                            .DELETE();
                    ; // Configura um tempo limite de leitura de 10 segundos
                    break;
                default:
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(this.url()))
                            .timeout(Duration.ofSeconds(10))
                            .GET();
                    ; // Configura um tempo limite de leitura de 10 segundos
                    break;
            }

            this.headers().forEach(requestBuilder::header);

            HttpRequest request = requestBuilder.build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            this.response = new Response.ResponseBuilder()
                    .request(this)
                    .body(response.body())
                    .status(response.statusCode())
                    .headers(response.headers().map())
                    .url(response.uri().toURL().toString())
                    .build();
            if (privateOnSuccessful != null) {
                try {
                    privateOnSuccessful.execute();
                } catch (Throwable ex) {
                    throw new FrameworkException(ex);
                }
            }
        } catch (Throwable e) {
            response = new Response.ResponseBuilder()
                    .status(-1)
                    .reasonPhrase(e.getMessage())
                    .build();
            if (privateOnFail != null) {
                try {
                    privateOnFail.execute();
                } catch (Throwable ex) {
                    throw new FrameworkException(ex);
                }
            }
            if (this.exceptionOnFail())
                throw new RequestException(response.toString());
            else if (this.printOnFail())
                this.print();
        }
        if (privateOnFinished != null) {
            try {
                privateOnFinished.execute();
            } catch (Throwable ex) {
                throw new FrameworkException(ex);
            }
        }
        return this;
    }


    public enum Method {
        GET,
        POST,
        PUT,
        DELETE
    }

    @FunctionalInterface
    public interface Executable {
        void execute() throws Throwable;
    }

    @Builder
    public static class Response {

        private String url;

        private Map<String, List<String>> headers;

        private int status;

        private String body;

        private String reasonPhrase;

        private RequestUtil request;

        public String toString() {
            if (this.isOK())
                return "";
            var str = new StringBuilder();
            str
                    .append(String.format("fail: statusCode: %d, reasonPhrase: %s", this.status, this.reasonPhrase))
                    .append("\n");
            this.request
                    .printMake()
                    .forEach(s -> str.append(s).append("\n"));
            return str.toString();
        }

        private <T> T jsonToObject(String json, Class<T> classOfT) {
            var objectMapper = UtilCoreConfig.newObjectMapper(FILE_FORMAT_DEFAULT);
            try {
                return objectMapper.readValue(json, classOfT);
            } catch (JsonProcessingException e) {
                if (this.request.printOnFail())
                    log.error(e.getMessage());
                return null;
            }
        }

        public boolean isOK() {
            return status == 200 || status == 201 || status == 202;
        }

        public RequestUtil request() {
            return request;
        }

        public int status() {
            return status;
        }

        public String reasonPhrase() {
            return reasonPhrase;
        }

        public String body() {
            return body;
        }

        public <T> T bodyAs(Class<T> objectClass) {
            var object = jsonToObject(body, objectClass);
            if (object == null && this.request.exceptionOnFail())
                throw new RequestException(objectClass);
            return object;
        }
    }

    public static class RequestException extends RuntimeException {
        public RequestException(String msg, Throwable t) {
            super(msg, t);
        }

        public RequestException(String msg) {
            super(msg);
        }

        public RequestException(String format, Object... args) {
            super(String.format(format, args));
        }

        public RequestException(Class<?> classType) {
            super(String.format("Request error: [%s]", classType.getName()));
        }
    }

}
