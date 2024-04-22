package com.littlecode.tests;

import com.littlecode.parsers.HashUtil;
import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class HashUtilTest {
    @Test
    public void UT_toUuid() {
        String bytesOut = "c4ca4238a0b923820dcc509a6f75849b";
        UUID uuidOut = UUID.fromString("c4ca4238-a0b9-2382-0dcc-509a6f75849b");

        Assertions.assertDoesNotThrow(() -> HashUtil.isUuid(bytesOut));
        Assertions.assertDoesNotThrow(() -> HashUtil.isUuid(null));
        Assertions.assertDoesNotThrow(() -> HashUtil.isUuid(""));
        Assertions.assertDoesNotThrow(() -> HashUtil.isUuid("1"));
        Assertions.assertTrue(HashUtil.isUuid(bytesOut));
        Assertions.assertFalse(HashUtil.isUuid(null));
        Assertions.assertFalse(HashUtil.isUuid(""));
        Assertions.assertFalse(HashUtil.isUuid("1"));

        Assertions.assertDoesNotThrow(() -> HashUtil.toUuid(bytesOut));
        Assertions.assertDoesNotThrow(() -> HashUtil.toUuid(null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toUuid(""));
        Assertions.assertDoesNotThrow(() -> HashUtil.toUuid("1"));
        Assertions.assertNotNull(HashUtil.toUuid(bytesOut));
        Assertions.assertEquals(HashUtil.toUuid(bytesOut), uuidOut);
        Assertions.assertNull(HashUtil.toUuid(null));
        Assertions.assertNull(HashUtil.toUuid(""));
        Assertions.assertNull(HashUtil.toUuid("1"));

    }

    @Test
    public void UT_toMd5() throws IOException {
        String bytesIn = "1";
        String bytesOut = "c4ca4238a0b923820dcc509a6f75849b";
        UUID uuidOut = UUID.fromString("c4ca4238-a0b9-2382-0dcc-509a6f75849b");

        Assertions.assertDoesNotThrow(() -> HashUtil.isUuid(bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.isUuid(bytesOut));
        Assertions.assertFalse(HashUtil.isUuid(bytesIn));
        Assertions.assertTrue(HashUtil.isUuid(bytesOut));

        Assertions.assertNotNull(HashUtil.formatStringToMd5(bytesOut));
        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5(bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5("").isEmpty());
        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5((String) null).isEmpty());
        Assertions.assertDoesNotThrow(() -> HashUtil.formatStringToMd5((StringBuilder) null).isEmpty());
        Assertions.assertTrue(HashUtil.formatStringToMd5(bytesIn).isEmpty());
        Assertions.assertTrue(HashUtil.formatStringToMd5("").isEmpty());
        Assertions.assertTrue(HashUtil.formatStringToMd5((String) null).isEmpty());
        Assertions.assertTrue(HashUtil.formatStringToMd5((StringBuilder) null).isEmpty());

        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5((String) null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5(""));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5(bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5("%s", bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5(null, bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5(null, null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5("%s", null));
        Assertions.assertEquals(HashUtil.toMd5((String) null), "");
        Assertions.assertEquals(HashUtil.toMd5(""), "");
        Assertions.assertEquals(HashUtil.toMd5(bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toMd5("%s", bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toMd5(null, bytesIn), "");
        Assertions.assertEquals(HashUtil.toMd5(null, null), "");
        Assertions.assertEquals(HashUtil.toMd5("%s", null), "");

        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid((String) null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(""));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid("%s", bytesIn));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(bytesOut));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid("%s", bytesOut));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(null, bytesOut));
        Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(null, null));
        Assertions.assertNull(HashUtil.toMd5Uuid((String) null));
        Assertions.assertNull(HashUtil.toMd5Uuid(""));
        Assertions.assertEquals(HashUtil.toMd5Uuid(bytesIn), uuidOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid("%s", bytesIn), uuidOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid(bytesOut), uuidOut);
        Assertions.assertEquals(HashUtil.toMd5Uuid("%s", bytesOut), uuidOut);
        Assertions.assertNull(HashUtil.toMd5Uuid(null, bytesOut));
        Assertions.assertNull(HashUtil.toMd5Uuid(null, null));

        var md5Object = ObjectCheck.builder().id(uuidOut).build();
        if (md5Object != null) {
            var md5ObjectHash = UUID.fromString("79900378-e666-9c39-0c62-4245590bee0c");
            var md5ObjectMd5 = md5ObjectHash.toString().replace("-", "");
            var md5ObjectJson = String.format("{\"id\":\"%s\"}", md5Object.getId());

            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid((String) null));
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid((UUID) null));
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(""));
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(md5Object));
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5(md5Object));

            Assertions.assertNull(HashUtil.toMd5Uuid((String) null));
            Assertions.assertNull(HashUtil.toMd5Uuid(""));
            Assertions.assertEquals(HashUtil.toMd5Uuid(md5Object), md5ObjectHash);
            Assertions.assertEquals(HashUtil.toMd5(md5Object), md5ObjectMd5);
            Assertions.assertEquals(HashUtil.toMd5((Object) null), "");

            File file = File.createTempFile("tmp", UUID.randomUUID().toString());
            FileWriter writer = new FileWriter(file);
            writer.write(md5ObjectJson);
            writer.flush();
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5((InputStream) null));
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5((FileInputStream) null));
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5(new FileInputStream(file)));
            Assertions.assertEquals(HashUtil.toMd5((InputStream) null), "");
            Assertions.assertEquals(HashUtil.toMd5((String) null), "");
            Assertions.assertEquals(HashUtil.toMd5((Object) null), "");
            Assertions.assertEquals(HashUtil.toMd5(new FileInputStream(file)), md5ObjectMd5);

            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid(new FileInputStream(file)));
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid((FileInputStream) null));
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid((String) null));
            Assertions.assertDoesNotThrow(() -> HashUtil.toMd5Uuid((Object) null));
            Assertions.assertNull(HashUtil.toMd5Uuid((InputStream) null));
            Assertions.assertNull(HashUtil.toMd5Uuid((FileInputStream) null));
            Assertions.assertNull(HashUtil.toMd5Uuid((Object) null));
            Assertions.assertNull(HashUtil.toMd5Uuid((String) null));
            Assertions.assertEquals(HashUtil.toMd5Uuid(new FileInputStream(file)), md5ObjectHash);

            Assertions.assertNull(HashUtil.toMd5Uuid((FileInputStream) null));
        }


    }

    @Test
    public void UT_toHex() {
        final String bytesIn = "stringToHex";
        final String bytesOut = "737472696e67546f486578";
        final String hexValid = "1A2B3C";

        Assertions.assertDoesNotThrow(() -> HashUtil.isHex(hexValid));
        Assertions.assertDoesNotThrow(() -> HashUtil.isHex(""));
        Assertions.assertDoesNotThrow(() -> HashUtil.isHex(null));
        Assertions.assertDoesNotThrow(() -> HashUtil.toHex("%s", bytesIn), bytesOut);
        Assertions.assertDoesNotThrow(() -> HashUtil.fromHex(bytesOut), bytesIn);

        Assertions.assertTrue(HashUtil.isHex(hexValid));
        Assertions.assertFalse(HashUtil.isHex(null));
        Assertions.assertFalse(HashUtil.isHex(""));
        Assertions.assertEquals(HashUtil.toHex(bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toHex("%s", bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toHex(null, bytesIn), "");
        Assertions.assertEquals(HashUtil.toHex(null, null), "");
        Assertions.assertEquals(HashUtil.toHex("%s", null), "");
        Assertions.assertNotNull(HashUtil.fromHex(bytesOut), bytesIn);
    }

    @Test
    public void UT_toBase64() {
        String bytesIn = "stringToBase64";
        String bytesOut = "c3RyaW5nVG9CYXNlNjQ=";

        Assertions.assertDoesNotThrow(() -> HashUtil.toBase64(null), bytesOut);
        Assertions.assertDoesNotThrow(() -> HashUtil.toBase64(bytesIn), bytesOut);
        Assertions.assertDoesNotThrow(() -> HashUtil.toBase64("%s", bytesIn), bytesOut);
        Assertions.assertDoesNotThrow(() -> HashUtil.toBase64("%s", null), bytesOut);
        Assertions.assertDoesNotThrow(() -> HashUtil.toBase64(null, null), bytesOut);
        Assertions.assertDoesNotThrow(() -> HashUtil.toBase64(null, bytesIn), bytesOut);
        Assertions.assertDoesNotThrow(() -> HashUtil.fromBase64(bytesOut), bytesIn);
        Assertions.assertDoesNotThrow(() -> HashUtil.fromBase64(null), bytesIn);

        Assertions.assertEquals(HashUtil.toBase64(null), "");
        Assertions.assertEquals(HashUtil.toBase64(bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toBase64("%s", bytesIn), bytesOut);
        Assertions.assertEquals(HashUtil.toBase64("%s", null), "");
        Assertions.assertEquals(HashUtil.toBase64(null, null), "");
        Assertions.assertEquals(HashUtil.toBase64(null, bytesIn), "");
        Assertions.assertEquals(HashUtil.toBase64(null), "");
        Assertions.assertNotNull(HashUtil.fromBase64(bytesOut), bytesIn);

        Assertions.assertEquals(HashUtil.toHex(null), "");
        Assertions.assertNotNull(HashUtil.toHex(bytesIn));
        Assertions.assertNotNull(HashUtil.toHex("%s", bytesIn));
        Assertions.assertEquals(HashUtil.toHex("%s", null), "");
        Assertions.assertEquals(HashUtil.toHex(null, null), "");
        Assertions.assertEquals(HashUtil.toHex(null, bytesIn), "");
        Assertions.assertEquals(HashUtil.toHex(null), "");
        Assertions.assertNotNull(HashUtil.fromHex(bytesOut));
    }

    @Data
    @Builder
    private static class ObjectCheck {
        private UUID id;
    }

}
