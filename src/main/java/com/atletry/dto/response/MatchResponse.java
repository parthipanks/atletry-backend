package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data
@Builder
public class MatchResponse {

    private Long id;
    private String hostName;
    private String hostInitials;
    private Long hostId;
    private String sportName;
    private Long sportId;
    private String title;
    private String venue;
    private Instant scheduledAt;
    private int maxPlayers;
    private int confirmedPlayers;
    private int spotsOpen;
    private String status;
    private String score;
    private List<String> imageUrls;
    private boolean isJoined;
    /** Initials of first 3 confirmed participants (excluding host) */
    private List<String> participantInitials;
    /** Participant count beyond the first 3 shown */
    private int extraParticipants;
}
