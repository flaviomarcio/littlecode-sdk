package com.littlecode.health;

import java.time.LocalDateTime;

public class HealthCheckStatics {
    public static LocalDateTime staticStartExecution=LocalDateTime.now();
    public static LocalDateTime staticLastExecution;
    public static String staticFailMessage;
}
