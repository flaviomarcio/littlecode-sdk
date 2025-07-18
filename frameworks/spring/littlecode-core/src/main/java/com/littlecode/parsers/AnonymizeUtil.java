package com.littlecode.parsers;

public class AnonymizeUtil {
    private static final int PHONE_LENGTH = "012345678".length();
    private final String value;

    private AnonymizeUtil(String value) {
        this.value = value == null ? "" : value.trim();
    }

    public static AnonymizeUtil value(Object value) {
        return new AnonymizeUtil(PrimitiveUtil.toString(value));
    }

    public String asString() {
        if (value.trim().isEmpty() || value.length() <= 3)
            return "";
        return value.substring(0, getStartIndex(value)) + "****" + value.substring(value.length() - 2);
    }

    public String asUserName() {
        if (value.trim().isEmpty())
            return "";
        if (value.length() <= 2) {
            return "*".repeat(value.length());
        }
        return value.substring(0, getStartIndex(value)) + "****" + value.substring(value.length() - 1);
    }

    public String asMail() {
        if (value.trim().isEmpty() || !value.contains("@"))
            return "";

        var email = value.substring(0, value.indexOf("@"));
        if (email.isEmpty())
            return "";
        var domain = value.substring(value.indexOf("@") + 1);
        return email.substring(0, getStartIndex(email)) + "****" + email.substring(email.length() - 1) + "@" + domain;

    }

    public String asPhoneNumber() {
        if (value.trim().isEmpty() || value.length() < PHONE_LENGTH)
            return "";
        var num = value.substring(value.length() - (PHONE_LENGTH));
        return num.substring(0, getStartIndex(num)) + "****" + num.substring(num.length() - 2);
    }

    public String asDocument() {
        if (value.trim().isEmpty() || value.length() <= 3)
            return "";
        return value.substring(0, getStartIndex(value)) + "****" + value.substring(value.length() - 2);
    }

    private int getStartIndex(String value) {
        if (value.length() > 6)
            return 4;
        if (value.length() > 5)
            return 3;
        if (value.length() > 4)
            return 2;
        return 1;
    }
}
