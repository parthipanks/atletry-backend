package com.atletry.controller;

import com.atletry.dto.request.RegisterDeviceRequest;
import com.atletry.dto.response.ApiResponse;
import com.atletry.dto.response.UserDeviceResponse;
import com.atletry.entity.User;
import com.atletry.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/devices")
@RequiredArgsConstructor
@Tag(name = "8. Devices", description = "Register and manage mobile devices for push notifications")
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/register")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(
        summary = "Register or refresh a device FCM token",
        description = "Call this after login and whenever the FCM token refreshes. "
                    + "If the same deviceId already exists for this user the token is updated. "
                    + "If the token belonged to another user it is deactivated there first."
    )
    ResponseEntity<ApiResponse<UserDeviceResponse>> register(
            @Valid @RequestBody RegisterDeviceRequest req,
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Device registered", deviceService.registerDevice(user, req)));
    }

    @GetMapping("/my")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "List my registered devices")
    ResponseEntity<ApiResponse<List<UserDeviceResponse>>> getMyDevices(
            @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.ok("Devices fetched", deviceService.getMyDevices(user)));
    }

    @DeleteMapping("/{deviceId}/deactivate")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Deactivate a device", description = "The device will no longer receive push notifications.")
    ResponseEntity<ApiResponse<Void>> deactivate(
            @PathVariable Long deviceId,
            @AuthenticationPrincipal User user) {
        deviceService.deactivateDevice(user, deviceId);
        return ResponseEntity.ok(ApiResponse.ok("Device deactivated"));
    }
}
