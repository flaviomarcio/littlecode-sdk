package com.littlecode.tests.db;


import com.littlecode.setup.SetupDescription;
import com.littlecode.setup.SetupMetaObject;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public class ModelClasses {
    public enum State {
        State1, State2
    }

    @SuperBuilder
    @Getter
    @Setter
    @Data
    @Entity
    @Table(name = "model_1")
    public static class Model1 {
        @Id
        @Column(nullable = false)
        private UUID id;

        @Enumerated(EnumType.ORDINAL)
        private State state;

        @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
        @JoinColumn(name = "model2_id")
        private Model2 model2;

        @Enumerated(EnumType.ORDINAL)
        private State stateValue;

        @Column(nullable = false)
        private LocalDateTime dateTimeValue;

        @Column(nullable = false)
        private LocalDate dateValue;

        @Column(nullable = false)
        private LocalTime timeValue;

        @Column(length = 10)
        private String stringValue;

        private boolean boolValue;
        private Boolean boolValueC;

        private double doubleValue;
        private Double doubleValueC;

        private int intValue;
        private Integer intValueC;

        private long bigintValue;
        private Long bigintValueC;
        private BigInteger bigintValueCC;

    }

    @SuperBuilder
    @Getter
    @Setter
    @Data
    @Entity
    @Table(schema = "schema_model_2", name = "model_2")
    public static class Model2 {
        @Id
        @Column(nullable = false, updatable = false)
        @SetupDescription(value = "is a primary key")
        private UUID id;
        private UUID srcMd5;
    }

    @SuperBuilder
    @Getter
    @Setter
    @Data
    @Entity
    @Table(schema = "schema_model_3", name = "model_3")
    @SetupMetaObject(metaDataIgnore = true)
    public static class Model3 {
        @Id
        @Column(nullable = false, updatable = false)
        private UUID id;
    }

    @SuperBuilder
    @Getter
    @Setter
    @Data
    @Entity
    @Table(schema = "schema_model_4", name = "model_4")
    public static class Model4 {
        @Id
        @Column(nullable = false, updatable = false)
        @SetupDescription(value = "is a primary key")
        private UUID id;
        private UUID srcMd5;
    }
}
