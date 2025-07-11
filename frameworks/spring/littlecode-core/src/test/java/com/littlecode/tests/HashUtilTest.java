package com.littlecode.tests;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.exceptions.ParserException;
import com.littlecode.parsers.HashUtil;
import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.PrimitiveUtil;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
class HashUtilTest {

    @Test
    @DisplayName("Deve validar readBytes")
    void UT_readBytes() throws IOException {
        var file = File.createTempFile("tmp", UUID.randomUUID().toString());
        try (FileWriter writer = new FileWriter(file)) {
            writer.write("test");
            writer.flush();
        } catch (IOException e) {
            throw new FrameworkException(e.getMessage());
        }

        Assertions.assertDoesNotThrow(() ->
                HashUtil.readBytes(null)
        );
        File finalFile = file;
        Assertions.assertDoesNotThrow(() -> HashUtil.readBytes(new FileInputStream(finalFile)));
        Assertions.assertDoesNotThrow(() -> HashUtil.readBytes(Mockito.mock(FileInputStream.class)));
        Assertions.assertEquals(HashUtil.readBytes(null), "");
        Assertions.assertEquals(HashUtil.readBytes(new FileInputStream(finalFile)), "test");
        Assertions.assertNotNull(HashUtil.readBytes(new FileInputStream(finalFile)));
        Assertions.assertNotNull(HashUtil.readBytes(null));
        Assertions.assertEquals(HashUtil.readBytes(null), "");
    }

    @Test
    @DisplayName("Deve validar toUUID")
    void UT_toUuid() {
        var list= Map.of(
                "38b3eff8baf56627478ec76a704e9b52","38b3eff8-baf5-6627-478e-c76a704e9b52",
                "008a6f9464ef81308da1dcc6ed9106d9","008a6f94-64ef-8130-8da1-dcc6ed9106d9",
                "c4ca4238a0b923820dcc509a6f75849b","c4ca4238-a0b9-2382-0dcc-509a6f75849b"
        );
        for (Map.Entry<String, String> entry : list.entrySet()) {
            var bytesIn = entry.getKey();
            var bytesOut = entry.getValue();
            UUID uuidOut = UUID.fromString(bytesOut);
            Assertions.assertEquals(uuidOut.toString(),bytesOut);
            Assertions.assertTrue(HashUtil.isUuid(bytesIn));
            Assertions.assertTrue(HashUtil.isUuid(bytesOut));
            Assertions.assertEquals(HashUtil.toUuid(bytesIn), uuidOut);
            Assertions.assertEquals(HashUtil.toUuid(bytesOut), uuidOut);
        }
    }

    @Test
    @DisplayName("Deve validar formatStringToMd5")
    void deveValidarFormatStringToMd5() {
        String bytesIn = "1";
        String bytesOut = "c4ca4238a0b923820dcc509a6f75849b";
        Assertions.assertTrue(HashUtil.formatStringToMd5((String)null).isEmpty());
        Assertions.assertTrue(HashUtil.formatStringToMd5("").isEmpty());
        Assertions.assertTrue(HashUtil.formatStringToMd5(" ").isEmpty());
        Assertions.assertTrue(HashUtil.formatStringToMd5((StringBuilder) null).isEmpty());
        Assertions.assertNotNull(HashUtil.formatStringToMd5(bytesOut));
        Assertions.assertFalse(HashUtil.formatStringToMd5(new StringBuilder(bytesOut)).isEmpty());
        Assertions.assertThrows(ParserException.class, () -> HashUtil.formatStringToMd5(bytesIn));
    }


    @Test
    @DisplayName("deve validar HashUtil.createMessageDigest")
    void UT_createMessageDigest() {
        Assertions.assertThrows(ParserException.class, ()-> HashUtil.createMessageDigest("TEST"));
        Assertions.assertNotNull(HashUtil.createMessageDigest((HashUtil.MD_5_STRATEGY)));
        Assertions.assertNotNull(HashUtil.createMessageDigest((HashUtil.SHA_256_STRATEGY)));
    }

    @Test
    @DisplayName("Deve validar isUuid")
    void deveValidarisUuid() {
        String bytesOut = "c4ca4238a0b923820dcc509a6f75849b";
        Assertions.assertFalse(HashUtil.isUuid(null));
        Assertions.assertFalse(HashUtil.isUuid(""));
        Assertions.assertFalse(HashUtil.isUuid(" "));
        Assertions.assertTrue(HashUtil.isUuid(bytesOut));
        Assertions.assertTrue(HashUtil.isUuid(UUID.randomUUID().toString()));
    }

