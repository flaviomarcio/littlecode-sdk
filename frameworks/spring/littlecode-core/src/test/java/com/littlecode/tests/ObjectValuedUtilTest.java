package com.littlecode.tests;

import com.littlecode.parsers.ObjectValueUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ObjectValuedUtilTest {

    @Test
    public void UT_000_CHECK_CONSTRUCTOR() {
        Assertions.assertThrows(NullPointerException.class, () -> new ObjectValueUtil(null));
        Assertions.assertThrows(NullPointerException.class, () -> ObjectValueUtil.of(null));
        Assertions.assertDoesNotThrow(() -> new ObjectValueUtil(new PrivateObject()));
        Assertions.assertDoesNotThrow(() -> ObjectValueUtil.of(new PrivateObject()));
    }

    @Test
    public void UT_000_CHECK_GETTER() {
        var objValueUtil=ObjectValueUtil.of(new PrivateObject());

        Assertions.assertThrows(NullPointerException.class, () -> objValueUtil.setTarget(null));
        Assertions.assertDoesNotThrow(() -> objValueUtil.setTarget(new PrivateObject()));

        Assertions.assertDoesNotThrow(objValueUtil::toString);
        Assertions.assertDoesNotThrow(objValueUtil::getTarget);
        Assertions.assertDoesNotThrow(objValueUtil::getFieldMap);
        Assertions.assertDoesNotThrow(objValueUtil::getFieldList);
        Assertions.assertDoesNotThrow(objValueUtil::getFieldNames);
        Assertions.assertDoesNotThrow(objValueUtil::asMap);
        Assertions.assertDoesNotThrow(objValueUtil::asMapString);

        Assertions.assertNotNull(objValueUtil.toString());
        Assertions.assertNotNull(objValueUtil.getTarget());
        Assertions.assertNotNull(objValueUtil.getFieldMap());
        Assertions.assertNotNull(objValueUtil.getFieldList());
        Assertions.assertNotNull(objValueUtil.getFieldNames());
        Assertions.assertNotNull(objValueUtil.asMap());
        Assertions.assertNotNull(objValueUtil.asMapString());



    }

    @Test
    public void UT_000_CHECK_GET_VALUES() {
        var privateObject=new PrivateObject();
        var objValueUtil=ObjectValueUtil.of(privateObject);

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.getFieldValue((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.getFieldValue((Field)null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asValue((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asValue((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asString((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asString((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asInt((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asInt((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asLong((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asLong((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asDouble((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asDouble((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asLocalDate((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asLocalDate((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asLocalTime((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asLocalTime((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asLocalDateTime((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asLocalDateTime((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asUUID((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asUUID((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asURI((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asURI((Field) null));

        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asPath((String)null));
        Assertions.assertThrows(NullPointerException.class, ()-> objValueUtil.asPath((Field) null));

        for(var field:objValueUtil.getFieldList()){
            Assertions.assertFalse(objValueUtil.contains("teste"));
            Assertions.assertFalse(objValueUtil.contains((Field)null));
            Assertions.assertTrue(objValueUtil.contains(field));
            Assertions.assertTrue(objValueUtil.contains(field.getName()));
            Assertions.assertNotNull(objValueUtil.getField(field.getName()));

            if(field.getName().equals("vObject"))
            {
                Assertions.assertNull(objValueUtil.getFieldValue(field));
                Assertions.assertNull(objValueUtil.getFieldValue(field.getName()));
            }
            else{
                Assertions.assertNotNull(objValueUtil.getFieldValue(field));
                Assertions.assertNotNull(objValueUtil.getFieldValue(field.getName()));
            }

            Assertions.assertDoesNotThrow(()-> objValueUtil.asValue(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asValue(field.getName()));

            Assertions.assertDoesNotThrow(()-> objValueUtil.asString(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asString(field.getName()));
        }

        {
            var field=objValueUtil.getField("vString");
            Assertions.assertDoesNotThrow(()->objValueUtil.asString(field));
            Assertions.assertDoesNotThrow(()->objValueUtil.asString(field.getName()));
            Assertions.assertEquals(objValueUtil.asString(field),privateObject.vString);
            Assertions.assertEquals(objValueUtil.asString(field.getName()),privateObject.vString);
        }

        {
            var field=objValueUtil.getField("vInt");
            Assertions.assertDoesNotThrow(()->objValueUtil.asInt(field));
            Assertions.assertDoesNotThrow(()->objValueUtil.asInt(field.getName()));
            Assertions.assertEquals(objValueUtil.asInt(field),privateObject.vInt);
            Assertions.assertEquals(objValueUtil.asInt(field.getName()),privateObject.vInt);
        }

        {
            var field=objValueUtil.getField("vLong");
            Assertions.assertDoesNotThrow(()-> objValueUtil.asLong(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asLong(field.getName()));
            Assertions.assertEquals(objValueUtil.asLong(field),privateObject.vLong);
            Assertions.assertEquals(objValueUtil.asLong(field.getName()),privateObject.vLong);
        }

        {
            var field=objValueUtil.getField("vDouble");
            Assertions.assertDoesNotThrow(()-> objValueUtil.asDouble(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asDouble(field.getName()));
            Assertions.assertEquals(objValueUtil.asDouble(field),privateObject.vDouble);
            Assertions.assertEquals(objValueUtil.asDouble(field.getName()),privateObject.vDouble);
        }

        {
            var field=objValueUtil.getField("vLocalDate");
            Assertions.assertDoesNotThrow(()-> objValueUtil.asLocalDate(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asLocalDate(field.getName()));
            Assertions.assertEquals(objValueUtil.asLocalDate(field),privateObject.vLocalDate);
            Assertions.assertEquals(objValueUtil.asLocalDate(field.getName()),privateObject.vLocalDate);
        }

        {
            var field=objValueUtil.getField("vLocalTime");
            Assertions.assertDoesNotThrow(()-> objValueUtil.asLocalTime(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asLocalTime(field.getName()));
            Assertions.assertEquals(objValueUtil.asLocalTime(field),privateObject.vLocalTime);
            Assertions.assertEquals(objValueUtil.asLocalTime(field.getName()),privateObject.vLocalTime);
        }

        {
            var field=objValueUtil.getField("vLocalDateTime");
            Assertions.assertDoesNotThrow(()-> objValueUtil.asLocalDateTime(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asLocalDateTime(field.getName()));
            Assertions.assertEquals(objValueUtil.asLocalDateTime(field),privateObject.vLocalDateTime);
            Assertions.assertEquals(objValueUtil.asLocalDateTime(field.getName()),privateObject.vLocalDateTime);
        }

        {
            var field=objValueUtil.getField("vUUID");
            Assertions.assertDoesNotThrow(()-> objValueUtil.asUUID(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asUUID(field.getName()));
            Assertions.assertEquals(objValueUtil.asUUID(field),privateObject.vUUID);
            Assertions.assertEquals(objValueUtil.asUUID(field.getName()),privateObject.vUUID);
        }

        {
            var field=objValueUtil.getField("vURI");
            Assertions.assertDoesNotThrow(()-> objValueUtil.asURI(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asURI(field.getName()));
            Assertions.assertEquals(objValueUtil.asURI(field),privateObject.vURI);
            Assertions.assertEquals(objValueUtil.asURI(field.getName()),privateObject.vURI);
        }

        {
            var field=objValueUtil.getField("vPath");
            Assertions.assertDoesNotThrow(()-> objValueUtil.asPath(field));
            Assertions.assertDoesNotThrow(()-> objValueUtil.asPath(field.getName()));
            Assertions.assertEquals(objValueUtil.asPath(field),privateObject.vPath);
            Assertions.assertEquals(objValueUtil.asPath(field.getName()),privateObject.vPath);
        }



    }

    @Getter
    private static class PrivateObject {
        private final UUID vObject=null;
        private final String vString=UUID.randomUUID().toString();
        private final UUID vUUID=UUID.randomUUID();
        private final URI vURI=URI.create("/tmp/file");
        private final Path vPath=Path.of("/tmp/file");
        private final LocalDate vLocalDate=LocalDate.now();
        private final LocalTime vLocalTime=LocalTime.now();
        private final LocalDateTime vLocalDateTime=LocalDateTime.now();
        private final int vInt=12;
        private final double vDouble=12;
        private final long vLong=112233454;
    }

}
