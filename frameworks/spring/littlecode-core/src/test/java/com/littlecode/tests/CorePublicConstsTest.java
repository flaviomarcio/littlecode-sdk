package com.littlecode.tests;

import com.littlecode.config.CorePublicConsts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;


@Slf4j
@ExtendWith(MockitoExtension.class)
public class CorePublicConstsTest {
    @Test
    public void UT_000_CHECK_CONSTRUCTOR() {
        Assertions.assertDoesNotThrow(CorePublicConsts::new);
        Assertions.assertNotNull(CorePublicConsts.MIN_LOCALDATE);
        Assertions.assertNotNull(CorePublicConsts.MAX_LOCALTIME);
        Assertions.assertNotNull(CorePublicConsts.MIN_LOCALTIME);
        Assertions.assertNotNull(CorePublicConsts.MIN_LOCALDATETIME);
        Assertions.assertNotNull(CorePublicConsts.MIN_LOCALDATETIME_MAX_TIME);
        Assertions.assertNotNull(CorePublicConsts.FORMAT_DATE);
        Assertions.assertNotNull(CorePublicConsts.FORMAT_DATE_TIME);
        Assertions.assertNotNull(CorePublicConsts.FORMAT_DATE_TIME_MS);
        Assertions.assertNotNull(CorePublicConsts.FORMAT_DATE_TIME_HH);
        Assertions.assertNotNull(CorePublicConsts.FORMAT_DATE_TIME_HH_MM);
        Assertions.assertNotNull(CorePublicConsts.FORMAT_TIME);
        Assertions.assertNotNull(CorePublicConsts.FORMAT_TIME_MS);
        Assertions.assertNotNull(CorePublicConsts.FORMAT_TIME_HH_MM);
    }
}
