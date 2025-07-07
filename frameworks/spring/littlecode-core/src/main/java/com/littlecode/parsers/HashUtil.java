package com.littlecode.parsers;

import com.littlecode.exceptions.ParserException;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;
import java.util.regex.Pattern;

public class HashUtil {
    public static final String MD_5_STRATEGY = "MD5";
    private static final Pattern UUID_REGEX = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    private HashUtil(){
    }

    public static MessageDigest createMessageDigest(String strategy) {
        try {
            return MessageDigest.getInstance(strategy);
        } catch (Exception ignored) {
            return null;
        }
    }

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

    public static String readBytes(InputStream value) {
        if (value != null) {
            try {
                return new String(value.readAllBytes());
            } catch (Exception ignored) {
            }
        }
        return "";
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
        if (value != null && !value.trim().isEmpty()) {
            try {
                return UUID.fromString(formatStringToMd5(value));
            } catch (Exception ignored) {
            }
        }
        return null;
    }

    public static UUID toMd5Uuid(InputStream value) {
        return toMd5Uuid(readBytes(value));
    }

    public static UUID toMd5Uuid(String format, Object... args) {
        if (format != null && args != null && args.length > 0) {
            var bytes = String.format(format, args);
            return toMd5Uuid(bytes);
        }
        return null;
    }

    public static UUID toMd5Uuid(Object o) {
        if (o != null) {
            var value = ObjectUtil.toString(o);
            if (value.isEmpty())
                return null;
            var md5Uuid = toUuid(value);
            if (md5Uuid != null)
                return md5Uuid;

            var md = createMessageDigest(MD_5_STRATEGY);
            if (md == null)
                return null;

            var messageDigest = md.digest(value.getBytes());
            var hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xFF & b);
                if (hex.length() == 1)
                    hexString.append("0");
                hexString.append(hex);
            }
            return toUuid(hexString.toString());
        }
        return null;
    }


    public static String toMd5(String format, Object... args) {
        return toMd5(String.format(format, args));
    }

    public static String toMd5(Object o) {
        return toMd5(ObjectUtil.toString(o));
    }

    public static String toMd5(InputStream value){
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

    public static String fromBase64(String value) {
        if (value == null || value.isEmpty())
            return "";
        return Arrays.toString(Base64.getDecoder().decode(value));
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
        if (hexEncoded == null || hexEncoded.isEmpty())
            return "";
        byte[] decodedBytes = new byte[hexEncoded.length() / 2];
        for (int i = 0; i < decodedBytes.length; i++)
            decodedBytes[i] = (byte) Integer.parseInt(hexEncoded.substring(i * 2, i * 2 + 2), 16);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

}
