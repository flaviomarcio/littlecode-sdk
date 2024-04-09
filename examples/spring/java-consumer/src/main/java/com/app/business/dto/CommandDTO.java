package com.app.business.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommandDTO {

    @Schema(title = "actiopn",
            example = "0000-0000-0000-0000-000000000000",
            description = "actions",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public Action action;
    @Schema(title = "target",
            example = "0000-0000-0000-0000-000000000000",
            description = "Body of object target for actions",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public ItemModelDTO target;
    @Schema(title = "callBack",
            example = "0000-0000-0000-0000-000000000000",
            description = "Enable callback of message id",
            requiredMode = Schema.RequiredMode.REQUIRED)
    public boolean callBack;
    @Schema(title = "message id",
            example = "0000-0000-0000-0000-000000000000",
            description = "Message identofier",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID messageId;

    @Getter
    public enum Action {
        SELECT(0),
        INSERT(1),
        UPDATE(2),
        UPSERT(3),
        DELETE(4);
        @JsonValue
        private final int value;

        Action(int value) {
            this.value = value;
        }

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static Action get(int value) {
            return Arrays.stream(values()).filter(t -> Objects.equals(t.getValue(), value)).findFirst().orElse(null);
        }

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static Action get(String value) {
            if (value == null)
                return null;
            return Arrays.stream(values()).filter(t -> t.name().equalsIgnoreCase(value)).findFirst().orElse(null);
        }
    }
}
