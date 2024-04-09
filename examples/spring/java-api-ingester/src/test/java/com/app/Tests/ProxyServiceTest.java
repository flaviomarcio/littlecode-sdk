package com.app.Tests;

import com.app.business.dto.TaskOut;
import com.app.business.service.ProxyService;
import com.app.factory.FactoryByTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class ProxyServiceTest {

    private final FactoryByTests FACTORY = new FactoryByTests();
    private final ProxyService proxyService = FACTORY.getProxyService();

    @Test
    public void UT_000_CHECK_SAVE() {
        for (int i = 0; i < 1000; i++) {
            var objectReturn = proxyService.proxy(Map.of("dt", LocalDateTime.now()));
            Assertions.assertTrue(objectReturn.isOK());
            var taskOut = objectReturn.cast(TaskOut.class);
            Assertions.assertNotNull(taskOut);
            Assertions.assertNotNull(taskOut.getId());
            Assertions.assertNotNull(taskOut.getDt());
            Assertions.assertNotNull(taskOut.getChecksum());
            Assertions.assertEquals(taskOut.getState(), FACTORY.getAppConfig().getAppProxyStateSent());
        }
    }


}
