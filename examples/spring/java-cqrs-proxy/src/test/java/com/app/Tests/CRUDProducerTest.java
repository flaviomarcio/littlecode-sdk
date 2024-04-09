package com.app.Tests;

import com.app.business.dto.ProducerIn;
import com.app.business.model.ofservice.ProxyProducer;
import com.app.business.service.crud.CRUDProducerService;
import com.app.factory.FactoryByTests;
import com.littlecode.containers.ObjectReturn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CRUDProducerTest {
    private final FactoryByTests FACTORY = new FactoryByTests();
    private final CRUDProducerService CRUD = FACTORY.getCrudProducerService();
    private final List<ProxyProducer> CRUDList = FACTORY.getProducerList();

    @Test
    public void UT_000_CHECK() {
        for (var item : CRUDList) {

            var in = CRUD.findIn(item.getId()).cast(ProducerIn.class);
            Assertions.assertNotNull(in);
            Assertions.assertNotNull(in.getId());
            Assertions.assertEquals(in.getId(), item.getId());
            Assertions.assertNotNull(in.getDtChange());
            Assertions.assertTrue(CRUD.disable(in.getId()).isOK());
            Assertions.assertTrue(CRUD.disable(in.getId()).isOK());
            Assertions.assertEquals(CRUD.disable((String) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.disable((UUID) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.disable((ProducerIn) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.disable(UUID.randomUUID()).getType(), ObjectReturn.Type.NoContent);

            Assertions.assertNotNull(CRUD.saveIn(in));
            in.setId(null);
            in.setDtChange(null);
            var outIn = CRUD.saveIn(in).cast(ProducerIn.class);
            Assertions.assertNotNull(outIn);
            Assertions.assertNotNull(outIn.getId());
            Assertions.assertNotNull(outIn.getDtChange());
        }
        Assertions.assertNotNull(CRUD.list().getBody());
    }


}
