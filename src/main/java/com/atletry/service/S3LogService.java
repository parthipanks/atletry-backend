package com.atletry.service;

import com.atletry.config.AtletryProperties;
import com.atletry.entity.Sport;
import com.atletry.entity.SportActivityLog;
import com.atletry.entity.User;
import com.atletry.enums.SportActivityAction;
import com.atletry.repository.SportActivityLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3LogService {

    private final S3Client                 s3;
    private final AtletryProperties        props;
    private final SportActivityLogRepository logRepo;
    private final ObjectMapper             mapper;

    private static final DateTimeFormatter DATE =
            DateTimeFormatter.ofPattern("yyyy/MM/dd").withZone(ZoneOffset.UTC);

    /**
     * Async: uploads a JSON activity log to S3 and saves a reference row to the DB.
     * Never throws — failures are only logged, not propagated.
     */
    @Async
    public void log(User user, Sport sport, SportActivityAction action,
                    Map<String, Object> extra) {
        try {
            Map<String, Object> payload = buildPayload(user, sport, action, extra);
            String key    = buildKey(user, sport, action);
            String bucket = props.getAws().getS3().getBucket();
            String json   = mapper.writeValueAsString(payload);

            s3.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket).key(key).contentType("application/json").build(),
                    RequestBody.fromString(json));

            logRepo.save(SportActivityLog.builder()
                    .user(user).sport(sport).action(action).s3Key(key).payload(payload).build());

            log.debug("Activity logged → s3://{}/{}", bucket, key);

        } catch (Exception e) {
            log.error("S3 log failed [user={} sport={} action={}]: {}",
                    user.getId(), sport.getId(), action, e.getMessage());
        }
    }

    // ── helpers ──────────────────────────────────────────────────────────────

    private Map<String, Object> buildPayload(User user, Sport sport,
                                             SportActivityAction action,
                                             Map<String, Object> extra) {
        Map<String, Object> p = new LinkedHashMap<>();
        p.put("eventType",  action.name());
        p.put("timestamp",  Instant.now().toString());
        p.put("userId",     user.getId().toString());
        p.put("mobile",     mask(user.getMobile()));
        p.put("sportId",    sport.getId().toString());
        p.put("sportName",  sport.getName());
        if (extra != null) p.putAll(extra);
        return p;
    }

    private String buildKey(User user, Sport sport, SportActivityAction action) {
        // logs/sport-activity/2025/01/15/user-<id>/SPORT_SELECTED-cricket-1736938200000.json
        return "logs/sport-activity/%s/user-%s/%s-%s-%d.json".formatted(
                DATE.format(Instant.now()),
                user.getId(),
                action.name(),
                sport.getName().toLowerCase().replace(" ", "-"),
                System.currentTimeMillis());
    }

    private String mask(String mobile) {
        if (mobile == null || mobile.length() < 4) return "****";
        return mobile.substring(0, 2) + "****" + mobile.substring(mobile.length() - 2);
    }
}
