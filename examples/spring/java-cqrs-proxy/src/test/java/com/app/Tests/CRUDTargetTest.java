package com.app.Tests;

import com.app.business.dto.TargetIn;
import com.app.business.model.ofservice.ProxyTarget;
import com.app.business.service.crud.CRUDTargetService;
import com.app.factory.FactoryByTests;
import com.littlecode.containers.ObjectReturn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CRUDTargetTest {

    private final FactoryByTests FACTORY = new FactoryByTests();
    private final CRUDTargetService CRUD = FACTORY.getCrudTargetService();

    private final List<ProxyTarget> CRUDList = FACTORY.getTargetList();

    @Test
    public void UT_000_CHECK() {
        for (var item : CRUDList) {

            var in = CRUD.findIn(item.getId()).cast(TargetIn.class);
            Assertions.assertNotNull(in);
            Assertions.assertNotNull(in.getId());
            Assertions.assertEquals(in.getId(), item.getId());
            Assertions.assertNotNull(in.getDtChange());
            Assertions.assertTrue(CRUD.disable(in.getId()).isOK());
            Assertions.assertTrue(CRUD.disable(in.getId()).isOK());
            Assertions.assertEquals(CRUD.disable((String) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.disable((UUID) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.disable((TargetIn) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.disable(UUID.randomUUID()).getType(), ObjectReturn.Type.NoContent);

            Assertions.assertNotNull(CRUD.saveIn(in));
            in.setId(null);
            in.setDtChange(null);
            var outIn = CRUD.saveIn(in).cast(TargetIn.class);
            Assertions.assertNotNull(outIn);
            Assertions.assertNotNull(outIn.getId());
            Assertions.assertNotNull(outIn.getDtChange());
        }
        Assertions.assertNotNull(CRUD.list().getBody());
    }


    @Test
    public void UT_000_CHECK2() {
        for (var item : FACTORY.getTargetList()) {

            var in = CRUD.findIn(item.getTargetRev().getId()).cast(TargetIn.class);
            Assertions.assertNotNull(in);
            Assertions.assertNotNull(in.getId());
            Assertions.assertEquals(in.getId(), item.getTargetRev().getId());
            Assertions.assertNotNull(in.getDtChange());
            Assertions.assertNotNull(in.getDispatchers());
            Assertions.assertFalse(in.getDispatchers().isEmpty());
            Assertions.assertDoesNotThrow(() -> CRUD.disable(in.getId()));
            Assertions.assertFalse(CRUD.disable(UUID.randomUUID()).isOK());

            Assertions.assertNotNull(CRUD.saveIn(in));
            in.setId(null);
            in.setDtChange(null);
            var outIn = CRUD.saveIn(in).cast(TargetIn.class);
            Assertions.assertNotNull(outIn);
            Assertions.assertNotNull(outIn.getId());
            Assertions.assertNotNull(outIn.getDtChange());
            Assertions.assertNotNull(outIn.getDispatchers());
            Assertions.assertFalse(outIn.getDispatchers().isEmpty());

            UUID id = null;
            Assertions.assertFalse(CRUD.disable(id).isOK());
            Assertions.assertNotNull(CRUD.list().getBody());
        }
    }


}
