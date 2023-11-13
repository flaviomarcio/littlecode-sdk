package com.littlecode.business;

import com.littlecode.business.databases.DataSourceBase;
import com.littlecode.business.validator.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

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
    }


}
