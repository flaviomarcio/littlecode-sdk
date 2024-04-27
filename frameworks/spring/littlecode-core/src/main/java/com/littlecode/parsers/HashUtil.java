package com.littlecode.parsers;

import com.littlecode.exceptions.ParserException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;

public class HashUtil {
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");

    public static String formatStringToMd5(StringBuilder value) {
        return value == null ? "" : formatStringToMd5(value.toString());
    }

    public static String formatStringToMd5(String valueIn) {
        if (valueIn != null && !valueIn.trim().isEmpty()) {
            var outValue = valueIn
                    .trim()
                    .replace("-", "")
                    .replace("{", "")
                    .replace("}", "");
            if (outValue.length() != 32)
                throw new ParserException(String.format("Invalid length %s", valueIn));

            return outValue.substring(0, 8) + "-" +
                    outValue.substring(8, 12) + "-" +
                    outValue.substring(12, 16) + "-" +
                    outValue.substring(16, 20) + "-" +
                    outValue.substring(20);
        }
        return "";
    }

    public static String readBytes(InputStream value) throws IOException {
        return value == null
                ? ""
                : new String(value.readAllBytes());
    }

    public static boolean isUuid(String value) {
        if (value != null && !value.trim().isEmpty()) {
            try {
                return UUID_REGEX.matcher(formatStringToMd5(value)).matches();
            } catch (Exception ignored) {
            }
        }
        return false;
    }

    public static UUID toUuid(String value) {
        try {
            return UUID.fromString(formatStringToMd5(value));
        } catch (Exception ignored) {
        }
        return null;
    }

    public static UUID toMd5Uuid(InputStream value) throws IOException {
        return toMd5Uuid(readBytes(value));
    }

    public static UUID toMd5Uuid(Object o) {
        return (o == null)
                ? null
                : toMd5Uuid(ObjectUtil.toString(o));
    }

    public static UUID toMd5Uuid(String format, Object... args) {
        return toMd5Uuid(String.format(format, args));
    }

    public static UUID toMd5Uuid(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return UUID.fromString(formatStringToMd5(value));
            } catch (Exception ignored) {
            }
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(value.getBytes());

                StringBuilder hexString = new StringBuilder();
                for (byte b : messageDigest) {
                    String hex = Integer.toHexString(0xFF & b);
                    if (hex.length() == 1)
                        hexString.append('0');

                    hexString.append(hex);
                }
                return UUID.fromString(formatStringToMd5(hexString));
            } catch (Exception ignored) {
            }
        }
        return null;
    }


    public static String toMd5(String format, Object... args) {
        return toMd5(String.format(format, args));
    }

    public static String toMd5(Object o) {
        return toMd5(ObjectUtil.toString(o));
    }

    public static String toMd5(InputStream value) throws IOException {
        return toMd5(readBytes(value));
    }

    public static String toMd5(String value) {
        var uuid = toMd5Uuid(value);
        return uuid == null
                ? ""
                : uuid.toString()
                .replace("{", "")
                .replace("}", "")
                .replace("-", "");
    }

    public static String toBase64(String format, Object... args) {
        return toBase64(String.format(format, args));
    }

    public static String toBase64(String bytes) {
        if (bytes == null || bytes.isEmpty())
            return "";
        return Base64.getEncoder().encodeToString(bytes.getBytes());
    }

    public static String fromBase64(String bytes) {
        if (bytes == null || bytes.isEmpty())
            return "";
        return Arrays.toString(Base64.getDecoder().decode(bytes));
    }

    public static String toHex(String format, Object... args) {
        return toHex(String.format(format, args));
    }

    public static String toHex(String value) {
        if (value == null || value.isEmpty())
            return "";
        StringBuilder hexStringBuilder = new StringBuilder();
        try (Formatter formatter = new Formatter(hexStringBuilder, Locale.US)) {
            for (byte b : value.getBytes())
                formatter.format("%02x", b);
            return hexStringBuilder.toString();
        }
    }

    public static String fromHex(String hexEncoded) {
        byte[] decodedBytes = new byte[hexEncoded.length() / 2];
        for (int i = 0; i < decodedBytes.length; i++)
            decodedBytes[i] = (byte) Integer.parseInt(hexEncoded.substring(i * 2, i * 2 + 2), 16);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

}
