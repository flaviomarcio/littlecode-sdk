package com.app.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupIn {
    @Schema(title = "id",
            example = "0000-0000-0000-0000-000000000000",
            description = "id corresponding to the record",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private UUID id;

    @Schema(title = "Datetime of creation",
            example = "1901-01-01T00:00:00",
            description = "Datetime using ISODate with ms",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime dtCreate;

    @Schema(title = "Datetime of changes",
            example = "1901-01-01T00:00:00",
            description = "Change datetime using ISODate with ms",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime dtChange;

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

}
