package com.littlecode.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.setup.SetupMetaIndex;
import com.littlecode.setup.config.SetupAutoConfiguration;
import com.littlecode.setup.db.metadata.MetaDataEngine;
import com.littlecode.tests.db.HikariConnectionPool;
import com.littlecode.tests.db.ModelClasses;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SetupTests {
    private final Factory factory = new Factory();

    @BeforeEach
    void beforeAll() {
        UtilCoreConfig.setApplicationContext(factory.getMockContext());
        UtilCoreConfig.setEnvironment(factory.getMockEnvironment());
    }


//    public void UT_CHECK() throws SQLException {
//        this.UT_CHECK_OBJECTS();
//        this.UT_CHECK_META_DATA();
//        this.UT_CHECK_META_INDEX();
//        this.UT_CHECK_EXECUTE();
//        this.UT_CHECK_STARTED();
//    }

    @Test
    public void UT_CHECK_OBJECTS() throws SQLException {
        Assertions.assertNotNull(factory.getMockConfig());
        Assertions.assertNotNull(factory.getMockSetting());

        //check connections
        var connection = HikariConnectionPool.getConnection();
        Assertions.assertNotNull(connection);
        Assertions.assertFalse(connection.isClosed());
        connection.close();
    }

    @Test
    public void UT_CHECK_META_DATA() {
        MetaDataEngine metaDataEngine = MetaDataEngine
                .builder()
                .setting(factory.getSetup().getSetting())
                .connection(HikariConnectionPool.getConnection())
                .packagesBase(new String[]{ModelClasses.class.getPackageName()})
                .build();
        Assertions.assertDoesNotThrow(() -> metaDataEngine.load().export());
        Assertions.assertNotNull(metaDataEngine.getSources());
        Assertions.assertFalse(metaDataEngine.getSources().isEmpty());
        Assertions.assertEquals(metaDataEngine.getSources().size(), 3);
        var statements = metaDataEngine.getStatements();
        Assertions.assertNotNull(statements);
        Assertions.assertEquals(statements.size(), 5);
        Assertions.assertDoesNotThrow(() -> metaDataEngine.clear().load().export());
    }

    @Test
    public void UT_CHECK_EXECUTE() {
        Assertions.assertNotNull(factory.getSetup());
        Assertions.assertDoesNotThrow(() -> factory.getSetup().execute());
    }

    @Test
    public void UT_CHECK_STARTED() {
        var env = factory.getMockEnvironment();
        Mockito.when(env.getProperty("spring.datasource.username")).thenReturn("services");
        Mockito.when(env.getProperty("spring.datasource.password")).thenReturn("services");
        Mockito.when(env.getProperty("spring.datasource.url")).thenReturn("jdbc:postgresql://localhost:5432/services");
        Assertions.assertDoesNotThrow(() -> new SetupAutoConfiguration(factory.getMockContext(), factory.getMockEnvironment()));
    }

    @Test
    public void UT_CHECK_META_INDEX() {
        Assertions.assertNull(SetupMetaIndex
                .builder()
                .name(SetupMetaIndex.Default)
                .build()
                .getName()
        );

        Assertions.assertNull(SetupMetaIndex
                .builder()
                .type(SetupMetaIndex.Default)
                .build()
                .getType()
        );

        Assertions.assertNull(SetupMetaIndex
                .builder()
                .using(SetupMetaIndex.Default)
                .build()
                .getUsing()
        );

        Assertions.assertEquals(SetupMetaIndex
                .builder()
                .type(SetupMetaIndex.Type.Unique)
                .build()
                .getType(), SetupMetaIndex.Type.Unique
        );

        Assertions.assertEquals(SetupMetaIndex
                .builder()
                .using(SetupMetaIndex.Using.Btree)
                .build()
                .getUsing(), SetupMetaIndex.Using.Btree
        );
    }


}
