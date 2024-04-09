package com.app.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemModelDTO {
    @Schema(title = "id",
            example = "0000-0000-0000-0000-000000000000",
            description = "id corresponding to the record",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private UUID id;

    @Schema(title = "Datetime of creation",
            example = "1901-01-01T00:00:00",
            description = "Datetime using ISODateTime with ms",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime dtCreate;

    @Schema(title = "Datetime of changes",
            example = "1901-01-01T00:00:00",
            description = "Change datetime using ISODateTime with ms",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDateTime dtChange;

    @Schema(title = "Name of record",
            example = "Name of record",
            description = "String to identify record",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    @NotNull
    @Size(max = 100)
    private String name;

    @Schema(title = "Title of record",
            example = "Title of record",
            description = "Title of record",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 200)
    @NotEmpty
    private String title;

    @Schema(title = "Description of record",
            example = "Description of record",
            description = "Description of record",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 1000)
    private String description;

    @Schema(title = "Route of tool",
            example = "http://tools-dns.com",
            description = "Route of tool",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 1000)
    @NotEmpty
    @NotNull
    private URI route;

    @Schema(title = "Icon of tool",
            example = "http://localhost/ico/iconame.png",
            description = "accepted files: png, svg, jpg, ico",
            maxLength = 50,
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @Size(max = 1000)
    private URI icon;

    @Schema(title = "Enable",
            example = "true|false",
            description = "Activity define",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    @NotNull
    private boolean enabled;
}
