package com.atletry.service;

import com.atletry.dto.request.RegisterDeviceRequest;
import com.atletry.dto.response.UserDeviceResponse;
import com.atletry.entity.User;
import com.atletry.entity.UserDevice;
import com.atletry.exception.ResourceNotFoundException;
import com.atletry.repository.UserDeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceService {

    private final UserDeviceRepository deviceRepository;

    /**
     * Register or refresh an FCM token for the authenticated user's device.
     *
     * Rules:
     * 1. If the same FCM token is registered to a different user, deactivate it there first
     *    (the token now belongs to this user's device).
     * 2. If this user already has a record for this deviceId, update the FCM token.
     * 3. Otherwise create a new device record.
     */
    @Transactional
    public UserDeviceResponse registerDevice(User user, RegisterDeviceRequest req) {
        // Invalidate stale records that hold the same FCM token for other users
        deviceRepository.findByFcmToken(req.getFcmToken()).stream()
                .filter(d -> !d.getUser().getId().equals(user.getId()))
                .forEach(d -> {
                    d.setActive(false);
                    deviceRepository.save(d);
                    log.info("Deactivated stale FCM token for previous user {}", d.getUser().getId());
                });

        // Upsert: update existing device record or create a new one
        UserDevice device = deviceRepository
                .findByUserIdAndDeviceId(user.getId(), req.getDeviceId())
                .map(existing -> {
                    existing.setFcmToken(req.getFcmToken());
                    existing.setPlatform(req.getPlatform());
                    existing.setActive(true);
                    return existing;
                })
                .orElseGet(() -> UserDevice.builder()
                        .user(user)
                        .deviceId(req.getDeviceId())
                        .fcmToken(req.getFcmToken())
                        .platform(req.getPlatform())
                        .build());

        return toResponse(deviceRepository.save(device));
    }

    @Transactional(readOnly = true)
    public List<UserDeviceResponse> getMyDevices(User user) {
        return deviceRepository.findByUserId(user.getId())
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public void deactivateDevice(User user, Long deviceId) {
        UserDevice device = deviceRepository.findById(deviceId)
                .filter(d -> d.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new ResourceNotFoundException("Device not found: " + deviceId));
        device.setActive(false);
        deviceRepository.save(device);
    }

    private UserDeviceResponse toResponse(UserDevice d) {
        String token = d.getFcmToken();
        String masked = token.length() > 8
                ? "…" + token.substring(token.length() - 8)
                : "…" + token;

        return UserDeviceResponse.builder()
                .id(d.getId())
                .userId(d.getUser().getId())
                .deviceId(d.getDeviceId())
                .fcmTokenMasked(masked)
                .platform(d.getPlatform().name())
                .isActive(d.isActive())
                .createdDate(d.getCreatedDate())
                .updatedDate(d.getUpdatedDate())
                .build();
    }
}
