package com.littlecode.jpa;

import com.littlecode.jpa.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PhysicalNamingStrategyTest {

    @Test
    public void UT_CHECK() {
        Assertions.assertDoesNotThrow(() -> new PhysicalNamingStrategy());
        var strategy = new PhysicalNamingStrategy();
        strategy.toPhysicalCatalogName(new Identifier("abc",false), Mockito.mock(JdbcEnvironment.class));
        strategy.toPhysicalColumnName(new Identifier("abc",false), Mockito.mock(JdbcEnvironment.class));
        strategy.toPhysicalSchemaName(new Identifier("abc",false), Mockito.mock(JdbcEnvironment.class));
        strategy.toPhysicalSequenceName(new Identifier("abc",false), Mockito.mock(JdbcEnvironment.class));
        strategy.toPhysicalTableName(new Identifier("abc",false), Mockito.mock(JdbcEnvironment.class));

    }


}
