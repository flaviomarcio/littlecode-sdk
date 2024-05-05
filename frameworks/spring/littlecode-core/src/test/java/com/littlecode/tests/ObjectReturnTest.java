package com.littlecode.tests;

import com.littlecode.containers.ObjectReturn;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ObjectReturnTest {

    @Test
    @DisplayName("Deve validar ObjectReturn.type")
    public void UT_DIRECT_RETURN_TYPE() {

        Assertions.assertDoesNotThrow(()->ObjectReturn.type(ObjectReturn.Type.Success));
        Assertions.assertDoesNotThrow(()->ObjectReturn.type(ObjectReturn.Type.Success, Object.class));
        Assertions.assertDoesNotThrow(()->ObjectReturn.type(ObjectReturn.Type.Success, (Class<?>) null));
        Assertions.assertDoesNotThrow(()->ObjectReturn.type(ObjectReturn.Type.Success, (String) null));
        Assertions.assertDoesNotThrow(()->ObjectReturn.type(ObjectReturn.Type.Success, "teste"));

        Assertions.assertDoesNotThrow(()->ObjectReturn.Empty().getType());
        Assertions.assertEquals(ObjectReturn.Empty().getType(),ObjectReturn.Type.Success);

        Assertions.assertDoesNotThrow(()->ObjectReturn.type(null));
        Assertions.assertDoesNotThrow(()->ObjectReturn.type(null, Object.class));
        Assertions.assertDoesNotThrow(()->ObjectReturn.type(null, (Class<?>) null));
        Assertions.assertDoesNotThrow(()->ObjectReturn.type(null, (String) null));
        Assertions.assertDoesNotThrow(()->ObjectReturn.type(null, "teste"));

        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success));
        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success, Object.class));
        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success, (Class<?>) null));
        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success, (String) null));
        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success, "teste"));

        Assertions.assertEquals(ObjectReturn.Empty().getType(), ObjectReturn.Type.Success);
        Assertions.assertTrue(ObjectReturn.Empty().isOK());

        Assertions.assertEquals(ObjectReturn.create().Success().end().getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.create().Accepted().end().getType(), ObjectReturn.Type.Accepted);
        Assertions.assertEquals(ObjectReturn.create().NoContent().end().getType(), ObjectReturn.Type.NoContent);
        Assertions.assertEquals(ObjectReturn.create().BadRequest().end().getType(), ObjectReturn.Type.BadRequest);
        Assertions.assertEquals(ObjectReturn.create().Forbidden().end().getType(), ObjectReturn.Type.Forbidden);
        Assertions.assertEquals(ObjectReturn.create().NotFound().end().getType(), ObjectReturn.Type.NotFound);
        Assertions.assertEquals(ObjectReturn.create().Conflict().end().getType(), ObjectReturn.Type.Conflict);
        Assertions.assertEquals(ObjectReturn.create().NotAllowed().end().getType(), ObjectReturn.Type.NotAllowed);
        Assertions.assertEquals(ObjectReturn.create().NotAcceptable().end().getType(), ObjectReturn.Type.NotAcceptable);
        Assertions.assertEquals(ObjectReturn.create().Unauthorized().end().getType(), ObjectReturn.Type.Unauthorized);
        Assertions.assertEquals(ObjectReturn.create().Fail().end().getType(), ObjectReturn.Type.Fail);
        Assertions.assertEquals(ObjectReturn.create().NotImplemented().end().getType(), ObjectReturn.Type.NotImplemented);

        Assertions.assertEquals(ObjectReturn.Success(this.getClass()).getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Accepted(this.getClass()).getType(), ObjectReturn.Type.Accepted);
        Assertions.assertEquals(ObjectReturn.NoContent(this.getClass()).getType(), ObjectReturn.Type.NoContent);
        Assertions.assertEquals(ObjectReturn.BadRequest(this.getClass()).getType(), ObjectReturn.Type.BadRequest);
        Assertions.assertEquals(ObjectReturn.Forbidden(this.getClass()).getType(), ObjectReturn.Type.Forbidden);
        Assertions.assertEquals(ObjectReturn.NotFound(this.getClass()).getType(), ObjectReturn.Type.NotFound);
        Assertions.assertEquals(ObjectReturn.Conflict(this.getClass()).getType(), ObjectReturn.Type.Conflict);
        Assertions.assertEquals(ObjectReturn.NotAllowed(this.getClass()).getType(), ObjectReturn.Type.NotAllowed);
        Assertions.assertEquals(ObjectReturn.NotAcceptable(this.getClass()).getType(), ObjectReturn.Type.NotAcceptable);
        Assertions.assertEquals(ObjectReturn.Unauthorized(this.getClass()).getType(), ObjectReturn.Type.Unauthorized);
        Assertions.assertEquals(ObjectReturn.Fail(this.getClass()).getType(), ObjectReturn.Type.Fail);
        Assertions.assertEquals(ObjectReturn.NotImplemented(this.getClass()).getType(), ObjectReturn.Type.NotImplemented);

        Assertions.assertEquals(ObjectReturn.Success().getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Accepted().getType(), ObjectReturn.Type.Accepted);
        Assertions.assertEquals(ObjectReturn.NoContent().getType(), ObjectReturn.Type.NoContent);
        Assertions.assertEquals(ObjectReturn.BadRequest().getType(), ObjectReturn.Type.BadRequest);
        Assertions.assertEquals(ObjectReturn.Forbidden().getType(), ObjectReturn.Type.Forbidden);
        Assertions.assertEquals(ObjectReturn.NotFound().getType(), ObjectReturn.Type.NotFound);
        Assertions.assertEquals(ObjectReturn.Conflict().getType(), ObjectReturn.Type.Conflict);
        Assertions.assertEquals(ObjectReturn.NotAllowed().getType(), ObjectReturn.Type.NotAllowed);
        Assertions.assertEquals(ObjectReturn.NotAcceptable().getType(), ObjectReturn.Type.NotAcceptable);
        Assertions.assertEquals(ObjectReturn.Unauthorized().getType(), ObjectReturn.Type.Unauthorized);
        Assertions.assertEquals(ObjectReturn.Fail().getType(), ObjectReturn.Type.Fail);
        Assertions.assertEquals(ObjectReturn.NotImplemented().getType(), ObjectReturn.Type.NotImplemented);

        Assertions.assertEquals(ObjectReturn.Success("Message").getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Accepted("Message").getType(), ObjectReturn.Type.Accepted);
        Assertions.assertEquals(ObjectReturn.NoContent("Message").getType(), ObjectReturn.Type.NoContent);
        Assertions.assertEquals(ObjectReturn.BadRequest("Message").getType(), ObjectReturn.Type.BadRequest);
        Assertions.assertEquals(ObjectReturn.Forbidden("Message").getType(), ObjectReturn.Type.Forbidden);
        Assertions.assertEquals(ObjectReturn.NotFound("Message").getType(), ObjectReturn.Type.NotFound);
        Assertions.assertEquals(ObjectReturn.Conflict("Message").getType(), ObjectReturn.Type.Conflict);
        Assertions.assertEquals(ObjectReturn.NotAllowed("Message").getType(), ObjectReturn.Type.NotAllowed);
        Assertions.assertEquals(ObjectReturn.NotAcceptable("Message").getType(), ObjectReturn.Type.NotAcceptable);
        Assertions.assertEquals(ObjectReturn.Unauthorized("Message").getType(), ObjectReturn.Type.Unauthorized);
        Assertions.assertEquals(ObjectReturn.Fail("Message").getType(), ObjectReturn.Type.Fail);
        Assertions.assertEquals(ObjectReturn.NotImplemented("Message").getType(), ObjectReturn.Type.NotImplemented);

        Assertions.assertEquals(ObjectReturn.Success("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Accepted("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.Accepted);
        Assertions.assertEquals(ObjectReturn.NoContent("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.NoContent);
        Assertions.assertEquals(ObjectReturn.BadRequest("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.BadRequest);
        Assertions.assertEquals(ObjectReturn.Forbidden("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.Forbidden);
        Assertions.assertEquals(ObjectReturn.NotFound("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.NotFound);
        Assertions.assertEquals(ObjectReturn.Conflict("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.Conflict);
        Assertions.assertEquals(ObjectReturn.NotAllowed("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.NotAllowed);
        Assertions.assertEquals(ObjectReturn.NotAcceptable("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.NotAcceptable);
        Assertions.assertEquals(ObjectReturn.Unauthorized("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.Unauthorized);
        Assertions.assertEquals(ObjectReturn.Fail("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.Fail);
        Assertions.assertEquals(ObjectReturn.NotImplemented("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.NotImplemented);

        Assertions.assertEquals(ObjectReturn.Success().getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Accepted().getType(), ObjectReturn.Type.Accepted);
        Assertions.assertEquals(ObjectReturn.NoContent().getType(), ObjectReturn.Type.NoContent);
        Assertions.assertEquals(ObjectReturn.BadRequest().getType(), ObjectReturn.Type.BadRequest);
        Assertions.assertEquals(ObjectReturn.Forbidden().getType(), ObjectReturn.Type.Forbidden);
        Assertions.assertEquals(ObjectReturn.NotFound().getType(), ObjectReturn.Type.NotFound);
        Assertions.assertEquals(ObjectReturn.Conflict().getType(), ObjectReturn.Type.Conflict);
        Assertions.assertEquals(ObjectReturn.NotAllowed().getType(), ObjectReturn.Type.NotAllowed);
        Assertions.assertEquals(ObjectReturn.NotAcceptable().getType(), ObjectReturn.Type.NotAcceptable);
        Assertions.assertEquals(ObjectReturn.Unauthorized().getType(), ObjectReturn.Type.Unauthorized);
        Assertions.assertEquals(ObjectReturn.Fail().getType(), ObjectReturn.Type.Fail);
        Assertions.assertEquals(ObjectReturn.NotImplemented().getType(), ObjectReturn.Type.NotImplemented);

        List.of(ObjectReturn.Type.values())
                .forEach(type -> {
                    var oCheck = ObjectReturn.create()
                            .type(type)
                            .message("Invalid %s", type)
                            .end();
                    Assertions.assertEquals(oCheck.getType(), type);

                    oCheck = ObjectReturn.type(type, "Invalid %s", type);
                    Assertions.assertEquals(oCheck.getType(), type);
                });

    }

    @Test
    @DisplayName("Deve validar ObjectReturn.of")
    public void UT_CHECK_RETURN_OF_OBJECTS() {
        ObjectReturn objectReturn = new ObjectReturn();
        Assertions.assertTrue(objectReturn.isOK());

        Assertions.assertTrue(ObjectReturn.of(new Object()).isOK());
        Assertions.assertEquals(ObjectReturn.of(new Object()).getType(), ObjectReturn.Type.Success);
        Assertions.assertNotNull(ObjectReturn.of(this).cast(ObjectReturnTest.class));
        Assertions.assertNull(ObjectReturn.of(new Object()).cast(ObjectReturnTest.class));

        Object o = null;
        Assertions.assertNull(ObjectReturn.of(o).cast(ObjectReturnTest.class));

        try {
            //noinspection DataFlowIssue,ResultOfMethodCallIgnored
            o.toString();
        } catch (Exception e) {
            Assertions.assertFalse(ObjectReturn.of(e).isOK());
            Assertions.assertEquals(ObjectReturn.of(e).getType(), ObjectReturn.Type.Fail);
        }
    }

    @Test
    @DisplayName("Deve validar ObjectReturn.create")
    public void UT_CHECK_RETURN_BY_OBJECT_CREATE() {
        Assertions.assertEquals(
                ObjectReturn.create()
                        .type(ObjectReturn.Type.NoContent)
                        .message("Invalid object")
                        .end()
                        .getType(),
                ObjectReturn.Type.NoContent);

        Assertions.assertEquals(
                ObjectReturn.create()
                        .type(ObjectReturn.Type.Fail)
                        .message(this.getClass())
                        .end()
                        .getType(),
                ObjectReturn.Type.Fail);

    }

    @Test
    @DisplayName("Deve validar falhas com mensagens falhas")
    public void UT_CHECK_RETURN_BY_MESSAGES_FAIL() {
        ObjectReturn objectReturn = ObjectReturn
                .create()
                .Fail()
                .message("Generic error")
                .end().body(Map.of("a", "b"));
        var listReturn = List.of(
                objectReturn,
                new ObjectReturn(objectReturn)
        );

        for (var oReturn : listReturn) {
            Assertions.assertFalse(oReturn.isOK());
            Assertions.assertEquals(oReturn.getType(), ObjectReturn.Type.Fail);
            Assertions.assertNotNull(oReturn.asResultHttp());
            Assertions.assertNotNull(oReturn.asResultInfo());
            Assertions.assertNotNull(oReturn.asResultPagination());
            Assertions.assertFalse(oReturn.asResultInfo().isSuccess());
            Assertions.assertFalse(oReturn.asResultInfo().getMessages().isEmpty());

            Assertions.assertTrue(oReturn.clear().isOK());
            Assertions.assertEquals(oReturn.getType(), ObjectReturn.Type.Success);
            Assertions.assertNotNull(oReturn.asResultHttp());
            Assertions.assertNotNull(oReturn.asResultInfo());
            Assertions.assertTrue(oReturn.asResultInfo().isSuccess());
            Assertions.assertTrue(oReturn.asResultInfo().getMessages().isEmpty());
        }
    }
    @Test
    @DisplayName("Deve validar falhas com mensagens falhas")
    public void UT_CHECK_RETURN_BY_MESSAGES_SUCESS() {
        var listReturn = List.of(
                ObjectReturn.Empty(),
                ObjectReturn.of(Map.of("a","b"))
        );

        for (var oReturn : listReturn) {
            Assertions.assertEquals(oReturn.getType(), ObjectReturn.Type.Success);
            Assertions.assertNotNull(oReturn.asResultHttp());
            Assertions.assertNotNull(oReturn.asResultInfo());
            Assertions.assertTrue(oReturn.asResultInfo().isSuccess());
            Assertions.assertTrue(oReturn.asResultInfo().getMessages().isEmpty());
        }

    }


    @Test
    @DisplayName("Deve validar conversao para adapters")
    public void UT_CHECK_RETURN_BY_ADAPTER() {


        List.of(ObjectReturn.Type.values())
                .forEach(type -> {
                    var oReturn = ObjectReturn
                            .create()
                            .type(type)
                            .message(this.getClass())
                            .end();
                    Assertions.assertTrue(oReturn.clear().isOK());
                    Assertions.assertNotNull(oReturn.asResultHttp());
                    Assertions.assertNotNull(oReturn.asResultInfo());
                });


    }


}
