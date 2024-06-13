package com.littlecode.web.core.interfaces;

import com.littlecode.web.core.RequestContext;

@FunctionalInterface
public interface RequestAuthChecker {
    boolean check(RequestContext context);
}
