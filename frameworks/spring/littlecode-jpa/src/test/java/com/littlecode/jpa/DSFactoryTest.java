package com.littlecode.jpa;

import com.littlecode.jpa.datasource.DSFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.vendor.Database;

import java.util.Map;

@ExtendWith(MockitoExtension.class)
public class DSFactoryTest {
    private static Environment mockEnviroments;

    @BeforeAll
    static void setup(){
        mockEnviroments =Mockito.mock(Environment.class);
        var envs= Map.of(
                "spring.datasource.h2.url","jdbc:h2:mem:testdb",
                "spring.datasource.h2.username","",
                "spring.datasource.h2.password","",
                "spring.datasource.h2.schema","",
                "spring.datasource.h2.continue-on-error","",
                "spring.datasource.h2.dialect","",
                "spring.datasource.h2.show_sql","true",
                "spring.datasource.h2.format_sql","true",
                "spring.datasource.h2.use_sql_comments","true"
        );
        envs
                .forEach((k, v) -> {
                    Mockito.when(mockEnviroments.containsProperty(k)).thenReturn(true);
                    Mockito.when(mockEnviroments.getProperty(k,"")).thenReturn(v);
                });

    }

    @Test
    public void UT_CHECK() {

        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(mockEnviroments, Database.H2, null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(mockEnviroments, null, null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(null, null, null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(mockEnviroments, Database.H2, new String[]{},null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(mockEnviroments, Database.H2, null,null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(mockEnviroments, null, null,null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(null, null, null,null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(mockEnviroments, Database.H2, new String[]{},"mysetting",null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(mockEnviroments, Database.H2, new String[]{},null,null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(mockEnviroments, Database.H2, null,null,null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(mockEnviroments, null, null,null,null));
        Assertions.assertThrows(NullPointerException.class,() -> new DSFactory(null, null, null,null,null));


        Assertions.assertDoesNotThrow(() -> new DSFactory(mockEnviroments, Database.H2, new String[]{}));
        Assertions.assertDoesNotThrow(() -> new DSFactory(mockEnviroments, Database.H2, new String[]{},"mysetting"));
        Assertions.assertDoesNotThrow(() -> new DSFactory(mockEnviroments, Database.H2, new String[]{},"mysetting","app.datasource"));
        var factory=new DSFactory(mockEnviroments, Database.H2, new String[]{"com.littlecode.jpa"});
        Assertions.assertNotNull(factory.getDatabase());
        Assertions.assertNotNull(factory.getPackages());
        Assertions.assertNotNull(factory.getDsName());
        Assertions.assertDoesNotThrow(()->factory.setDsName("dbName"));
        Assertions.assertNotNull(factory.getDsBasePath());
        Assertions.assertDoesNotThrow(()->factory.setDsBasePath("spring.datasource"));
        Assertions.assertNotNull(factory.getPackages());
        Assertions.assertNotNull(factory.makeDataSource());
        Assertions.assertNotNull(factory.makeEntityManagerFactory());
        Assertions.assertNotNull(factory.makeVendorAdapter());
        Assertions.assertNotNull(factory.makeTransactionManager());
    }
}
