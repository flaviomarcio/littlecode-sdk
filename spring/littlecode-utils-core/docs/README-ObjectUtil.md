# ObjectUtil

- Object to examples
    ```java
    @Data
    @Builder
    @Component
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ObjectCheck extends ObjectBase {
        private SubObjectCheck sub;
        private ObjectCheckType type;
    
        @NotNull
        private UUID id;
        private LocalDate date;
        private LocalTime time;
        private LocalDateTime dateTime;
        private double doubleValue;
        private int intValue;
        private boolean boolValue;
    
        private String stringValue;
        private Long longClassClass;
        private Double doubleValueClass;
        private Integer intValueClass;
        private Boolean boolValueClass;
    }
    ```