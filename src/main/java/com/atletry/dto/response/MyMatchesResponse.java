package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.List;


@Data
@Builder
public class MyMatchesResponse {

    private List<UpcomingMatch> upcoming;
    private List<PastMatch> past;

    @Data
    @Builder
    public static class UpcomingMatch {
        private Long id;
        private String title;
        private String sportName;
        private String venue;
        private Instant scheduledAt;
        private int currentPlayers;
        private int maxPlayers;
        private boolean isHost;
        private String participantStatus;
    }

    @Data
    @Builder
    public static class PastMatch {
        private Long id;
        private String title;
        private String sportName;
        private String venue;
        private Instant playedAt;
        /** WON, LOST, DRAW, or null if result not set */
        private String result;
        private String score;
        private boolean isRated;
    }
}
