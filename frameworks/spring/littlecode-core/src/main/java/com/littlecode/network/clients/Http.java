package com.littlecode.network.clients;

import com.littlecode.network.RequestUtil;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@NoArgsConstructor
public class Http implements RequestClient {

    @Override
    public void call(RequestUtil rqUtil) {
        var requestBody = (rqUtil.getBody() == null || rqUtil.getBody().trim().isEmpty())
                ? HttpRequest.BodyPublishers.noBody()
                : HttpRequest.BodyPublishers.ofString(rqUtil.getBody());

        final var rqResponse = rqUtil.response();
        final var timeout = Duration.ofSeconds(rqUtil.getTimeout());
        try {

            HttpRequest.Builder requestBuilder;

            switch (rqUtil.method()) {
                case POST:
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(rqUtil.url()))
                            .timeout(timeout)
                            .POST(requestBody);
                    ; // Configura um tempo limite de leitura de 10 segundos
                    break;
                case PUT:
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(rqUtil.url()))
                            .timeout(timeout)
                            .PUT(requestBody);
                    ; // Configura um tempo limite de leitura de 10 segundos
                    break;
                case DELETE:
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(rqUtil.url()))
                            .timeout(timeout)
                            .DELETE();
                    ; // Configura um tempo limite de leitura de 10 segundos
                    break;
                default:
                    requestBuilder = HttpRequest.newBuilder()
                            .uri(URI.create(rqUtil.url()))
                            .timeout(timeout)
                            .method(rqUtil.method().name(), requestBody);
                    ; // Configura um tempo limite de leitura de 10 segundos
                    break;
            }

            HttpClient httpClient = this.createHttpClient(rqUtil);
            rqUtil.headers().forEach(requestBuilder::header);
            HttpRequest request = requestBuilder.build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            rqResponse.setBody(response.body());
            rqResponse.setStatus(response.statusCode());
            rqResponse.getHeaders().clear();
            rqResponse.getHeaders().putAll(response.headers().map());
            rqResponse.setUrl(response.uri().toURL().toString());

        } catch (Exception e) {
            rqResponse.setStatus(-1);
            rqResponse.setReasonPhrase(e.getMessage());
        }
    }

    @Override
    public HttpClient createHttpClient(RequestUtil rqUtil) {
        return HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(rqUtil.getTimeout()))
                .build();
    }
}
