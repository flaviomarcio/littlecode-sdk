package com.app.business.dto;

import com.app.business.model.ofservice.ProxyForwarder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TargetIn {
    @Schema(title = "Record id",
            example = "0000-0000-0000-0000-000000000000",
            description = "id corresponding to the record",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private UUID id;

    @JsonIgnore
    @Schema(title = "Datetime of creation",
            example = "1901-01-01T00:00:00",
            description = "Datetime using ISODate with ms",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime dtCreate;

    @JsonIgnore
    @Schema(title = "Datetime of changes",
            example = "1901-01-01T00:00:00",
            description = "Change datetime using ISODate with ms",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime dtChange;

    @Schema(title = "Name of record",
            example = "Group of record",
            description = "Id to grouping records",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private UUID groupId;

    @Schema(title = "Name of record",
            example = "Name of record",
            description = "String to identify record",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    @Size(max = 50)
    private String name;

    @Schema(title = "Name of record",
            example = "Description of record",
            description = "Description of record",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 200)
    private String description;

    @Schema(title = "Enable",
            example = "true|false",
            description = "Activity define",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @NotEmpty
    @NotBlank
    private boolean enabled;

    @Schema(title = "Dispatches of target",
            example = "List of class Dispatcher",
            description = "Settings to configuration dispatches for sms, e-mail, ...",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    @NotNull
    @NotBlank
    private List<Dispatcher> dispatchers;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Dispatcher {
        @Schema(title = "Dispatches type",
                example = "eMail|SMS|...",
                description = "Settings to configuration dispatches by dispatcher type",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Enumerated(EnumType.STRING)
        private ProxyForwarder.Dispatcher dispatcher;
        @Schema(title = "Name of dispatcher",
                example = "Dispatcher by type",
                description = "String to identify dispatcher",
                maxLength = 50,
                requiredMode = Schema.RequiredMode.REQUIRED)
        @Size(max = 50)
        private String name;
        @Schema(title = "Enable",
                example = "true|false",
                description = "Activity define",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        @NotEmpty
        @NotBlank
        private boolean enabled;
    }
}
