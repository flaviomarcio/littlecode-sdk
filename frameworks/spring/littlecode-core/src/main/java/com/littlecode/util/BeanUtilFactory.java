package com.littlecode.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.littlecode.config.UtilCoreConfig;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

@Slf4j
public class BeanUtilFactory {

    @Bean
    public static ObjectMapper createObjectMapper() {
        return UtilCoreConfig.newObjectMapper();
    }

    @Bean
    public static ModelMapper createModelMapper() {
        return UtilCoreConfig.newModelMapper();
    }

    public static RestTemplate createRestTemplate(){
        return createRestTemplate("TLS");
    }

    public static RestTemplate createRestTemplate(String sslInstance){
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = getTrustManagers();

        // Install the all-trusting trust manager
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance(sslInstance);
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (Exception e) {
            log.error("{}: {}", BeanUtilFactory.class.getSimpleName(), e.getMessage());
            return null;
        }
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // Create an all-trusting host name verifier
        HostnameVerifier allHostsValid = (hostname, session) -> true;

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        ClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        var restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(new InternalResponseErrorHandler());
        return restTemplate;
    }

    public static class InternalResponseErrorHandler implements ResponseErrorHandler {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            // Sempre retorna false para indicar que nenhuma resposta é um "erro"
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            // Não faz nada, evitando que exceções sejam lançadas
        }
    }

    public static TrustManager[] getTrustManagers() {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };
        return trustAllCerts;
    }
}