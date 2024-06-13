package com.littlecode.web.core.helper;

public class MsgHelper {
    public static final String OBJECT_INVALID = "Invalid %s object";
    public static final String PATH_ERROR_ON_CREATE = "Error on create path %s";
    public static final String PATH_ERROR_NOT_FOUND = "Error path not found %s";
    public static final String KEYS_LOADING = "loading keys from file: %s, %s";
    public static final String INVALID_CREDENTIAL = "Invalid credential";
    public static final String TOKEN_EXPIRED = "Expired token";
    public static final String TOKEN_INVALID = "Invalid token";
    public static final String INVALID_DATA = "Invalid data %s";
    public static final String INVALID_INFORMATION = "Invalid information";
    public static final String USER_IS_NOT_IN_THIS_SCOPE = "User is not in this scope";
    public static final String USER_INVALID_OBJECT = "Invalid user object";
    public static final String INVALID_ATTRIBUTE = "Invalid attribute[%s]";
    public static final String USER_EXISTS = "The user already exists: %s";
    public static final String USER_NO_EXISTS = "User no exists: %s";
    public static final String USER_YOUR_NEW_PASSWORD = "Sua nova senha: %s";


    public static String o(String format, Class<?> c) {
        return o(format, c.getName());
    }

    public static String o(String format, Object... args) {
        return String.format(format, args);
    }
}
