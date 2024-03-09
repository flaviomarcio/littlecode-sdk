package com.littlecode.business;

import com.littlecode.business.databases.DataSourceBase;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class DataSourceTest {

    @Test
    public void UT_CHECK_VALIDATORS() {
        Assertions.assertNotNull(DataSourceBase.DS_H2);
        Assertions.assertNotNull(DataSourceBase.DS_MYSQL);
        Assertions.assertNotNull(DataSourceBase.DS_POSTGRES);
        Assertions.assertNotNull(DataSourceBase.DS_SQLSERVER);
        Assertions.assertNotNull(DataSourceBase.DS_ORACLE);
        Assertions.assertNotNull(DataSourceBase.DS_SERVICE);
    }


}
