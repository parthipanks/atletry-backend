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
@Schema(description = "Registered device details")
public class UserDeviceResponse {

    @Schema(description = "Device record ID")
    private Long id;

    @Schema(description = "Owning user ID")
    private Long userId;

    @Schema(description = "Unique hardware/installation identifier")
    private String deviceId;

    @Schema(description = "FCM token (last 8 chars shown for security)", example = "…a1b2c3d4")
    private String fcmTokenMasked;

    @Schema(description = "Device platform", example = "ANDROID")
    private String platform;

    @Schema(description = "Whether this device is active and will receive push notifications")
    private boolean isActive;

    private ZonedDateTime createdDate;
    private ZonedDateTime updatedDate;
}
