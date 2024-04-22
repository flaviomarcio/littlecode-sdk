package com.littlecode.jpa;

import com.littlecode.jpa.datasource.DSFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.vendor.Database;

@ExtendWith(MockitoExtension.class)
public class DSFactoryTest {

    @Test
    public void UT_CHECK() {
        Assertions.assertDoesNotThrow(() -> new DSFactory(Mockito.mock(Environment.class), Database.H2, new String[]{}));
        var factory=new DSFactory(Mockito.mock(Environment.class), Database.H2, new String[]{"com.littlecode.jpa"});
        Assertions.assertNotNull(factory.getDatabase());
        Assertions.assertNotNull(factory.getPackages());
        Assertions.assertNotNull(factory.makeDataSource());
        Assertions.assertNotNull(factory.makeEntityManagerFactory());
        Assertions.assertNotNull(factory.makeVendorAdapter());
        Assertions.assertNotNull(factory.makeTransactionManager());
    }
}
