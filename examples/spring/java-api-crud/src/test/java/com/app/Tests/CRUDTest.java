package com.app.Tests;

import com.app.business.dto.ItemModelDTO;
import com.app.business.model.ItemModel;
import com.app.business.service.CRUDService;
import com.app.factory.FactoryByTests;
import com.littlecode.containers.ObjectReturn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CRUDTest {

    private final FactoryByTests FACTORY = new FactoryByTests();
    private final CRUDService CRUD = FACTORY.getCommanderService();
    private final List<ItemModel> CRUDList = FACTORY.getCommanderModelList();

    private boolean dataValidation(ItemModelDTO target) {
        Assertions.assertNotNull(target);
        Assertions.assertNotNull(target.getId());
        Assertions.assertNotNull(target.getDtChange());
        return true;
    }

    @Test
    public void UT_000_CHECK_SAVE() {

        for (var model : CRUDList) {

            var in = CRUD.get(model.getId()).cast(ItemModelDTO.class);
            Assertions.assertNotNull(in);
            Assertions.assertNotNull(in.getId());
            Assertions.assertEquals(in.getId(), model.getId());
            Assertions.assertNotNull(in.getDtChange());

            Assertions.assertNotNull(CRUD.saveIn(in));
            in.setId(null);
            in.setDtChange(null);
            var outIn = CRUD.saveIn(in);
            Assertions.assertTrue(outIn.isOK());
            Assertions.assertTrue(this.dataValidation(outIn.cast(ItemModelDTO.class)));

        }
    }

    @Test
    public void UT_000_CHECK_DELETE() {
        for (var model : CRUDList) {
            var in = CRUD.get(model.getId()).cast(ItemModelDTO.class);
            Assertions.assertTrue(CRUD.delete(in.getId()).isOK());
            Assertions.assertTrue(CRUD.delete(in.getId()).isOK());
            Assertions.assertEquals(CRUD.delete((String) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.delete("").getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.delete((UUID) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.delete(in).getType(), ObjectReturn.Type.Success);
            Assertions.assertEquals(CRUD.delete(UUID.randomUUID()).getType(), ObjectReturn.Type.NoContent);
            in.setId(null);
            Assertions.assertEquals(CRUD.delete(in).getType(), ObjectReturn.Type.BadRequest);
        }
    }


}
