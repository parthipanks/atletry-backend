package com.atletry.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final AtletryProperties properties;
    private final ResourceLoader     resourceLoader;

    /**
     * Returns null (not a bean) when Firebase is disabled so PushNotificationService
     * gracefully skips sending. Supports both classpath: and file: resource paths.
     */
    @Bean
    FirebaseMessaging firebaseMessaging() {
        AtletryProperties.Firebase firebase = properties.getFirebase();

        if (!firebase.isEnabled() || firebase.getServiceAccountPath().isBlank()) {
            log.warn("Firebase disabled or service-account-path not set — push notifications will be skipped");
            return null;
        }

        try {
            Resource resource = resourceLoader.getResource(firebase.getServiceAccountPath());
            if (!resource.exists()) {
                log.error("Firebase service account file not found: {}", firebase.getServiceAccountPath());
                return null;
            }

            try (InputStream serviceAccount = resource.getInputStream()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                        .build();

                FirebaseApp app = FirebaseApp.getApps().isEmpty()
                        ? FirebaseApp.initializeApp(options)
                        : FirebaseApp.getInstance();

                log.info("Firebase initialized from {}", firebase.getServiceAccountPath());
                return FirebaseMessaging.getInstance(app);
            }
        } catch (IOException e) {
            log.error("Failed to initialize Firebase: {}", e.getMessage());
            return null;
        }
    }
}
