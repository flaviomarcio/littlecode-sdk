package com.littlecode.mq.extensions.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.InvalidException;
import com.littlecode.mq.MQ;
import com.littlecode.mq.extensions.CallbackMessage;
import com.littlecode.mq.extensions.DlqMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class MQExtensionsTest {

    @BeforeAll
    public static void setUp() {
        UtilCoreConfig.setApplicationContext(Mockito.mock(ApplicationContext.class));
        UtilCoreConfig.setEnvironment(Mockito.mock(Environment.class));
    }

    @Test
    @DisplayName("deve validar DlqMQExtension")
    void testDlqMQExtension() {
        Assertions.assertDoesNotThrow(() -> (new DlqMessage.Payload(Map.of("key","value"))).getData());
        Assertions.assertDoesNotThrow(() -> new DlqMessage(Mockito.mock(MQ.class)));
        Assertions.assertDoesNotThrow(() -> new DlqMessage("",Mockito.mock(MQ.class)));
        var extension=new DlqMessage(Mockito.mock(MQ.class));
        Assertions.assertDoesNotThrow(extension::setting);
        Assertions.assertThrows(NullPointerException.class, () -> extension.queue(null));
        Assertions.assertThrows(InvalidException.class, () -> extension.queue(""));
        Assertions.assertDoesNotThrow(() -> extension.queue("test"));
        Assertions.assertThrows(NullPointerException.class, () -> extension.send((DlqMessage.Payload) null));
        Assertions.assertThrows(NullPointerException.class, () -> extension.send((Object) null));
        Assertions.assertDoesNotThrow(() -> extension.send((Object)Map.of("key","value")));
        Assertions.assertDoesNotThrow(() -> extension.send(new DlqMessage.Payload(Map.of("key","value"))));
    }

    @Test
    @DisplayName("deve validar CallbackMQExtension")
    void testCallbackMQExtension() {
        Assertions.assertDoesNotThrow(() -> (new CallbackMessage.Payload(Map.of("key","value"))).getData());
        Assertions.assertDoesNotThrow(() -> new CallbackMessage(Mockito.mock(MQ.class)));
        Assertions.assertDoesNotThrow(() -> new CallbackMessage("",Mockito.mock(MQ.class)));
        var extension=new CallbackMessage(Mockito.mock(MQ.class));
        Assertions.assertDoesNotThrow(extension::setting);
        Assertions.assertThrows(NullPointerException.class, () -> extension.queue(null));
        Assertions.assertThrows(NullPointerException.class, () -> extension.send((CallbackMessage.Payload) null));
        Assertions.assertThrows(NullPointerException.class, () -> extension.send((Object) null));
        Assertions.assertDoesNotThrow(() -> extension.send((Object)Map.of("key","value")));
        Assertions.assertDoesNotThrow(() -> extension.send(new CallbackMessage.Payload(Map.of("key","value"))));
    }
}
