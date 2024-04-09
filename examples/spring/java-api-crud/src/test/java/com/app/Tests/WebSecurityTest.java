package com.app.Tests;

import com.app.common.application.security.WebUrlHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WebSecurityTest {

    @Test
    public void UT_000_CHECK_WebUrlHelper() {
        WebUrlHelper.setContextPath("/api");
        Assertions.assertNotNull(WebUrlHelper.getContextPath());
        Assertions.assertFalse(WebUrlHelper.isTrustedOpenUrl("/swagger-ui.html"));
        Assertions.assertFalse(WebUrlHelper.isTrustedOpenUrl("/swagger-ui/index.html"));
        Assertions.assertTrue(WebUrlHelper.isTrustedOpenUrl("/api/swagger-ui.html"));
        Assertions.assertTrue(WebUrlHelper.isTrustedOpenUrl("/api/swagger-ui/index.html"));
        Assertions.assertFalse(WebUrlHelper.isTrustedOpenUrl("/test"));
        Assertions.assertFalse(WebUrlHelper.isTrustedOpenUrl("/test.index"));
        Assertions.assertFalse(WebUrlHelper.isTrustedUrl("/teste"));
        Assertions.assertNotNull(WebUrlHelper.getTrustedOpenUrl());
    }

}
