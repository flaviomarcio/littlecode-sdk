package com.littlecode.tests;

import com.littlecode.containers.ObjectContainer;
import com.littlecode.exceptions.FrameworkException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ObjectContainerTest {
    @Test
    public void UT_CHECK() {
        enum EnumCheck {
            EnumA, EnumB
        }
        ;

        var objectCheck = new ObjectCheck();
        var bodyJson = String.format("{\"id\":\"%s\"}", objectCheck.getId());

        {
            var container = ObjectContainer.of(EnumCheck.EnumA, objectCheck);
            Assertions.assertTrue(ObjectContainer.of(bodyJson).getType().toString().isEmpty());
            Assertions.assertEquals(ObjectContainer.of(objectCheck).getType().toString(), objectCheck.getClass().getName());

            Assertions.assertEquals(container.asString(), bodyJson);
            Assertions.assertNotNull(container.getChecksum());
            Assertions.assertTrue(ObjectContainer.of(objectCheck).canType(ObjectCheck.class));
            Assertions.assertTrue(container.canType(EnumCheck.EnumA));
            Assertions.assertFalse(container.canType(EnumCheck.EnumB));
            Assertions.assertFalse(container.canType(null));
            Assertions.assertNotNull(container.getType());
            Assertions.assertNotNull(container.getTypeName());
        }

        {
            var container = ObjectContainer.of(objectCheck);
            Assertions.assertNotNull(container.asObject(ObjectCheck.class));
            Assertions.assertNotNull(container.asObject(ObjectCheck.class.getName()));
            Assertions.assertThrows(FrameworkException.class, () -> container.asObject(ObjectCheck.class.getSimpleName()));
            Assertions.assertThrows(FrameworkException.class,() -> container.asObject(ObjectCheck.class.toString()));
            Assertions.assertDoesNotThrow(ObjectContainer::classDictionaryClear);
            Assertions.assertDoesNotThrow(() -> ObjectContainer.classDictionaryRegister(ObjectCheck.class));
            Assertions.assertNotNull(container.getType());
            Assertions.assertNotNull(container.getTypeName());
            Assertions.assertNotNull(container.getTypeClass());
            Assertions.assertDoesNotThrow(() -> container.asObject(ObjectCheck.class));
            Assertions.assertDoesNotThrow(() -> container.asObject(ObjectCheck.class.getName()));
            Assertions.assertDoesNotThrow(() -> container.asObject(ObjectCheck.class.getSimpleName()));
            Assertions.assertDoesNotThrow(() -> container.asObject(ObjectCheck.class.toString()));
            Assertions.assertEquals(container.asObject(ObjectCheck.class).getId(), objectCheck.getId());
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