    @Test
    @DisplayName("Deve validar toUuid")
    void deveValidar_toUuid() {
        var uuid=UUID.randomUUID();
        String uuidBytes = uuid.toString()
                .replace("-","")
                .replace("{","")
                .replace("}","");
        Assertions.assertFalse(uuid.equals(HashUtil.toUuid(UUID.randomUUID().toString())));
        Assertions.assertTrue(uuid.equals(HashUtil.toUuid(uuid.toString())));
        Assertions.assertTrue(uuid.equals(HashUtil.toUuid(uuidBytes)));
        Assertions.assertNull(HashUtil.toUuid((String)null));
        Assertions.assertNull(HashUtil.toUuid(""));
        Assertions.assertNull(HashUtil.toUuid(" "));
    }

    @Test
    @DisplayName("Deve validar toMd5")
    void UT_toMd5() {
        var uuid=UUID.randomUUID();
        String uuidBytes = uuid.toString()
                .replace("-","")
                .replace("{","")
                .replace("}","");
        Assertions.assertNull(HashUtil.toMd5(null, null));
        Assertions.assertNull(HashUtil.toMd5("%s", new String[0]));
        Assertions.assertNull(HashUtil.toMd5("%s", null));
        Assertions.assertEquals(HashUtil.toMd5("%s", uuidBytes), uuidBytes);
        Assertions.assertEquals(HashUtil.toMd5("%s", uuidBytes), uuidBytes);
        Assertions.assertEquals(HashUtil.toMd5(uuidBytes), uuidBytes);
        Assertions.assertEquals(HashUtil.toMd5(uuid.toString()), uuidBytes);
        Assertions.assertTrue(HashUtil.toMd5((String)null).isEmpty());
        Assertions.assertTrue(HashUtil.toMd5("").isEmpty());
        Assertions.assertFalse(HashUtil.toMd5(" ").isEmpty());
    }

