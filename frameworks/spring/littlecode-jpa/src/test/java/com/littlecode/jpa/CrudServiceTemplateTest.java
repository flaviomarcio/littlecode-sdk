package com.littlecode.jpa;

import com.littlecode.containers.ObjectReturn;
import com.littlecode.jpa.crud.CrudServiceTemplate;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CrudServiceTemplateTest {

    @Test
    public void UT_000_CHECK_ScheduleGroup() {
        Assertions.assertDoesNotThrow(PrivateCRUD::new);
        var privateCRUD=new PrivateCRUD();

        Assertions.assertThrows(RuntimeException.class, ()->privateCRUD.findIn(UUID.randomUUID()));
        Assertions.assertThrows(RuntimeException.class, privateCRUD::list);
        Assertions.assertThrows(RuntimeException.class, ()->privateCRUD.disable((UUID)null));
        Assertions.assertThrows(RuntimeException.class, ()->privateCRUD.disable(""));
        Assertions.assertThrows(RuntimeException.class, ()->privateCRUD.disable(PrivateModelIn.builder().id(UUID.randomUUID()).build()));
        Assertions.assertThrows(RuntimeException.class, ()->privateCRUD.saveIn(PrivateModelIn.builder().build()));
        Assertions.assertThrows(RuntimeException.class, ()->privateCRUD.inFrom(PrivateModel.builder().build()));
        Assertions.assertEquals(privateCRUD.disable((PrivateModelIn)null).getType(), ObjectReturn.Type.BadRequest);
        Assertions.assertDoesNotThrow(privateCRUD::getFieldIdName);
    }

    @Builder
    @Getter
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrivateModelIn{
        @Id
        private UUID id;
    }

    @Builder
    @Getter
    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrivateModel{
        @Id
        private UUID id;
    }

    @NoArgsConstructor
    public static class PrivateCRUD extends CrudServiceTemplate<PrivateModel,PrivateModelIn> {
    }


}
