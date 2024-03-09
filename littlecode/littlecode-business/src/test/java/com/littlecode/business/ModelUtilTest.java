package com.littlecode.business;

import com.littlecode.business.util.ModelUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ModelUtilTest {

    @Test
    public void UT_CHECK() {
        Assertions.assertTrue(
                ModelUtil
                        .target(
                                ObjectTest.builder()
                                        .id(UUID.randomUUID())//not null
                                        .dt(LocalDateTime.now())//not null
                                        .name("test")//not null
                                        .build()
                        )
                        .isValid()
        );

        Assertions.assertFalse(
                ModelUtil
                        .target(
                                ObjectTest.builder()
                                        .id(null)//not null
                                        .dt(LocalDateTime.now())//not null
                                        .name("test")//length 10
                                        .build()
                        )
                        .isValid()
        );

        Assertions.assertFalse(
                ModelUtil
                        .target(
                                ObjectTest.builder()
                                        .id(UUID.randomUUID())//not null
                                        .dt(null)//not null
                                        .name("test")//length 10
                                        .build()
                        )
                        .isValid()
        );

        Assertions.assertFalse(
                ModelUtil
                        .target(
                                ObjectTest.builder()
                                        .id(UUID.randomUUID())//not null
                                        .dt(LocalDateTime.now())//not null
                                        .name("test test test test")//length 10
                                        .build()
                        )
                        .isValid()
        );


    }


    @Entity
    @Table
    @Builder
    public static class ObjectTest {
        @Id
        private UUID id;
        @Column(nullable = false)
        private LocalDateTime dt;
        @Column(nullable = false, length = 10)
        private String name;
    }
}
