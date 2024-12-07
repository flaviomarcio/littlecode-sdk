package com.littlecode.network;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.files.FileFormat;
import com.littlecode.network.clients.Http;
import com.littlecode.network.clients.RequestClient;
import com.littlecode.parsers.ObjectUtil;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.*;

@Slf4j
public class RequestUtil {
    private static final FileFormat FILE_FORMAT_DEFAULT = FileFormat.JSON;
    private final Map<String, String> headers = new HashMap<>();
    private final Response response = new Response(this);
    @Getter
    int timeout = 10;
    private RequestClient client;
    @Getter
    private Executable onStarted = null;
    @Getter
    private Executable onSuccessful = null;
    @Getter
    private Executable onFail = null;
    @Getter
    private Executable onFinished = null;
    @Getter
    @Setter
    private boolean printOnFail;
    @Getter
    @Setter
    private boolean exceptionOnFail;
    private Method method = Method.GET;
    private URI uri;
    private String path;
    @Getter
    private String body;

    public static RequestUtil builder() {
        return new RequestUtil();
    }

    public RequestUtil build() {
        return this;
    }

    public String toString() {
        return response.toString();
    }

    public RequestClient getClient() {
        if (this.client == null)
            this.client = new Http();
        return this.client;
    }

    public RequestUtil client(RequestClient client) {
        if (client == null)
            throw new NullPointerException("client is null");
        this.client = client;
        return this;
    }

    public RequestUtil onStarted(Executable executable) {
        this.onStarted = executable;
        return this;
    }

    public RequestUtil onSuccessful(Executable executable) {
        this.onSuccessful = executable;
        return this;
    }

    public RequestUtil onFail(Executable executable) {
        this.onFail = executable;
        return this;
    }

    public RequestUtil onFinished(Executable executable) {
        this.onFinished = executable;
        return this;
    }

    public RequestUtil clear() {
        this.uri = null;
        this.method = Method.GET;
        this.headers.clear();
        this.body = null;
        this.response.clear();
        this.onSuccessful = null;
        this.onFail = null;
        return this;
    }

    public List<String> printLines() {
        var method = this.method();
        List<String> lines = new ArrayList<>();
        lines.add(String.format("curl -i -X %s \\", method.name().toUpperCase()));
        this.headers().forEach((key, value) -> {
            lines.add(String.format("             --header '%s: %s' \\", key, value));
        });
        lines.add(String.format("             --location '%s' \\", this.url()));
        if (method == Method.POST || method == Method.PUT || method == Method.PATCH)
            lines.add(String.format("             --data '%s' ", this.getBody()));
        return lines;

    }

    public RequestUtil print() {
        this.printLines().forEach(log::info);
        return this;
    }

    public boolean isOK() {
        return response.isOK();
    }

    public boolean printOnFail() {
        return this.printOnFail;
    }

    public RequestUtil printOnFail(boolean newValue) {
        this.printOnFail = newValue;
        return this;
    }

    public boolean exceptionOnFail() {
        return this.exceptionOnFail;
    }

