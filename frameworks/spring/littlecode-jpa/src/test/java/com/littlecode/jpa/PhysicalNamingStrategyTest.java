package com.littlecode.jpa;

import com.littlecode.jpa.model.naming.PhysicalNamingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PhysicalNamingStrategyTest {

    @Test
    public void UT_CHECK() {
        Assertions.assertDoesNotThrow(() -> new PhysicalNamingStrategy());
    }


}
