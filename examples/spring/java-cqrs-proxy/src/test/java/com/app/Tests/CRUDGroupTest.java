package com.app.Tests;

import com.app.business.dto.GroupIn;
import com.app.business.model.ofservice.ProxyGroup;
import com.app.business.service.crud.CRUDGroupService;
import com.app.factory.FactoryByTests;
import com.littlecode.containers.ObjectReturn;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CRUDGroupTest {

    private final FactoryByTests FACTORY = new FactoryByTests();
    private final CRUDGroupService CRUD = FACTORY.getCrudGroupService();
    private final List<ProxyGroup> CRUDList = FACTORY.getGroupList();

    @Test
    public void UT_000_CHECK() {
        for (var item : CRUDList) {

            var in = CRUD.findIn(item.getId()).cast(GroupIn.class);
            Assertions.assertNotNull(in);
            Assertions.assertNotNull(in.getId());
            Assertions.assertEquals(in.getId(), item.getId());
            Assertions.assertNotNull(in.getDtChange());
            Assertions.assertTrue(CRUD.disable(in.getId()).isOK());
            Assertions.assertTrue(CRUD.disable(in.getId()).isOK());
            Assertions.assertEquals(CRUD.disable((String) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.disable((UUID) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.disable((GroupIn) null).getType(), ObjectReturn.Type.BadRequest);
            Assertions.assertEquals(CRUD.disable(UUID.randomUUID()).getType(), ObjectReturn.Type.NoContent);

            Assertions.assertNotNull(CRUD.saveIn(in));
            in.setId(null);
            in.setDtChange(null);
            var outIn = CRUD.saveIn(in).cast(GroupIn.class);
            Assertions.assertNotNull(outIn);
            Assertions.assertNotNull(outIn.getId());
            Assertions.assertNotNull(outIn.getDtChange());
        }
        Assertions.assertNotNull(CRUD.list().getBody());
    }


}