    public RequestUtil exceptionOnFail(boolean newValue) {
        this.exceptionOnFail = newValue;
        return this;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public RequestUtil headers(Map<String, String> newHeaders) {
        this.headers.clear();
        if (newHeaders != null)
            this.headers.putAll(newHeaders);
        return this;
    }

    public RequestUtil headersJSON() {
        this.headers.put("Content-Type", "application/json");
        return this;
    }

    public RequestUtil headersAuthBearer(String token) {
        this.headers.put("Authorization", String.format("Bearer %s", token));
        return this;
    }

    public RequestUtil headersAuthBasic(String token) {
        this.headers.put("Authorization", String.format("Basic %s", token));
        return this;
    }

    public RequestUtil headersAuthBasic(String clientId, String secret) {
        if ((clientId != null && !clientId.trim().isEmpty()) && (secret != null && !secret.trim().isEmpty())) {
            String credentials = String.format("%s:%s", clientId, secret);
            return this.headersAuthBasic(Base64.getEncoder().encodeToString(credentials.getBytes()));
        }
        return this;
    }

    public RequestUtil headers(String headerName, String headerValue) {
        this.headers().put(headerName, headerValue);
        return this;
    }

    public RequestUtil body(String newBody) {
        this.body = newBody;
        return this;
    }

    public RequestUtil timeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public RequestUtil body(Object newBody) {
        if (newBody != null) {
            try {
                var mapper = UtilCoreConfig.newObjectMapper(FILE_FORMAT_DEFAULT);
                this.body = mapper.writeValueAsString(newBody);
            } catch (Exception ignored) {
            }
        } else {
            this.body = null;
        }
        return this;
    }


    public RequestUtil CONNECT() {
        this.method = Method.CONNECT;
        return this;
    }

    public RequestUtil DELETE() {
        this.method = Method.DELETE;
        return this;
    }

    public RequestUtil GET() {
        this.method = Method.GET;
        return this;
    }

    public RequestUtil HEAD() {
        this.method = Method.HEAD;
        return this;
    }

    public RequestUtil LIST() {
        this.method = Method.LIST;
        return this;
    }

    public RequestUtil OPTIONS() {
        this.method = Method.OPTIONS;
        return this;
    }

    public RequestUtil PATCH() {
        this.method = Method.PATCH;
        return this;
    }

    public RequestUtil POST() {
        this.method = Method.POST;
        return this;
    }

    public RequestUtil PUT() {
        this.method = Method.PUT;
        return this;
    }

    public RequestUtil TRACE() {
        this.method = Method.TRACE;
        return this;
    }

    public Method method() {
        return method;
    }

    public RequestUtil method(Method newMethod) {
        method =
                (newMethod == null)
                        ? Method.GET
                        : newMethod;
        return this;
    }

    public String url() {
        var newUri = this.uri().toString() + this.path();
        return URI.create(newUri).toString();
    }

    public URI uri() {
        if (uri == null)
            return uri = URI.create("http://localhost");
        return uri;
    }

    public RequestUtil uri(String newUri) {
        if (newUri == null || newUri.trim().isEmpty())
            this.uri = null;
        else
            this.uri = URI.create(newUri);
        return this;
    }

    public RequestUtil uri(URI newUri) {
        this.uri = newUri;
        return this;
    }

    public String path() {
        return path;
    }

    public RequestUtil path(String newPath) {
        this.path =
                (newPath == null || newPath.trim().equals("/"))
                        ? "/"
                        : newPath.trim();
        return this;
    }

    public Response response() {
        return response;
    }

    public RequestUtil call() {
        if (this.getOnStarted() != null)
            this.getOnStarted().execute();

        this.getClient().call(this);

        if (this.response.isOK()) {
            if (this.getOnSuccessful() != null)
                this.getOnSuccessful().execute();
        } else {
            if (this.getOnFail() != null)
                this.getOnFail().execute();
            if (this.exceptionOnFail())
                throw new FrameworkException(this.response.toString());
            else if (this.printOnFail())
                this.print();
        }

        if (this.getOnFinished() != null)
            this.getOnFinished().execute();
        return this;
    }

    public enum Method {
        CONNECT,
        DELETE,
        GET,
        HEAD,
        LIST,
        OPTIONS,
        PATCH,
        POST,
        PUT,
        TRACE,
    }

    @FunctionalInterface
    public interface Executable {
        void execute();
    }

    @Data
    @RequiredArgsConstructor
    public static class Response {

        private final RequestUtil request;
        private final Map<String, List<String>> headers = new HashMap<>();
        private String url;
        private int status = -1;
        private String body;
        private String reasonPhrase;

        public void clear() {
            this.url = "";
            this.status = -1;
            this.body = null;
            this.reasonPhrase = null;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers.clear();
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    var k = entry.getKey();
                    var v = entry.getValue();
                    this.headers.put(k, List.of(v));
                }
            }
        }

        public String toString() {
            if (!this.isOK()) {
                var str = new StringBuilder();
                str
                        .append(String.format("fail: statusCode: %d, reasonPhrase: %s", this.status, this.reasonPhrase))
                        .append("\n");
                this.request
                        .printLines()
                        .forEach(s -> str.append(s).append("\n"));
                return str.toString();
            }
            return "";
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

        public <T> T bodyAs(Class<T> aClass) {
            return ObjectUtil.createFromString(aClass, body);
        }

        public <T> List<T> bodyAsList(Class<T> aClass) {
            List<T> __return = new ArrayList<>();
            if (aClass != null) {
                var __object = ObjectUtil.createFromString(Object.class, body);
                List<Object> list = new ArrayList<>();
                if (__object != null) {
                    //noinspection rawtypes
                    if (__object instanceof List value) {
                        list.addAll(value);
                    } else {
                        list.add(__object);
                    }
                    for (Object o : list) {
                        T __item = ObjectUtil.createFromObject(aClass, o);
                        if (__item != null)
                            __return.add(__item);
                    }
                }
            }
            return __return;
        }
    }
}