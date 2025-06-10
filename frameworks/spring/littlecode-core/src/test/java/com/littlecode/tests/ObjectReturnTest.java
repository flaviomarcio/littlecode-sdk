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
    @DisplayName("Deve validar equals and hashCode")
    public void UT_DEVE_VALIDAR_EQUALS_AND_HASHCODE() {
        var objA = ObjectReturn.of(Map.of("id", 12345));
        var objB = ObjectReturn.of(Map.of("id", 12345));
        var objC = ObjectReturn.of(Map.of("id", UUID.randomUUID()));
        Assertions.assertTrue(objA.equals(objA));
        Assertions.assertTrue(objA.equals(objB));
        Assertions.assertTrue(objA.hashCode() == objB.hashCode());
        Assertions.assertFalse(objA.equals(objC));
        Assertions.assertFalse(objA.hashCode() == objC.hashCode());
    }


    @Test
    @DisplayName("Deve validar ObjectReturn.type")
    public void UT_DIRECT_RETURN_TYPE() {

        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(ObjectReturn.Type.Success));
        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(ObjectReturn.Type.Success, Object.class));
        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(ObjectReturn.Type.Success, (Class<?>) null));
        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(ObjectReturn.Type.Success, (String) null));
        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(ObjectReturn.Type.Success, "teste"));

        Assertions.assertDoesNotThrow(() -> ObjectReturn.Empty().getType());
        Assertions.assertDoesNotThrow(() -> ObjectReturn.Empty().getStatusCode());
        Assertions.assertDoesNotThrow(() -> ObjectReturn.Empty().getStatus());
        Assertions.assertEquals(ObjectReturn.Empty().getType(), ObjectReturn.Type.Success);

        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(null));
        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(null, Object.class));
        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(null, (Class<?>) null));
        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(null, (String) null));
        Assertions.assertDoesNotThrow(() -> ObjectReturn.type(null, "teste"));

        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success));
        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success, Object.class));
        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success, (Class<?>) null));
        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success, (String) null));
        Assertions.assertNotNull(ObjectReturn.type(ObjectReturn.Type.Success, "teste"));

        Assertions.assertEquals(ObjectReturn.Empty().getType(), ObjectReturn.Type.Success);
        Assertions.assertTrue(ObjectReturn.Success().isOK());
        Assertions.assertTrue(ObjectReturn.Fail().isError());
        Assertions.assertFalse(ObjectReturn.Empty().is1xxInformational());
        Assertions.assertTrue(ObjectReturn.OK().is2xxSuccessful());
        Assertions.assertTrue(ObjectReturn.Accepted().is2xxSuccessful());
        Assertions.assertFalse(ObjectReturn.Accepted().is3xxRedirection());
        Assertions.assertTrue(ObjectReturn.BadRequest().is4xxClientError());
        Assertions.assertTrue(ObjectReturn.Fail().is5xxServerError());

        Assertions.assertEquals(ObjectReturn.Empty().getType(), ObjectReturn.Type.Success);
        Assertions.assertTrue(ObjectReturn.Success().isOK());
        Assertions.assertTrue(ObjectReturn.Fail().isError());
        Assertions.assertFalse(ObjectReturn.Empty().is1xxInformational());
        Assertions.assertTrue(ObjectReturn.OK().is2xxSuccessful());
        Assertions.assertTrue(ObjectReturn.Created().isCreated());
        Assertions.assertTrue(ObjectReturn.Accepted().isAccepted());
        Assertions.assertTrue(ObjectReturn.BadRequest().isBadRequest());
        Assertions.assertTrue(ObjectReturn.NotFound().isNotFound());
        Assertions.assertTrue(ObjectReturn.Conflict().isConflict());

        Assertions.assertEquals(ObjectReturn.create().Success().end().getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.create().OK().end().getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.create().Created().end().getType(), ObjectReturn.Type.Created);
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
        Assertions.assertEquals(ObjectReturn.OK(this.getClass()).getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Created(this.getClass()).getType(), ObjectReturn.Type.Created);
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
        Assertions.assertEquals(ObjectReturn.OK().getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Created().getType(), ObjectReturn.Type.Created);
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
        Assertions.assertEquals(ObjectReturn.OK("Message").getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Created("Message").getType(), ObjectReturn.Type.Created);
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
        Assertions.assertEquals(ObjectReturn.OK("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Created("Message: %s", UUID.randomUUID()).getType(), ObjectReturn.Type.Created);
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
        Assertions.assertEquals(ObjectReturn.OK().getType(), ObjectReturn.Type.Success);
        Assertions.assertEquals(ObjectReturn.Created().getType(), ObjectReturn.Type.Created);
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

        Assertions.assertDoesNotThrow(() -> ObjectReturn.of(ObjectReturn.Empty()));
        Assertions.assertDoesNotThrow(() -> ObjectReturn.of((ObjectReturn) null));

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
                ObjectReturn.of(Map.of("a", "b"))
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

    @Test
    @DisplayName("Deve validar subclass Result Info")
    public void UT_CHECK_RETURN_BY_RESULT_INFO() {
        Assertions.assertDoesNotThrow(() -> new ObjectReturn.ResultInfo());

        var resultInfo = new ObjectReturn.ResultInfo();
        Assertions.assertDoesNotThrow(resultInfo::getResult);
        Assertions.assertDoesNotThrow(resultInfo::getPage);
        Assertions.assertDoesNotThrow(resultInfo::getCode);
        Assertions.assertDoesNotThrow(resultInfo::getCount);
        Assertions.assertDoesNotThrow(resultInfo::getPerPage);
        Assertions.assertDoesNotThrow(resultInfo::getMessages);
        Assertions.assertDoesNotThrow(resultInfo::getTotalCount);
        Assertions.assertDoesNotThrow(resultInfo::getTotalPages);
        Assertions.assertDoesNotThrow(resultInfo::isSuccess);

        Assertions.assertDoesNotThrow(() -> resultInfo.setResult(null));
        Assertions.assertDoesNotThrow(() -> resultInfo.setPage(0));
        Assertions.assertDoesNotThrow(() -> resultInfo.setCode(0));
        Assertions.assertDoesNotThrow(() -> resultInfo.setCount(0));
        Assertions.assertDoesNotThrow(() -> resultInfo.setPerPage(0));
        Assertions.assertDoesNotThrow(() -> resultInfo.setMessages(null));
        Assertions.assertDoesNotThrow(() -> resultInfo.setTotalCount(0));
        Assertions.assertDoesNotThrow(() -> resultInfo.setTotalPages(0));

    }


    @Test
    @DisplayName("Deve validar subclass-message")
    public void UT_CHECK_RETURN_BY_MESSAGE() {
        Assertions.assertDoesNotThrow(() -> new ObjectReturn.Message());
        Assertions.assertDoesNotThrow(() -> ObjectReturn.Message.builder().build());

        var message = ObjectReturn.Message.builder().build();
        Assertions.assertDoesNotThrow(message::getMessage);
        Assertions.assertDoesNotThrow(message::getCode);
        Assertions.assertDoesNotThrow(message::getType);

        Assertions.assertDoesNotThrow(() -> message.setMessage(null));
        Assertions.assertDoesNotThrow(() -> message.setCode(null));

        Assertions.assertDoesNotThrow(() -> message.setType(ObjectReturn.Type.NoContent));
        Assertions.assertDoesNotThrow(message::isSuccess);
        Assertions.assertFalse(message::isSuccess);

        Assertions.assertDoesNotThrow(() -> message.setType(null));
        Assertions.assertDoesNotThrow(message::isSuccess);
        Assertions.assertFalse(message::isSuccess);

        Assertions.assertDoesNotThrow(() -> message.setType(ObjectReturn.Type.Success));
        Assertions.assertDoesNotThrow(message::isSuccess);
        Assertions.assertTrue(message::isSuccess);

    }

    @Test
    @DisplayName("Deve validar subclass-message-maker")
    public void UT_CHECK_RETURN_BY_MESSAGE_MAKER() {
        Assertions.assertDoesNotThrow(() -> new ObjectReturn.MessageMaker(ObjectReturn.Empty()));

        var messageMaker = new ObjectReturn.MessageMaker(ObjectReturn.Empty());
        Assertions.assertDoesNotThrow(messageMaker::clear);
        Assertions.assertDoesNotThrow(messageMaker::end);
        Assertions.assertDoesNotThrow(() -> messageMaker.type(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.type(ObjectReturn.Type.NoContent));
        Assertions.assertDoesNotThrow(() -> messageMaker.type(ObjectReturn.Type.NoContent, "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.type(ObjectReturn.Type.NoContent, null));
        Assertions.assertDoesNotThrow(() -> messageMaker.type(null, null));
        Assertions.assertDoesNotThrow(() -> messageMaker.type(ObjectReturn.Type.NoContent, "%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.type(ObjectReturn.Type.NoContent, "%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.type(ObjectReturn.Type.NoContent, null, null));
        Assertions.assertDoesNotThrow(() -> messageMaker.type(null, null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.code(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.code(1L));

        Assertions.assertDoesNotThrow(() -> messageMaker.message((String) null));
        Assertions.assertDoesNotThrow(() -> messageMaker.message("test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.message(Object.class));
        Assertions.assertDoesNotThrow(() -> messageMaker.message((Class) null));
        Assertions.assertDoesNotThrow(() -> messageMaker.message("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.message("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.message(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.Success());
        Assertions.assertDoesNotThrow(() -> messageMaker.Success(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Success("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.Success("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Success(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.OK());
        Assertions.assertDoesNotThrow(() -> messageMaker.OK(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.OK("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.OK("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.OK(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.Created());
        Assertions.assertDoesNotThrow(() -> messageMaker.Created(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Created("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.Created("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Created(null, null));


        Assertions.assertDoesNotThrow(() -> messageMaker.Accepted());
        Assertions.assertDoesNotThrow(() -> messageMaker.Accepted(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Accepted("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.Accepted("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Accepted(null, null));



        Assertions.assertDoesNotThrow(() -> messageMaker.NoContent());
        Assertions.assertDoesNotThrow(() -> messageMaker.NoContent(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NoContent("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.NoContent("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NoContent(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.BadRequest());
        Assertions.assertDoesNotThrow(() -> messageMaker.BadRequest(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.BadRequest("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.BadRequest("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.BadRequest(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.Forbidden());
        Assertions.assertDoesNotThrow(() -> messageMaker.Forbidden(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Forbidden("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.Forbidden("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Forbidden(null, null));


        Assertions.assertDoesNotThrow(() -> messageMaker.NotFound());
        Assertions.assertDoesNotThrow(() -> messageMaker.NotFound(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotFound("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotFound("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotFound(null, null));


        Assertions.assertDoesNotThrow(() -> messageMaker.Conflict());
        Assertions.assertDoesNotThrow(() -> messageMaker.Conflict(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Conflict("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.Conflict("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Conflict(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.NotAllowed());
        Assertions.assertDoesNotThrow(() -> messageMaker.NotAllowed(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotAllowed("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotAllowed("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotAllowed(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.NotAcceptable());
        Assertions.assertDoesNotThrow(() -> messageMaker.NotAcceptable(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotAcceptable("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotAcceptable("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotAcceptable(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.Unauthorized());
        Assertions.assertDoesNotThrow(() -> messageMaker.Unauthorized(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Unauthorized("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.Unauthorized("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Unauthorized(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.NotImplemented());
        Assertions.assertDoesNotThrow(() -> messageMaker.NotImplemented(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotImplemented("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotImplemented("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.NotImplemented(null, null));

        Assertions.assertDoesNotThrow(() -> messageMaker.Fail());
        Assertions.assertDoesNotThrow(() -> messageMaker.Fail(null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Fail("%s", "test"));
        Assertions.assertDoesNotThrow(() -> messageMaker.Fail("%s", null));
        Assertions.assertDoesNotThrow(() -> messageMaker.Fail(null, null));

    }


}
