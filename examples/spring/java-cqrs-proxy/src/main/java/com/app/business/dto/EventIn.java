package com.app.business.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventIn {
    @Schema(title = "Producer",
            example = "{\"id\":\"b6d45e4a-df2c-44a3-803e-2bd08273b06d\"},\"accessKey\":\"4a946fd3-f18b-4b89-ab14-a3a960d868a4\"}",
            description = "Targets for sent messages, for more detail check DTO [Target]",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Producer producer;
    @Schema(title = "Target destinations",
            example = "[{\"targetId\":\"4a946fd3-f18b-4b89-ab14-a3a960d868a4\"},\"forwarderId\":\"b6d45e4a-df2c-44a3-803e-2bd08273b06d\",\"destinations\":[]}]",
            description = "Targets for sent messages, for more detail check DTO [Target]",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty
    private Target target;

    @Schema(title = "Message to sent",
            example = "Hi, your order is finished",
            description = "Message to sent",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Object body;

    @Builder
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Producer {
        @Schema(title = "Producer of message",
                example = "b6d45e4a-df2c-44a3-803e-2bd08273b06d",
                description = "Id of producer",
                requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        private UUID id;

        @Schema(title = "Producer of message",
                example = "4a946fd3-f18b-4b89-ab14-a3a960d868a4",
                description = "Optional, but required when the Producer is configured to require",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private UUID accessKey;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Target {
        @Schema(title = "Target destination",
                example = "2e8ed669-52cb-4b97-98dc-e5eb986e5b7c",
                description = "Target destinations for sending the message, required if [forwarderId] is empty",
                maxLength = 50,
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private UUID targetId;

        @Schema(title = "Forwarder",
                example = "586889e0-56d7-42c8-92e8-b55c366b3363",
                description = "Forwarder is the forwarder for sending the message, mandatory if [targetId] is empty and mandatory to fill in [destinations], Contains presets for [Forwarder] and [Destinations]",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private UUID forwarderId;

        @Schema(title = "Target destination",
                example = "[\"john@email.com\",\"jack@email.com\"], fill in the list with account data (email, id, phonenumber,...), for more detail check domain [Dispatcher]",
                description = "Required if [forwarderId] is not empty and optional if [targetId] is not empty",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private List<String> destinations;

        public List<String> getDestinations() {
            if (this.destinations == null)
                this.destinations = new ArrayList<>();
            return this.destinations;
        }
    }

    @Builder
    @Data
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class Message {
        @Schema(title = "id of template",
                example = "b6d45e4a-df2c-44a3-803e-2bd08273b06d",
                description = "Predefined template with message body and arguments, for more detail check domain [Template]",
                requiredMode = Schema.RequiredMode.REQUIRED)
        private UUID templateId;

        @Schema(title = "Producer of message",
                example = "4a946fd3-f18b-4b89-ab14-a3a960d868a4",
                description = "Id of producer",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private UUID accessKey;
    }

}
