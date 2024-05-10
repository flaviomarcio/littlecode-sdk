package com.littlecode.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.util.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SystemUtilTest {

    private static final String PROPERTY_A = "a-test-1.a-test-2.a-test-3";
    private static final String PROPERTY_A_VALUE = "a-test-0";
    private static final String PROPERTY_B = "b-test-1.b-test-2.b-test-3";
    private static final String PROPERTY_B_VALUE = "b-test-0";
    private static final String PROPERTY_C = "c-test-1.c-test-2.c-test-3";
    private static final String PROPERTY_C_VALUE = "c-test-0";

    @BeforeAll
    public static void beforeAll() {

        var applicationContext = Mockito.mock(ApplicationContext.class);
        var environment = Mockito.mock(Environment.class);

        Mockito.when(environment.getProperty(PROPERTY_A)).thenReturn(PROPERTY_A_VALUE);

        Mockito.when(environment.getProperty(PROPERTY_B, PROPERTY_B_VALUE)).thenReturn(PROPERTY_B_VALUE);
        Mockito.when(environment.getProperty(PROPERTY_B, "")).thenReturn("");

        Mockito.when(environment.getProperty(PROPERTY_C, "")).thenReturn(PROPERTY_C_VALUE);

        UtilCoreConfig.setApplicationContext(applicationContext);
        UtilCoreConfig.setEnvironment(environment);
    }

    @Test
    @DisplayName("Deve validar variaveis publicas")
    public void UT_CHECK_ENV_PUBLIC() {
        Assertions.assertDoesNotThrow(SystemUtil::new);
        Assertions.assertDoesNotThrow(() -> SystemUtil.sleep(1));
        Assertions.assertDoesNotThrow(() -> SystemUtil.sleep(0));
        Assertions.assertNotNull(SystemUtil.Env.JAVA_TEMP_DIR);
        Assertions.assertNotNull(SystemUtil.Env.JAVA_LIBRARY_PATH);
        Assertions.assertNotNull(SystemUtil.Env.OS_NAME);
        Assertions.assertNotNull(SystemUtil.Env.FILE_SEPARATOR);
        Assertions.assertNotNull(SystemUtil.Env.PATH_SEPARATOR);
        Assertions.assertNotNull(SystemUtil.Env.LINE_SEPARATOR);
        Assertions.assertNotNull(SystemUtil.Env.USER_NAME);
        Assertions.assertNotNull(SystemUtil.Env.USER_HOME);
        Assertions.assertNotNull(SystemUtil.Env.USER_DIR);
        Assertions.assertNotNull(SystemUtil.Env.JAVA_HOME);
        Assertions.assertNotNull(SystemUtil.Env.APP_NAME);
        Assertions.assertNotNull(SystemUtil.Env.getAppName());
    }

    @Test
    @DisplayName("Deve validar metodos GET")
    public void UT_CHECK_ENV_GET() {

        Assertions.assertDoesNotThrow(() -> SystemUtil.Env.getProperty(null));
        Assertions.assertDoesNotThrow(() -> SystemUtil.Env.getProperty(null, null));
        Assertions.assertDoesNotThrow(() -> SystemUtil.Env.getProperty(PROPERTY_A));
        Assertions.assertDoesNotThrow(() -> SystemUtil.Env.getProperty(PROPERTY_A, PROPERTY_A_VALUE));


        Assertions.assertEquals(SystemUtil.Env.getProperty(null), "");
        Assertions.assertEquals(SystemUtil.Env.getProperty(PROPERTY_A), PROPERTY_A_VALUE);
        Assertions.assertEquals(SystemUtil.Env.getProperty(PROPERTY_A, PROPERTY_A_VALUE), PROPERTY_A_VALUE);
        Assertions.assertEquals(SystemUtil.Env.getProperty(PROPERTY_A, ""), PROPERTY_A_VALUE);

        Assertions.assertEquals(SystemUtil.Env.getProperty(PROPERTY_B), "");
        Assertions.assertEquals(SystemUtil.Env.getProperty(PROPERTY_B, PROPERTY_B_VALUE), PROPERTY_B_VALUE);
        Assertions.assertEquals(SystemUtil.Env.getProperty(PROPERTY_B, ""), "");

        Assertions.assertEquals(SystemUtil.Env.getProperty(PROPERTY_C), "");
        Assertions.assertEquals(SystemUtil.Env.getProperty(PROPERTY_C, ""), PROPERTY_C_VALUE);
        Assertions.assertEquals(SystemUtil.Env.getProperty(PROPERTY_C, PROPERTY_C_VALUE), PROPERTY_C_VALUE);
    }


}