    @Test
    @DisplayName("Deve validar toMd5Uuid")
    void UT_toMd5Uuid() {
        String bytesIn = "1";
        String bytesOut = "c4ca4238a0b923820dcc509a6f75849b";
        UUID uuidOut = UUID.fromString("c4ca4238-a0b9-2382-0dcc-509a6f75849b");

        Assertions.assertNull(HashUtil.toMd5Uuid("%s", null));
        Assertions.assertNull(HashUtil.toMd5Uuid("%s", new String[0]));
        Assertions.assertNull(HashUtil.toMd5Uuid(null, null));
        Assertions.assertNull(HashUtil.toMd5Uuid(null, new String[0]));
        Assertions.assertEquals(HashUtil.toMd5Uuid("%s", bytesIn),uuidOut);

        Assertions.assertNull(HashUtil.toMd5Uuid((Object) null));
        Assertions.assertNull(HashUtil.toMd5Uuid((String) null));
        Assertions.assertNull(HashUtil.toMd5Uuid((InputStream) null));

        Assertions.assertNull(HashUtil.toMd5Uuid(new Object()));
        Assertions.assertEquals(HashUtil.toMd5Uuid(bytesIn),uuidOut);

        Assertions.assertEquals(HashUtil.toMd5Uuid(bytesIn), uuidOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid("%s", bytesIn), uuidOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid(bytesOut), uuidOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid("%s", bytesOut), uuidOut);

        var md5Object = ObjectCheck.builder().id(uuidOut).build();
        if (md5Object != null) {
            var md5ObjectHash = UUID.fromString("79900378-e666-9c39-0c62-4245590bee0c");
            var md5ObjectMd5 = md5ObjectHash.toString().replace("-", "");
            var md5ObjectJson = String.format("{\"id\":\"%s\"}", md5Object.getId());

            Assertions.assertEquals(HashUtil.toMd5Uuid(md5Object), md5ObjectHash);
            Assertions.assertEquals(HashUtil.toMd5(md5Object), md5ObjectMd5);

            try {
                File file = File.createTempFile("tmp", UUID.randomUUID().toString());
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(md5ObjectJson);
                    writer.flush();
                } catch (IOException e) {
                    throw new FrameworkException(e.getMessage());
                }
                Assertions.assertEquals(HashUtil.toMd5(new FileInputStream(file)), md5ObjectMd5);
                Assertions.assertEquals(HashUtil.toMd5Uuid(new FileInputStream(file)), md5ObjectHash);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Test
    @DisplayName("Deve validar toSha256")
    void UT_toSha256() throws IOException {
        String bytesIn = "1";
        String bytesOut = "k��s�4��k�N�Z?WG���/\u001DI�\u001ERݷ�[K";
        String bytesOutHex = "6befbfbdefbfbd73efbfbd34efbfbdefbfbd6befbfbd4eefbfbd5a3f5747efbfbdefbfbdefbfbd2f1d49efbfbd1e52ddb7efbfbd5b4b";
//        UUID bytesOutUuid = UUID.fromString("6befbfbd-efbf-bd73-efbf-bd34efbfbdef");

//        Assertions.assertFalse(HashUtil.isUuid(bytesIn));
//        Assertions.assertTrue(HashUtil.isUuid(bytesOut));
//        Assertions.assertNotNull(HashUtil.formatStringToMd5(bytesOut));
//        Assertions.assertThrows(ParserException.class, () -> HashUtil.formatStringToMd5(bytesIn));
//        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5((String) null));
//        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5((StringBuilder) null));

        Assertions.assertTrue(HashUtil.toSha256((Object) null).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256((String) null).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256((InputStream) null).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256((Object)null).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256(new Object()).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256("").isEmpty());
        Assertions.assertFalse(HashUtil.toSha256(" ").isEmpty());
        Assertions.assertFalse(HashUtil.toSha256(bytesIn).isEmpty());
        Assertions.assertFalse(HashUtil.toSha256(10L).isEmpty());
        Assertions.assertEquals(HashUtil.toSha256(bytesIn), bytesOut);

        Assertions.assertTrue(HashUtil.toSha256(null, null).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256("%s", new String[0]).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256("%s", null).isEmpty());
        Assertions.assertEquals(HashUtil.toSha256("%s", bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toSha256(bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toSha256("%s".formatted(bytesIn)), bytesOut);

        UUID bytesOutUuid = UUID.fromString("6befbfbd-efbf-bd73-efbf-bd34efbfbdef");
        var hashObject = ObjectCheck.builder().id(bytesOutUuid).build();
        if (hashObject != null) {
            var hashObjectSha256="J��\u0002�\u000E��qR����s1��uSp���m��� ��";


            var hashObjectJson = ObjectUtil.toString(hashObject);

            Assertions.assertEquals(HashUtil.toSha256(hashObject), hashObjectSha256);

            try {
                File file = File.createTempFile("tmp", UUID.randomUUID().toString());
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(hashObjectJson);
                    writer.flush();
                } catch (IOException e) {
                    throw new FrameworkException(e.getMessage());
                }

                Assertions.assertEquals(HashUtil.toSha256(file), hashObjectSha256);
                Assertions.assertEquals(HashUtil.toSha256(file.toPath()), hashObjectSha256);
                Assertions.assertEquals(HashUtil.toSha256(new FileInputStream(file)), hashObjectSha256);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }

    @Test
    @DisplayName("Deve validar toSha256Uuid")
    void UT_toSha256Uuid() throws IOException {
        String bytesIn = "1";


        UUID bytesOutUuid = UUID.fromString("6befbfbd-efbf-bd73-efbf-bd34efbfbdef");

//        Assertions.assertFalse(HashUtil.isUuid(bytesIn));
//        Assertions.assertTrue(HashUtil.isUuid(bytesOut));
//        Assertions.assertNotNull(HashUtil.formatStringToMd5(bytesOut));
//        Assertions.assertThrows(ParserException.class, () -> HashUtil.formatStringToMd5(bytesIn));
//        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5((String) null));
//        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5((StringBuilder) null));

        Assertions.assertNull(HashUtil.toSha256Uuid((Object) null));
        Assertions.assertNull(HashUtil.toSha256Uuid((String) null));
        Assertions.assertNull(HashUtil.toSha256Uuid(""));
        Assertions.assertNotNull(HashUtil.toSha256Uuid(" "));
        Assertions.assertNull(HashUtil.toSha256Uuid((InputStream) null));
        Assertions.assertNull(HashUtil.toSha256Uuid(new Object()));
        Assertions.assertEquals(HashUtil.toSha256Uuid(bytesIn), bytesOutUuid);
        Assertions.assertEquals(HashUtil.toSha256Uuid(bytesIn), bytesOutUuid);
        Assertions.assertEquals(HashUtil.toSha256Uuid("%s".formatted(bytesIn)), bytesOutUuid);

        var hashObject = ObjectCheck.builder().id(bytesOutUuid).build();
        if (hashObject != null) {
            var hashObjectHash = UUID.fromString("4aefbfbd-efbf-bd02-efbf-bd0eefbfbdef");
            var hashObjectJson = ObjectUtil.toString(hashObject);

            Assertions.assertEquals(HashUtil.toSha256Uuid(hashObject), hashObjectHash);

            try {
                File file = File.createTempFile("tmp", UUID.randomUUID().toString());
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(hashObjectJson);
                    writer.flush();
                } catch (IOException e) {
                    throw new FrameworkException(e.getMessage());
                }


                Assertions.assertEquals(HashUtil.toSha256Uuid(file), hashObjectHash);

                Assertions.assertEquals(HashUtil.toSha256Uuid(file.toPath()), hashObjectHash);

                Assertions.assertEquals(HashUtil.toSha256Uuid(new FileInputStream(file)), hashObjectHash);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }


    }


    @Test
    @DisplayName("Deve validar toSha256Hex")
    void UT_toSha256Hex()  {
        String bytesIn = "1";
        String bytesOut = "k��s�4��k�N�Z?WG���/\u001DI�\u001ERݷ�[K";
        String bytesOutHex = "6befbfbdefbfbd73efbfbd34efbfbdefbfbd6befbfbd4eefbfbd5a3f5747efbfbdefbfbdefbfbd2f1d49efbfbd1e52ddb7efbfbd5b4b";
        UUID bytesOutUuid = UUID.fromString("6befbfbd-efbf-bd73-efbf-bd34efbfbdef");

        Assertions.assertTrue(HashUtil.toSha256Hex((String)null).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256Hex((Object) null).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256Hex((String) null).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256Hex((InputStream) null).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256Hex(new Object()).isEmpty());
        Assertions.assertTrue(HashUtil.toSha256Hex("").isEmpty());
        Assertions.assertFalse(HashUtil.toSha256Hex(" ").isEmpty());

        Assertions.assertEquals(HashUtil.toSha256Hex(bytesIn), bytesOutHex);
        Assertions.assertEquals(HashUtil.toSha256Hex("%s".formatted(bytesIn)), bytesOutHex);

        var hashObject = ObjectCheck.builder().id(bytesOutUuid).build();
        if (hashObject != null) {
            var hashObjectSha256="J��\u0002�\u000E��qR����s1��uSp���m��� ��";
            var hashObjectHash = UUID.fromString("4aefbfbd-efbf-bd02-efbf-bd0eefbfbdef");
            var hashObjectSha256Hex = "4aefbfbdefbfbd02efbfbd0eefbfbdefbfbd7152efbfbdefbfbdefbfbdefbfbd7331efbfbdefbfbd755370efbfbdefbfbdefbfbd6defbfbdefbfbdefbfbd20efbfbdefbfbd";
            var hashObjectJson = ObjectUtil.toString(hashObject);

            Assertions.assertEquals(HashUtil.toSha256Uuid(hashObject), hashObjectHash);

            try {
                File file = File.createTempFile("tmp", UUID.randomUUID().toString());
                try (FileWriter writer = new FileWriter(file)) {
                    writer.write(hashObjectJson);
                    writer.flush();
                } catch (IOException e) {
                    throw new FrameworkException(e.getMessage());
                }


                Assertions.assertEquals(HashUtil.toSha256Hex(file), hashObjectSha256Hex);
                Assertions.assertEquals(HashUtil.toSha256Hex(file.toPath()), hashObjectSha256Hex);
                Assertions.assertEquals(HashUtil.toSha256Hex(new FileInputStream(file)), hashObjectSha256Hex);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }


    @Test
    @DisplayName("Deve validar toHEX")
    void UT_toHex() {
        String bytesIn = "stringToHex";
        String bytesOut = "737472696e67546f486578";

        Assertions.assertEquals(HashUtil.toHex(null), "");
        Assertions.assertEquals(HashUtil.toHex(""), "");
        Assertions.assertEquals(HashUtil.toHex(bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toHex("%s", bytesIn), bytesOut);
        Assertions.assertNotNull(HashUtil.fromHex(bytesOut), bytesIn);
        Assertions.assertNotNull(HashUtil.fromHex(""), "");
        Assertions.assertNotNull(HashUtil.fromHex(null), "");
    }

    @Test
    @DisplayName("Deve validar toBase64")
    void UT_toBase64() {
        String bytesIn = "stringToBase64";
        String bytesOut = "c3RyaW5nVG9CYXNlNjQ=";
        Assertions.assertEquals(HashUtil.toBase64(null), "");
        Assertions.assertEquals(HashUtil.toBase64(""), "");
        Assertions.assertEquals(HashUtil.toBase64(bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toBase64("%s", bytesIn), bytesOut);
        Assertions.assertNotNull(HashUtil.fromBase64(bytesOut), bytesIn);
        Assertions.assertNotNull(HashUtil.fromBase64(null), "");
        Assertions.assertNotNull(HashUtil.fromBase64(""), "");
    }

    @Data
    @Builder
    private static class ObjectCheck {
        private UUID id;
    }

}
