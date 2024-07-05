package com.littlecode.tests;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.exceptions.ParserException;
import com.littlecode.parsers.HashUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class HashUtilTest {

    @Test
    @DisplayName("Deve validar readBytes")
    public void UT_readBytes() throws IOException {
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
    public void UT_toUuid() {
        String bytesOut = "c4ca4238a0b923820dcc509a6f75849b";
        UUID uuidOut = UUID.fromString("c4ca4238-a0b9-2382-0dcc-509a6f75849b");
        Assertions.assertTrue(HashUtil.isUuid(bytesOut));
        Assertions.assertEquals(HashUtil.toUuid(bytesOut), uuidOut);

    }

    @Test
    @DisplayName("Deve validar toMD5")
    public void UT_toMd5() throws IOException {
        String bytesIn = "1";
        String bytesOut = "c4ca4238a0b923820dcc509a6f75849b";
        UUID uuidOut = UUID.fromString("c4ca4238-a0b9-2382-0dcc-509a6f75849b");

        Assertions.assertFalse(HashUtil.isUuid(bytesIn));
        Assertions.assertTrue(HashUtil.isUuid(bytesOut));
        Assertions.assertNotNull(HashUtil.formatStringToMd5(bytesOut));
        Assertions.assertThrows(ParserException.class, () -> HashUtil.formatStringToMd5(bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.createMessageDigest("TEST"));
        Assertions.assertDoesNotThrow(() -> HashUtil.createMessageDigest("MD5"));
        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5((String) null));
        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5((StringBuilder) null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid("%s", null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(null, null));

        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5(bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5((Object) null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5((String) null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5((InputStream) null));

        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid((Object) null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid((String) null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid((InputStream) null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(new Object()));

        Assertions.assertEquals(HashUtil.toMd5(bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toMd5("%s", bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid(bytesIn), uuidOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid("%s", bytesIn), uuidOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid("%s", null), null);
        Assertions.assertEquals(HashUtil.toMd5Uuid(null, null), null);
        Assertions.assertEquals(HashUtil.toMd5Uuid(bytesOut), uuidOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid("%s", bytesOut), uuidOut);
        Assertions.assertNull(HashUtil.toMd5Uuid((Object) null));
        Assertions.assertNull(HashUtil.toMd5Uuid((String) null));
        Assertions.assertNull(HashUtil.toMd5Uuid((InputStream) null));

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
    @DisplayName("Deve validar toHEX")
    public void UT_toHex() {
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
    public void UT_toBase64() {
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
