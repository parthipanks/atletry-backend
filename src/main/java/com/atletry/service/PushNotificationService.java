package com.atletry.service;

import com.atletry.entity.NotificationTemplate;
import com.atletry.entity.User;
import com.atletry.entity.UserDevice;
import com.atletry.entity.UserNotification;
import com.atletry.enums.NotificationEventType;
import com.atletry.repository.NotificationTemplateRepository;
import com.atletry.repository.UserDeviceRepository;
import com.atletry.repository.UserNotificationRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private static final int FCM_BATCH_SIZE = 500;

    private final UserDeviceRepository           deviceRepository;
    private final NotificationTemplateRepository templateRepository;
    private final UserNotificationRepository     notificationRepository;

    @Autowired(required = false)
    private FirebaseMessaging firebaseMessaging;

    @Async
    @Transactional
    public void sendBroadcast(NotificationEventType eventType, Map<String, String> placeholders) {
        try {
            Optional<NotificationTemplate> optTemplate =
                    templateRepository.findByEventTypeAndIsActiveTrue(eventType);

            if (optTemplate.isEmpty()) {
                log.debug("No active template for event {} — skipping push", eventType);
                return;
            }

            NotificationTemplate template = optTemplate.get();
            String title = resolve(template.getTitle(), placeholders);
            String body  = resolve(template.getBody(),  placeholders);

            List<UserDevice> devices = deviceRepository.findAllByIsActiveTrue();
            if (devices.isEmpty()) {
                log.debug("No active device tokens — skipping push for event {}", eventType);
                return;
            }

            // Deduplicate by user — one notification record per user
            Map<Long, User> uniqueUsers = new LinkedHashMap<>();
            List<String> allTokens = new ArrayList<>();
            for (UserDevice device : devices) {
                uniqueUsers.putIfAbsent(device.getUser().getId(), device.getUser());
                allTokens.add(device.getFcmToken());
            }

            // Persist one record per user
            List<UserNotification> records = uniqueUsers.values().stream()
                    .map(user -> UserNotification.builder()
                            .user(user)
                            .title(title)
                            .body(body)
                            .eventType(eventType)
                            .build())
                    .toList();
            notificationRepository.saveAll(records);

            // Send via FCM only if Firebase is configured
            if (firebaseMessaging == null) {
                log.debug("Firebase not configured — notification recorded but not pushed for event {}", eventType);
                return;
            }

            sendInBatches(allTokens, title, body, eventType.name());

        } catch (Exception e) {
            log.error("Push notification failed for event {} — API not affected: {}", eventType, e.getMessage(), e);
        }
    }

    private void sendInBatches(List<String> tokens, String title, String body, String event) {
        List<List<String>> batches = partition(tokens, FCM_BATCH_SIZE);
        for (List<String> batch : batches) {
            try {
                MulticastMessage message = MulticastMessage.builder()
                        .setNotification(Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build())
                        .putData("event", event)
                        .addAllTokens(batch)
                        .build();
                var response = firebaseMessaging.sendEachForMulticast(message);
                log.info("FCM batch sent: {} success, {} failure — event={}",
                        response.getSuccessCount(), response.getFailureCount(), event);
            } catch (Exception e) {
                log.error("FCM batch send failed for event {}: {}", event, e.getMessage());
            }
        }
    }

    private String resolve(String template, Map<String, String> placeholders) {
        if (template == null || placeholders == null) return template;
        String result = template;
        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue() != null ? entry.getValue() : "");
        }
        return result;
    }

    private <T> List<List<T>> partition(List<T> list, int size) {
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}
