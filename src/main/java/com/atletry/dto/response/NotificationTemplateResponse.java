package com.atletry.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Notification template details")
public class NotificationTemplateResponse {

    @Schema(description = "Template ID")
    private Long id;

    @Schema(description = "The event that triggers this notification", example = "MATCH_CREATED")
    private String eventType;

    @Schema(description = "Notification title (may contain resolved placeholders)", example = "New Match — Cricket!")
    private String title;

    @Schema(description = "Notification body text")
    private String body;

    @Schema(description = "Internal description of this template's purpose")
    private String description;

    @Schema(description = "Whether this template is active and will be used when the event fires")
    private boolean isActive;

    private ZonedDateTime createdDate;
    private ZonedDateTime updatedDate;
}
