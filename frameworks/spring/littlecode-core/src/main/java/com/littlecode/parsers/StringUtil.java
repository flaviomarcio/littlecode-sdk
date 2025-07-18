package com.littlecode.parsers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StringUtil {
    private Object value;
    private String padChar;

    public StringUtil(Object value) {
        this.setValue(value);
        this.setPadChar(" ");
    }

    public StringUtil(Object value, Object padChar) {
        this.setValue(value);
        this.setPadChar(padChar);
    }

    public static StringUtil of(Object value) {
        return new StringUtil(value);
    }

    public static String toString(Object value) {
        return PrimitiveUtil.toString(value);
    }

    public static String toNumber(Object value) {
        return PrimitiveUtil.toString(value).replaceAll("[^0-9]", "");
    }

    public static String toAlpha(Object value) {
        return PrimitiveUtil.toString(value).replaceAll("[^a-zA-Z]", "");
    }

    public static String toAlphaNumber(Object value) {
        return PrimitiveUtil.toString(value).replaceAll("[^a-zA-Z0-9]", "");
    }

    @Deprecated(since = "rightPad is deprecated, use toLeftPad")
    public static String leftPad(int length, String padChar, Object input) {
        return toLeftPad(length, padChar, input);
    }

    public static String toLeftPad(int length, String padChar, Object input) {
        final var input_s = PrimitiveUtil.toString(input);
        final var padChar_s = PrimitiveUtil.toString(padChar);
        if (input_s.length() >= length) return input_s;
        int padding = length - input_s.length();
        return padChar_s.repeat(padding) + input;
    }

    @Deprecated(since = "rightPad is deprecated, use toRightPad")
    public static String rightPad(int length, String padChar, Object input) {
        return toRightPad(length, padChar, input);
    }

    public static String toRightPad(int length, String padChar, Object input) {
        final var input_s = PrimitiveUtil.toString(input);
        final var padChar_s = PrimitiveUtil.toString(padChar);
        if (input_s.length() >= length) return input_s;
        int padding = length - input_s.length();
        return input_s + padChar_s.repeat(padding);
    }

    @Deprecated(since = "rightPad is deprecated, use toCenterPad")
    public static String centerPad(int length, String padChar, Object input) {
        return toCenterPad(length, padChar, input);
    }

    public static String toCenterPad(int length, String padChar, Object input) {
        final var input_s = PrimitiveUtil.toString(input);
        final var padChar_s = PrimitiveUtil.toString(padChar);
        if (input_s.length() >= length) return input_s;
        int totalPadding = length - input_s.length();
        int leftPadding = totalPadding / 2;
        int rightPadding = totalPadding - leftPadding;
        return padChar_s.repeat(leftPadding) + input_s + padChar_s.repeat(rightPadding);
    }

    public void setPadChar(Object value) {
        this.padChar = PrimitiveUtil.toString(value);
    }

    public StringUtil padChar(String padChar) {
        this.setPadChar(padChar);
        return this;
    }

    public String toString() {
        return PrimitiveUtil.toString(this.value);
    }

    public String asString() {
        return PrimitiveUtil.toString(this.value);
    }

    public String asNumber() {
        return toNumber(this.value);
    }

    public String asAlpha() {
        return toAlpha(this.value);
    }

    public String asAlphaNumber() {
        return toAlphaNumber(this.value);
    }

    public String asLeftPad(int length) {
        return toLeftPad(length, this.padChar, this.value);
    }

    public String asRightPad(int length) {
        return toRightPad(length, this.padChar, this.value);
    }

    public String asCenterPad(int length) {
        return toCenterPad(length, this.padChar, this.value);
    }

}
