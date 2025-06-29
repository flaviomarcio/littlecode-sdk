package com.littlecode.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.util.BeanUtilFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
class BeanUtilFactoryTest {

    @BeforeEach
    void init() {
        UtilCoreConfig.setEnvironment(Mockito.mock(Environment.class));
        UtilCoreConfig.setApplicationContext(Mockito.mock(ApplicationContext.class));
    }

    @Test
    @DisplayName("deve validar metodo createModelMapper")
    void deveValidarMetodoCreateModelMapper() {
        Assertions.assertNotNull(BeanUtilFactory.createModelMapper());
    }

    @Test
    @DisplayName("deve validar metodo createObjectMapper")
    void deveValidarMetodoCreateObjectMapper() {
        Assertions.assertNotNull(BeanUtilFactory.createObjectMapper());
    }

    @Test
    @DisplayName("deve validar metodo createRestTemplate")
    void deveValidarMetodoCreateRestTemplate() {
        Assertions.assertNull(BeanUtilFactory.createRestTemplate("????"));
        Assertions.assertNotNull(BeanUtilFactory.createRestTemplate());
        Assertions.assertDoesNotThrow(() -> {
            List.of(BeanUtilFactory.getTrustManagers())
                    .forEach(e -> {
                        X509TrustManager x509 = (X509TrustManager) e;
                        Assertions.assertNotNull(x509);
                        Assertions.assertDoesNotThrow(() -> x509.checkClientTrusted(null, null));
                        Assertions.assertDoesNotThrow(() -> x509.checkServerTrusted(null, null));
                        Assertions.assertDoesNotThrow(x509::getAcceptedIssuers);
                    });
        });
    }

    @Test
    @DisplayName("deve validar classe InternalResponseErrorHandler")
    void deveValidarInternalResponseErrorHandler() throws IOException {
        var handler = new BeanUtilFactory.InternalResponseErrorHandler();
        handler.hasError(null);
        handler.handleError(null);

    }
}
