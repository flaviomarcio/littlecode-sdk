package com.littlecode.tests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.littlecode.config.UtilCoreConfig;
import com.littlecode.exceptions.FrameworkException;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ObjectUtilTest {
    private static final LocalDate MAX_DATE = LocalDate.of(1901, 1, 1);
    private static final LocalTime MAX_TIME = LocalTime.of(23, 59, 59);
    private static final LocalDateTime MAX_DATETIME = LocalDateTime.of(MAX_DATE, MAX_TIME);
    private static final ObjectCheck objectSrc = getObjectChecker();
    private static final String objectSrcString = getObjectString(objectSrc);
    //estatico para garantir que o md5 está sendo gerado como esperado
    private static final UUID objectMd5StaticUUID = getObjectMD5(objectSrc);
    private static final String objectMd5Static = objectMd5StaticUUID.toString()
            .replace("-", "")
            .replace("{", "")
            .replace("}", "");
    private static final UUID uuidOut1 = UUID.randomUUID();
    private static final UUID uuidOut2 = UUID.randomUUID();

    @SuppressWarnings("SameParameterValue")
    private static String getObjectString(Object o) {
        if (o == null)
            return "";
        var objectMapper = UtilCoreConfig.newObjectMapper();
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
            return "";
        }
    }

    private static UUID getObjectMD5(Object o) {
        return HashUtil.toMd5Uuid(o);
    }

    private static ObjectCheck getObjectChecker() {
        return ObjectCheck
                .builder()
                .sub(SubObjectCheck.builder().id(uuidOut2).build())
                .type(ObjectCheckType.Type1)
                .id(uuidOut1)
                .date(MAX_DATE)
                .time(MAX_TIME)
                .dateTime(MAX_DATETIME)
                .doubleValue(1.678)
                .intValue(1999)
                .boolValue(true)
                .doubleValueClass(1.678)
                .intValueClass(1999)
                .boolValueClass(true)
                .stringValue("test: " + uuidOut2)
                .build();
    }

    @BeforeAll
    public static void beforeAll() {
    }

    @Test
    public void UT_000_CHECK_FIELDS() {
        Assertions.assertNotNull(ObjectUtil.toFieldsList(objectSrc));
        Assertions.assertNotNull(ObjectUtil.toFieldsList(objectSrc.getClass()));
        Assertions.assertFalse(ObjectUtil.toFieldsList(objectSrc).isEmpty());
        Assertions.assertFalse(ObjectUtil.toFieldsList(objectSrc.getClass()).isEmpty());

        Assertions.assertNotNull(ObjectUtil.toFieldsMap(objectSrc));
        Assertions.assertFalse(ObjectUtil.toFieldsMap(objectSrc).isEmpty());
        Assertions.assertFalse(ObjectUtil.toFieldsMap(objectSrc.getClass()).isEmpty());

        Object objectNull = null;
        Assertions.assertDoesNotThrow(() -> ObjectUtil.toFieldsList(objectNull));
        Assertions.assertNotNull(ObjectUtil.toFieldsList(objectNull));
        Assertions.assertTrue(ObjectUtil.toFieldsList(objectNull).isEmpty());

        Assertions.assertDoesNotThrow(() -> ObjectUtil.toFieldsMap(objectNull));
        Assertions.assertNotNull(ObjectUtil.toFieldsMap(objectNull));
        Assertions.assertTrue(ObjectUtil.toFieldsMap(objectNull).isEmpty());
    }

    @Test
    public void UT_000_CHECK_EQUAL() {
        Assertions.assertTrue(ObjectUtil.equal(objectSrc, objectSrc));
        Assertions.assertFalse(ObjectUtil.equal(objectSrc, this));
    }


    @Test
    public void UT_000_CHECK_TO_STRING_AND_TO_JSON() {
        Assertions.assertEquals(ObjectUtil.toString(objectSrc), objectSrcString);
    }

    @Test
    public void UT_000_CHECK_MD5() {
        var objectNew = ObjectUtil.createFromString(ObjectCheck.class, objectSrcString);
        Assertions.assertNotNull(objectNew);
        Assertions.assertEquals(objectSrc.getId(), objectNew.getId());

        var objectUUID = ObjectUtil.toMd5Uuid(objectNew);
        Assertions.assertEquals(objectUUID, objectMd5StaticUUID);
        var objectMd5 = ObjectUtil.toMd5(objectNew);
        Assertions.assertEquals(objectMd5, objectMd5Static);
    }

    @Test
    public void UT_000_CREATE_FROM() {
        Assertions.assertNotNull(ObjectUtil.createFromYML(ObjectCheck.class, objectSrcString));
        Assertions.assertNotNull(ObjectUtil.createFromPROPS(ObjectCheck.class, objectSrcString));
        Assertions.assertNotNull(ObjectUtil.createFromObject(ObjectCheck.class, objectSrcString));
        Assertions.assertNotNull(ObjectUtil.createFromXML(ObjectCheck.class, objectSrcString));
        Assertions.assertNotNull(ObjectUtil.createFromJSON(ObjectCheck.class, objectSrcString));
    }


    @Test
    public void UT_000_CHECK_MAPS() {
        var mapObjectA = ObjectUtil.toMapObject(objectSrcString);
        var mapObjectB = ObjectUtil.toMapObject(objectSrc);
        Assertions.assertFalse(mapObjectA.isEmpty());
        Assertions.assertFalse(mapObjectB.isEmpty());

        var objA = ObjectUtil.createFromValues(ObjectCheck.class, mapObjectA);
        Assertions.assertNotNull(objA);
        var objB = ObjectUtil.createFromValues(ObjectCheck.class, mapObjectB);
        Assertions.assertNotNull(objB);
        Assertions.assertTrue(ObjectUtil.equal(objA, objB));
    }

    @Test
    public void UT_000_CHECK_FIELD() {
        Assertions.assertNotNull(ObjectUtil.toFieldByAnnotation(ObjectCheck.class, NotNull.class));
        Assertions.assertFalse(ObjectUtil.toFieldsByAnnotation(ObjectCheck.class, NotNull.class).isEmpty());

        Assertions.assertNull(ObjectUtil.toFieldByAnnotation(SubObjectCheck.class, NotNull.class));
        Assertions.assertTrue(ObjectUtil.toFieldsByAnnotation(SubObjectCheck.class, NotNull.class).isEmpty());

        Assertions.assertNotNull(ObjectUtil.toFieldByName(ObjectCheck.class, "id"));
        Assertions.assertNull(ObjectUtil.toFieldByName(ObjectCheck.class, "test"));

        Assertions.assertNotNull(ObjectUtil.toFieldByType(ObjectCheck.class, UUID.class));
        Assertions.assertNull(ObjectUtil.toFieldByType(SubObjectCheck.class, Long.class));

        Assertions.assertFalse(ObjectUtil.toFieldsByType(ObjectCheck.class, UUID.class).isEmpty());
        Assertions.assertTrue(ObjectUtil.toFieldsByType(SubObjectCheck.class, Long.class).isEmpty());

    }

    @Test
    public void UT_000_CHECK_CREATE() {
        final var md5Src = ObjectUtil.toMapObject(objectSrc);

        //check 1
        var objNew = ObjectUtil.createFromObject(ObjectCheck.class, objectSrc);
        Assertions.assertNotNull(objNew);
        Assertions.assertEquals(objNew.getClass(), ObjectCheck.class);
        var md5New = ObjectUtil.toMapObject(objNew);
        Assertions.assertEquals(md5Src, md5New);

        //check 2
        objNew = ObjectUtil.createFromString(ObjectCheck.class, objectSrcString);
        Assertions.assertNotNull(objNew);
        Assertions.assertEquals(objNew.getClass(), ObjectCheck.class);
        md5New = ObjectUtil.toMapObject(objNew);
        Assertions.assertEquals(md5Src, md5New);

        try {
            File file = File.createTempFile("tmp", UUID.randomUUID().toString());
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(objectSrcString);
                writer.flush();
            } catch (IOException e) {
                throw new FrameworkException(e.getMessage());
            }
            objNew = ObjectUtil.createFromFile(ObjectCheck.class, file);
            Assertions.assertNotNull(objNew);
            Assertions.assertEquals(objNew.getClass(), ObjectCheck.class);
            md5New = ObjectUtil.toMapObject(objNew);
            Assertions.assertEquals(md5Src, md5New);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //check 3
        objNew = ObjectUtil.create(ObjectCheck.class);
        Assertions.assertNotNull(objNew);
        Assertions.assertEquals(objNew.getClass(), ObjectCheck.class);

        Assertions.assertNull(ObjectUtil.createWithArgsConstructor(ObjectCheckWithArgs.class));
        Assertions.assertNull(ObjectUtil.createWithArgsConstructor(ObjectCheckWithArgs.class, UUID.randomUUID()));
        Assertions.assertNull(ObjectUtil.createWithArgsConstructor(ObjectCheckWithArgs.class, UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID()));
        Assertions.assertNull(ObjectUtil.createWithArgsConstructor(ObjectCheckWithArgs.class, UUID.randomUUID(), UUID.randomUUID().toString()));
        Assertions.assertNull(ObjectUtil.createWithArgsConstructor(ObjectCheckWithArgs.class, UUID.randomUUID(), UUID.randomUUID()));

        var objArg = UUID.randomUUID();
        ObjectCheckWithArgs objArgNew = ObjectUtil.createWithArgsConstructor(ObjectCheckWithArgs.class, objArg, MAX_DATETIME);
        Assertions.assertNotNull(objArgNew);
        Assertions.assertEquals(objArgNew.getClass(), ObjectCheckWithArgs.class);
        Assertions.assertEquals(objArgNew.getId(), objArg);
        Assertions.assertEquals(objArgNew.getDt(), MAX_DATETIME);

    }

    @Test
    public void UT_000_CHECK_CLASSES() {
        Assertions.assertNotNull(ObjectUtil.getClassByName(ObjectCheckType.class.getName()));
        Assertions.assertNotNull(ObjectUtil.getClassByName(ObjectCheck.class.getName()));
        Assertions.assertNull(ObjectUtil.getClassByName("abc.1234.c4"));

        Assertions.assertNotNull(ObjectUtil.getClassesByAnnotation(AnnTest.class));
        Assertions.assertNotNull(ObjectUtil.getClassesByInherits(ObjectBase.class));

//        Assertions.assertFalse(ObjectUtil.getClassesByAnnotation(AnnTest.class).isEmpty());
//        Assertions.assertFalse(ObjectUtil.getClassesByInherits(ObjectBase.class).isEmpty());
    }

    private enum ObjectCheckType {
        Type1
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnnTest {
    }

    @Component
    @AnnTest
    public static class ObjectBase {

    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Builder
    @Component
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ObjectCheck extends ObjectBase {
        private SubObjectCheck sub;
        private ObjectCheckType type;

        @NotNull
        private UUID id;
        private LocalDate date;
        private LocalTime time;
        private LocalDateTime dateTime;
        private double doubleValue;
        private int intValue;
        private boolean boolValue;

        private String stringValue;
        private Long longClassClass;
        private Double doubleValueClass;
        private Integer intValueClass;
        private Boolean boolValueClass;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class SubObjectCheck {
        private UUID id;
    }

    @Getter
    private static class ObjectCheckWithArgs {
        private final UUID id;
        private final LocalDateTime dt;

        public ObjectCheckWithArgs(UUID id, LocalDateTime dt) {
            this.id = id;
            this.dt = dt;
        }
    }

}
