package com.atletry.dto.request;

import com.atletry.enums.DevicePlatform;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Register or update a mobile device FCM token for push notifications")
public class RegisterDeviceRequest {

    @NotBlank(message = "deviceId is required")
    @Size(max = 255, message = "deviceId must be at most 255 characters")
    @Schema(description = "Unique hardware/installation identifier for this device", example = "550e8400-e29b-41d4-a716-446655440000")
    private String deviceId;

    @NotBlank(message = "fcmToken is required")
    @Size(max = 500, message = "fcmToken must be at most 500 characters")
    @Schema(description = "Firebase Cloud Messaging registration token obtained from the mobile SDK")
    private String fcmToken;

    @NotNull(message = "platform is required")
    @Schema(description = "Device operating system", example = "ANDROID")
    private DevicePlatform platform;
}
