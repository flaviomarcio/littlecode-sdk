package com.littlecode.mq.tests;

import com.littlecode.containers.ObjectContainer;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.mq.MQ;
import com.littlecode.mq.config.MQAutoConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class MQTest {

    @Test
    public void UT_CHECK_STARTED() {
        var context = Mockito.mock(ApplicationContext.class);
        var environment = Mockito.mock(Environment.class);
        var mqStarted = new MQAutoConfiguration(context, environment);
        Assertions.assertNotNull(mqStarted);
    }

    @Test
    public void UT_CHECK_TASK() {
        enum EnumCheck {
            EnumA, EnumB
        }

        var objectCheck = new ObjectCheck();
        var bodyJson = String.format("{\"id\":\"%s\"}", objectCheck.getId());


        {
            var task = MQ.Message.Task.of(EnumCheck.EnumA, objectCheck);
            Assertions.assertTrue(MQ.Message.Task.of(bodyJson).getType().toString().isEmpty());
            Assertions.assertEquals(MQ.Message.Task.of(objectCheck).getType(), objectCheck.getClass().getName());

            Assertions.assertEquals(task.asString(), bodyJson);
            Assertions.assertNotNull(task.asContainer());
            Assertions.assertTrue(MQ.Message.Task.of(objectCheck).canType(ObjectCheck.class));
            Assertions.assertTrue(task.canType(EnumCheck.EnumA));
            Assertions.assertFalse(task.canType(EnumCheck.EnumB));
            Assertions.assertFalse(task.canType(null));
        }


        {
            var task = MQ.Message.Task.of(objectCheck);
            Assertions.assertNotNull(task.asObject(ObjectCheck.class));
            Assertions.assertNotNull(task.asObject(ObjectCheck.class.getName()));
            Assertions.assertThrows(FrameworkException.class, () -> task.asObject(ObjectCheck.class.getSimpleName()));
            Assertions.assertThrows(FrameworkException.class, () -> task.asObject(ObjectCheck.class.toString()));
            ObjectContainer.classDictionaryRegister(ObjectCheck.class);
            Assertions.assertNotNull(task.getType());
            Assertions.assertNotNull(task.getTypeName());
            Assertions.assertNotNull(task.getTypeClass());
            Assertions.assertDoesNotThrow(() -> task.asObject(ObjectCheck.class));
            Assertions.assertDoesNotThrow(() -> task.asObject(ObjectCheck.class.getName()));
            Assertions.assertDoesNotThrow(() -> task.asObject(ObjectCheck.class.getSimpleName()));
            Assertions.assertDoesNotThrow(() -> task.asObject(ObjectCheck.class.toString()));
            Assertions.assertEquals(task.asObject(ObjectCheck.class).getId(), objectCheck.getId());
        }

    }

    @Getter
    @Setter
    public static class ObjectCheck {
        private final UUID id;

        public ObjectCheck() {
            this.id = UUID.randomUUID();
        }
    }

}
