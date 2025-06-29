package com.littlecode.tests;

import com.littlecode.containers.ObjectContainer;
import com.littlecode.exceptions.FrameworkException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
class ObjectContainerTest {

    @Test
    @DisplayName("Deve validar class Constructor")
    void UT_CHECK_CONSTRUCTOR() {
        Assertions.assertDoesNotThrow(() -> ObjectContainer.builder().build());
        Assertions.assertDoesNotThrow(() -> new ObjectContainer());

        var objectContainer = new ObjectContainer();
        Assertions.assertDoesNotThrow(objectContainer::getId);
        Assertions.assertDoesNotThrow(objectContainer::getTypeName);
        Assertions.assertDoesNotThrow(objectContainer::getType);
        Assertions.assertDoesNotThrow(objectContainer::getChecksum);
        Assertions.assertDoesNotThrow(objectContainer::getTypeName);
        Assertions.assertDoesNotThrow(objectContainer::getBody);
        Assertions.assertDoesNotThrow(objectContainer::getTypeClass);


        Assertions.assertDoesNotThrow(() -> objectContainer.setId(null));
        Assertions.assertDoesNotThrow(() -> objectContainer.setType(null));
        Assertions.assertDoesNotThrow(() -> objectContainer.setBody(null));
    }

    @Test
    @DisplayName("Deve validar metodos As")
    void UT_CHECK_AS() {
        var objectCheck = new ObjectCheck();
        var objectContainer = ObjectContainer.of(objectCheck);
        Assertions.assertDoesNotThrow(objectContainer::asString);
        Assertions.assertDoesNotThrow(() -> objectContainer.asObject(ObjectCheck.class));

        {
            var container = ObjectContainer.of(objectCheck);
            Assertions.assertDoesNotThrow(() -> container.asObject(ObjectCheck.class));
            Assertions.assertDoesNotThrow(() -> container.asObject((Class) null));
            Assertions.assertDoesNotThrow(() -> container.asObject((String) null));
            Assertions.assertThrows(FrameworkException.class, () -> container.asObject("test"));
            Assertions.assertNotNull(container.asObject(ObjectCheck.class));
            Assertions.assertNotNull(container.asObject(ObjectCheck.class.getName()));
            Assertions.assertThrows(FrameworkException.class, () -> container.asObject(ObjectCheck.class.getSimpleName()));
            Assertions.assertThrows(FrameworkException.class, () -> container.asObject(ObjectCheck.class.toString()));
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

    @Test
    @DisplayName("Deve validar metodo class Dictionary By Name")
    void UT_CHECK_metodo_class_Dictionary_By_Name() {
        enum EnumCheck {
            EnumA, EnumB
        }
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classDictionaryByName(null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classDictionaryByName(EnumCheck.class));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classDictionaryByName(EnumCheck.class.getName()));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classDictionaryByName(new ObjectCheck()));
        Assertions.assertNull(ObjectContainer.classDictionaryByName(null));
    }

    @Test
    @DisplayName("Deve validar metodo class to list names")
    void UT_CHECK_CLASS_TO_LIST_NAMES() {
        enum EnumCheck {
            EnumA, EnumB
        }
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToListNames(null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToListNames(EnumCheck.class));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToListNames(EnumCheck.class.getName()));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToListNames(new ObjectCheck()));
        Assertions.assertNotNull(ObjectContainer.classToListNames(null));
        Assertions.assertNotNull(ObjectContainer.classToListNames(EnumCheck.class));
        Assertions.assertNotNull(ObjectContainer.classToListNames(EnumCheck.class));
        Assertions.assertNotNull(ObjectContainer.classToListNames(EnumCheck.class.getName()));
        Assertions.assertNotNull(ObjectContainer.classToListNames(EnumCheck.class.getSimpleName()));
        Assertions.assertNotNull(ObjectContainer.classToListNames(new ObjectCheck()));
    }


    @Test
    @DisplayName("Deve validar metodo class by name")
    void UT_CHECK_CLASS_BY_NAME() {
        enum EnumCheck {
            EnumA, EnumB
        }
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classBy(new ObjectCheck()));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classBy(null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classBy("test"));
    }

    @Test
    @DisplayName("Deve validar metodo class to name")
    void UT_CHECK_CLASS_TO_NAME() {
        enum EnumCheck {
            EnumA, EnumB
        }
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToName(EnumCheck.EnumA));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToName(EnumCheck.class));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToName(null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToName(new Object()));
    }

    @Test
    @DisplayName("Deve validar metodo class to name")
    void UT_CHECK_CLASS_BY() {
        enum EnumCheck {
            EnumA, EnumB
        }
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classBy(null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classBy(EnumCheck.class));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classBy(Object.class));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classBy(Object.class.getName()));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classBy(Object.class.getCanonicalName()));

        Assertions.assertNull(ObjectContainer.classBy(null));
        Assertions.assertNotNull(ObjectContainer.classBy(EnumCheck.class));
        Assertions.assertNotNull(ObjectContainer.classBy(Object.class));
        Assertions.assertNotNull(ObjectContainer.classBy(Object.class.getName()));
        Assertions.assertNotNull(ObjectContainer.classBy(Object.class.getCanonicalName()));
    }

    @Test
    @DisplayName("Deve validar metodo class to String")
    void UT_CHECK_CLASS_TO_STRING() {
        enum EnumCheck {
            EnumA, EnumB
        }
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToString(null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToString("test"));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToString(ObjectCheck.class));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.classToString(new ObjectCheck()));

        Assertions.assertEquals(ObjectContainer.classToString(null), "");
        Assertions.assertEquals(ObjectContainer.classToString("test"), "");
        Assertions.assertEquals(ObjectContainer.classToString(ObjectCheck.class), ObjectCheck.class.getName());
        Assertions.assertEquals(ObjectContainer.classToString(new ObjectCheck()), ObjectCheck.class.getName());
    }

    @Test
    @DisplayName("Deve validar class ObjectContainer")
    void UT_CHECK_OF() {
        enum EnumCheck {
            EnumA, EnumB
        }
        Assertions.assertDoesNotThrow(() -> ObjectContainer.of(UUID.randomUUID(), new ObjectCheck()));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.of(UUID.randomUUID(), null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.of((UUID) null, null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.of(EnumCheck.EnumA, new ObjectCheck()));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.of(EnumCheck.EnumA, null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.of(null, null));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.of(new ObjectCheck()));
        Assertions.assertDoesNotThrow(() -> ObjectContainer.of(null));

    }

    @Test
    @DisplayName("Deve validar class can type")
    void UT_CHECK_CAN_TYPE() {
        enum EnumCheck {
            EnumA, EnumB
        }
        var objectContainer = ObjectContainer.of(UUID.randomUUID(), new ObjectCheck());
        Assertions.assertDoesNotThrow(() -> objectContainer.canType(null));
        Assertions.assertDoesNotThrow(() -> objectContainer.canType(ObjectCheck.class));

        Assertions.assertFalse(objectContainer.canType(null));
        Assertions.assertFalse(objectContainer.canType("test"));
        Assertions.assertFalse(objectContainer.canType(EnumCheck.EnumA));
        Assertions.assertFalse(objectContainer.canType(EnumCheck.class));
        Assertions.assertTrue(objectContainer.canType(ObjectCheck.class));
    }

    @Getter
    @Setter
    static class ObjectCheck {
        private final UUID id;

        ObjectCheck() {
            this.id = UUID.randomUUID();
        }
    }
}
