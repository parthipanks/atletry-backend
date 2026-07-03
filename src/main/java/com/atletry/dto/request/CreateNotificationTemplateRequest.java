package com.atletry.dto.request;

import com.atletry.enums.NotificationEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Request to create a notification template for a push event")
public class CreateNotificationTemplateRequest {

    @NotNull(message = "eventType is required")
    @Schema(description = "The event that triggers this notification", example = "MATCH_CREATED")
    private NotificationEventType eventType;

    @NotBlank(message = "title is required")
    @Size(max = 200, message = "title must be at most 200 characters")
    @Schema(description = "Notification title. Use {placeholders} for dynamic values", example = "New Match — {sportName}!")
    private String title;

    @NotBlank(message = "body is required")
    @Schema(description = "Notification body text. Supports {title}, {sportName}, {venue}, {name}, {creatorName}", example = "A new {sportName} match '{title}' is open at {venue}. Join now!")
    private String body;

    @Schema(description = "Internal description of what this template is used for")
    private String description;
}
