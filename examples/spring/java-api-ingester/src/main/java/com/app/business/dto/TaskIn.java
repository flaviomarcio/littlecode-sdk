package com.app.business.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
public class TaskIn {
    private UUID id;
    private LocalDateTime dt;
    private String state;
    private UUID checksum;
    private Object payload;
}
