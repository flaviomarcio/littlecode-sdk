package com.littlecode.tests;

import com.littlecode.health.*;
import com.littlecode.util.TestsUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
public class HealthCheckTest {

    @BeforeEach
    void setUp() {
        HealthCheck.reset();
    }

    @Test
    @DisplayName("deve validar constructors")
    void deveValidarConstructors() {
        HealthCheck.reset();
        var config=new HealthCheckConfig();
        Assertions.assertDoesNotThrow(() -> HealthCheck.createHealthIndicator(config));
        Assertions.assertDoesNotThrow(() -> HealthCheck.createHealthIndicator(config, new HealthCheckEvent(config)));
    }

    @Test
    @DisplayName("deve validar GetterSetter")
    void deveValidarGetterSetters() {
        TestsUtil.checkObject(new HealthCheckConfig(), HealthCheckConfig.builder().build());
    }

    @Test
    @DisplayName("deve executar Indicator com limite de execucao de uma aplicacao")
    void deveExecutarIndicatorComLimiteDeExecucaoDeAplicacao() {
        HealthCheck.reset();

        final var config=new HealthCheckConfig();
        var healthCheck = HealthCheck.createHealthIndicator(config);
        Assertions.assertNull(healthCheck.getEvent().createHealth());

        {
            config.setStopOnLimitExecution(true);
            config.setStopOnLimitDuration("3m");
            HealthCheck.setStaticStartExecution(null);
            Assertions.assertNull(healthCheck.getEvent().createHealth());
        }

        {
            config.setStopOnLimitExecution(true);
            config.setStopOnLimitDuration("3m");
            HealthCheck.setStaticStartExecution(LocalDateTime.now().minusDays(1));
            Assertions.assertNotNull(healthCheck.getEvent().createHealth());
            HealthCheck.setStaticStartExecution(LocalDateTime.now().plusDays(1));
            Assertions.assertNull(healthCheck.getEvent().createHealth());
        }

        {
            config.setStopOnLimitExecution(true);
            config.setStopOnLimitDuration("");
            HealthCheck.setStaticStartExecution(LocalDateTime.now().minusDays(1));
            Assertions.assertNull(healthCheck.getEvent().createHealth());
        }
    }

    @Test
    @DisplayName("deve executar Indicator com limite de osiosidade de uma aplicacao")
    void deveExecutarIndicatorComLimiteDeOciosidadeDeAplicacao() {
        HealthCheck.reset();
        final var config=new HealthCheckConfig();
        var healthCheck = HealthCheck.createHealthIndicator(config);
        Assertions.assertNull(healthCheck.getEvent().createHealth());
        config.setStopOnIdle(true);
        config.setStopOnIdleDuration("3m");
        {
            HealthCheck.setStaticLastExecution(null);
            Assertions.assertNull(healthCheck.getEvent().createHealth());
        }
        {
            HealthCheck.executionNotify();
            HealthCheck.setStaticLastExecution(LocalDateTime.now().minusDays(1));
            Assertions.assertNotNull(healthCheck.getEvent().createHealth());
            HealthCheck.setStaticLastExecution(LocalDateTime.now().plusDays(1));
            Assertions.assertNull(healthCheck.getEvent().createHealth());
        }

        {
            config.setStopOnIdleDuration("");
            Assertions.assertNull(healthCheck.getEvent().createHealth());
        }
    }

    @Test
    @DisplayName("deve executar Indicator com falha existente")
    void deveExecutarIndicatorComFalhaExistente() {
        HealthCheck.reset();
        final var config=new HealthCheckConfig();
        var healthCheck = HealthCheck.createHealthIndicator(config);
        {
            Assertions.assertNull(healthCheck.getEvent().createHealth());
        }
        {
            config.setStopOnFail(true);
            Assertions.assertNull(healthCheck.getEvent().createHealth());
        }
        {
            config.setStopOnFail(true);
            HealthCheck.failNotify("falha");
            Assertions.assertNotNull(healthCheck.getEvent().createHealth());
        }
    }

    @Test
    @DisplayName("deve executar Indicator e validar healt")
    void deveExecutarIndicatorEValidarHealt() {
        HealthCheck.reset();
        final var config=new HealthCheckConfig();
        var healthCheck = HealthCheck.createHealthIndicator(config);
        {
            config.setStopOnFail(true);
            Assertions.assertNotNull(healthCheck.health());
        }
        {
            config.setStopOnFail(true);
            HealthCheck.failNotify("falha");
            Assertions.assertNotNull(healthCheck.health());
        }
    }
//
//    @Test
//    @DisplayName("deve executar Indicator com limite de execucao de uma aplicacao")
//    void deveExecutarIndicator() {
//            final var config=new HealthCheckConfig();
//            var healthCheck = HealthCheck.createHealthIndicator(config);
//            Assertions.assertNotNull(healthCheck.health());
//            Assertions.assertNull(healthCheck.getEvent().createHealth());
//            HealthCheck.failNotify("message fail");
//            config.setStopOnFail(true);
//            Assertions.assertNotNull(healthCheck.getEvent().createHealth());
//    }
}
