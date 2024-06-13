package com.littlecode.web.core;

import com.littlecode.parsers.ObjectUtil;
import com.littlecode.parsers.ObjectValueUtil;
import com.littlecode.parsers.PrimitiveUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RequestContextHolder {

    private static final String CONST_PREFIX = "principal.%s";
    private static final String CONST_SCOPE_ID = "scopeId";
    private static final String CONST_USER_ID = "userId";
    private static final String CONST_USER_NAME = "userName";
    private static final String CONST_REQUEST_PATH = "requestPath";
    private static final String CONST_REQUEST_TOKEN = "requestToken";
    private static final String CONST_REQUEST_CONTEXT = "requestContext";

    private static final ThreadLocal<Map<String, String>> requestContext = new ThreadLocal<>();

    private RequestContextHolder() {
        super();
    }

    private static synchronized Map<String, String> getContext() {
        Map<String, String> ctx = requestContext.get();
        if (ctx == null) {
            ctx = new HashMap<>();
            requestContext.set(ctx);
        }
        return ctx;
    }

    public static synchronized String getRequestPath() {
        return PrimitiveUtil.toString(getContext().get(String.format(CONST_PREFIX, CONST_REQUEST_PATH)));
    }

    public static synchronized void setRequestPath(String path) {
        getContext().put(String.format(CONST_PREFIX, CONST_REQUEST_PATH), PrimitiveUtil.toString(path));
    }

    public static synchronized String getRequestToken() {
        return PrimitiveUtil.toString(getContext().get(String.format(CONST_PREFIX, CONST_REQUEST_TOKEN)));
    }

    public static synchronized void setRequestToken(String token) {
        getContext().put(String.format(CONST_PREFIX, CONST_REQUEST_TOKEN), PrimitiveUtil.toString(token));
    }

    public static synchronized RequestContext getRequestContext() {
        var v=getContext().get(String.format(CONST_PREFIX, CONST_REQUEST_CONTEXT));
        return v==null || v.trim().isEmpty()
                ?null
                :ObjectUtil.createFromJSON(RequestContext.class, v);
    }

    public static synchronized void setConstRequestContext(final RequestContext o) {
        getContext().put(String.format(CONST_PREFIX, CONST_USER_ID), ObjectUtil.toString(o));
    }

    public static synchronized String getScopeId() {
        return PrimitiveUtil.toString(getContext().get(String.format(CONST_PREFIX, CONST_SCOPE_ID)));
    }

    public static synchronized void setScopeId(Object userId) {
        getContext().put(String.format(CONST_PREFIX, CONST_USER_ID), PrimitiveUtil.toString(userId));
    }

    public static synchronized String getUserId() {
        return PrimitiveUtil.toString(getContext().get(String.format(CONST_PREFIX, CONST_USER_ID)));
    }

    public static synchronized void setUserId(Object userId) {
        getContext().put(String.format(CONST_PREFIX, CONST_USER_ID), PrimitiveUtil.toString(userId));
    }

    public static synchronized String getUserName() {
        return PrimitiveUtil.toString(getContext().get(String.format(CONST_PREFIX, CONST_USER_NAME)));
    }

    public static synchronized void setUserName(String name) {
        getContext().put(String.format(CONST_PREFIX, CONST_USER_ID), PrimitiveUtil.toString(name));
    }

}
