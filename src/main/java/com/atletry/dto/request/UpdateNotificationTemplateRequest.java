package com.atletry.dto.request;

import com.atletry.enums.NotificationEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Partial update for a notification template — only provided fields are changed")
public class UpdateNotificationTemplateRequest {

    @Schema(description = "Change the event type this template is bound to")
    private NotificationEventType eventType;

    @Size(max = 200, message = "title must be at most 200 characters")
    @Schema(description = "Updated notification title")
    private String title;

    @Schema(description = "Updated notification body")
    private String body;

    @Schema(description = "Updated internal description")
    private String description;

    @Schema(description = "Set to false to disable this template without deleting it")
    private Boolean isActive;
}
