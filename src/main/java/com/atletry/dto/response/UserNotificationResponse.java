package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.ZonedDateTime;


@Data
@Builder
public class UserNotificationResponse {
    private Long          id;
    private String        title;
    private String        body;
    private String        eventType;
    private boolean       isRead;
    private ZonedDateTime sentAt;
}
