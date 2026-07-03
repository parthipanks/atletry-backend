package com.atletry.service;

import com.atletry.dto.request.CreateMatchRequest;
import com.atletry.dto.request.RateMatchRequest;
import com.atletry.dto.response.MatchResponse;
import com.atletry.dto.response.MyMatchesResponse;
import com.atletry.entity.Match;
import com.atletry.entity.MatchParticipant;
import com.atletry.entity.Sport;
import com.atletry.entity.User;
import com.atletry.enums.MatchStatus;
import com.atletry.enums.MediaUploadEntityType;
import com.atletry.enums.NotificationEventType;
import com.atletry.enums.ParticipantStatus;
import com.atletry.exception.BadRequestException;
import com.atletry.exception.ResourceNotFoundException;
import com.atletry.repository.MatchParticipantRepository;
import com.atletry.repository.MatchRepository;
import com.atletry.repository.SportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchRepository            matchRepository;
    private final MatchParticipantRepository participantRepository;
    private final SportRepository            sportRepository;
    private final PushNotificationService    pushNotificationService;
    private final MediaUploadService         mediaUploadService;

    @Transactional(readOnly = true)
    public List<MatchResponse> getOpenMatches(User currentUser, Long sportId) {
        List<MatchStatus> visible = Arrays.asList(MatchStatus.OPEN, MatchStatus.LIVE);
        List<Match> matches = sportId != null
                ? matchRepository.findBySportIdAndStatusInOrderByScheduledAtAsc(sportId, visible)
                : matchRepository.findByStatusInOrderByScheduledAtAsc(visible);

        return matches.stream()
                .map(m -> toMatchResponse(m, currentUser))
                .collect(Collectors.toList());
    }

    @Transactional
    public MatchResponse createMatch(User host, CreateMatchRequest req, List<MultipartFile> images) {
        Sport sport = sportRepository.findById(req.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found"));

        Match match = Match.builder()
                .host(host)
                .sport(sport)
                .title(req.getTitle())
                .venue(req.getVenue())
                .scheduledAt(req.getScheduledAt())
                .maxPlayers(req.getMaxPlayers())
                .build();

        MatchParticipant hostParticipant = MatchParticipant.builder()
                .match(match)
                .user(host)
                .status(ParticipantStatus.CONFIRMED)
                .build();
        match.getParticipants().add(hostParticipant);

        match = matchRepository.save(match);

        List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.MATCH, match.getId(), host);
        if (!urls.isEmpty()) {
            match.getImageUrls().addAll(urls);
            match = matchRepository.save(match);
        }

        MatchResponse response = toMatchResponse(match, host);

        pushNotificationService.sendBroadcast(NotificationEventType.MATCH_CREATED, Map.of(
                "sportName",   sport.getName(),
                "title",       req.getTitle(),
                "venue",       req.getVenue(),
                "creatorName", host.getName() != null ? host.getName() : ""
        ));

        return response;
    }

    @Transactional
    public MatchResponse joinMatch(User user, Long matchId) {
        Match match = findOrThrow(matchId);

        if (match.getStatus() == MatchStatus.COMPLETED || match.getStatus() == MatchStatus.CANCELLED) {
            throw new BadRequestException("Match is no longer open to join");
        }
        if (participantRepository.existsByMatchIdAndUserId(matchId, user.getId())) {
            throw new BadRequestException("Already joined this match");
        }

        int confirmed = participantRepository.countByMatchIdAndStatus(matchId, ParticipantStatus.CONFIRMED);
        if (confirmed >= match.getMaxPlayers()) {
            throw new BadRequestException("Match is full");
        }

        MatchParticipant participant = MatchParticipant.builder()
                .match(match)
                .user(user)
                .status(ParticipantStatus.CONFIRMED)
                .build();
        participantRepository.save(participant);

        return toMatchResponse(matchRepository.findById(matchId).orElseThrow(), user);
    }

    @Transactional
    public void leaveMatch(User user, Long matchId) {
        Match match = findOrThrow(matchId);

        if (match.getHost().getId().equals(user.getId())) {
            throw new BadRequestException("Host cannot leave their own match");
        }

        MatchParticipant participant = participantRepository.findByMatchIdAndUserId(matchId, user.getId())
                .orElseThrow(() -> new BadRequestException("Not a participant of this match"));

        participantRepository.delete(participant);
    }

    @Transactional(readOnly = true)
    public MyMatchesResponse getMyMatches(User user) {
        List<MatchParticipant> all = participantRepository.findByUserIdOrderByMatch_ScheduledAtAsc(user.getId());
        Instant now = Instant.now();

        List<MyMatchesResponse.UpcomingMatch> upcoming = all.stream()
                .filter(p -> p.getMatch().getScheduledAt().isAfter(now)
                        && p.getMatch().getStatus() != MatchStatus.CANCELLED)
                .map(p -> {
                    Match m = p.getMatch();
                    int current = participantRepository.countByMatchIdAndStatus(m.getId(), ParticipantStatus.CONFIRMED);
                    return MyMatchesResponse.UpcomingMatch.builder()
                            .id(m.getId())
                            .title(m.getTitle())
                            .sportName(m.getSport().getName())
                            .venue(m.getVenue())
                            .scheduledAt(m.getScheduledAt())
                            .currentPlayers(current)
                            .maxPlayers(m.getMaxPlayers())
                            .isHost(m.getHost().getId().equals(user.getId()))
                            .participantStatus(p.getStatus().name())
                            .build();
                })
                .collect(Collectors.toList());

        List<MyMatchesResponse.PastMatch> past = all.stream()
                .filter(p -> p.getMatch().getScheduledAt().isBefore(now)
                        || p.getMatch().getStatus() == MatchStatus.COMPLETED)
                .map(p -> {
                    Match m = p.getMatch();
                    String result = null;
                    if (p.getIsWinner() != null) {
                        result = p.getIsWinner() ? "WON" : "LOST";
                    }
                    return MyMatchesResponse.PastMatch.builder()
                            .id(m.getId())
                            .title(m.getTitle())
                            .sportName(m.getSport().getName())
                            .venue(m.getVenue())
                            .playedAt(m.getScheduledAt())
                            .result(result)
                            .score(m.getScore())
                            .isRated(p.getRating() != null)
                            .build();
                })
                .collect(Collectors.toList());

        return MyMatchesResponse.builder()
                .upcoming(upcoming)
                .past(past)
                .build();
    }

    @Transactional
    public void rateMatch(User user, Long matchId, RateMatchRequest req) {
        Match match = findOrThrow(matchId);

        if (match.getScheduledAt().isAfter(Instant.now())) {
            throw new BadRequestException("Cannot rate a match that hasn't been played yet");
        }

        MatchParticipant participant = participantRepository.findByMatchIdAndUserId(matchId, user.getId())
                .orElseThrow(() -> new BadRequestException("You did not participate in this match"));

        if (participant.getRating() != null) {
            throw new BadRequestException("Match already rated");
        }

        participant.setRating(req.getRating());
        participant.setRatedAt(Instant.now());
        participantRepository.save(participant);
    }

    @Transactional(readOnly = true)
    public List<MatchResponse> getPendingApproval() {
        return matchRepository.findByStatusOrderByCreatedDateDesc(MatchStatus.PENDING_APPROVAL)
                .stream().map(m -> toMatchResponse(m, null)).collect(Collectors.toList());
    }

    @Transactional
    public MatchResponse approve(Long matchId) {
        Match match = findOrThrow(matchId);
        if (match.getStatus() != MatchStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Match is not pending approval");
        }
        match.setStatus(MatchStatus.OPEN);
        return toMatchResponse(matchRepository.save(match), null);
    }

    @Transactional
    public MatchResponse reject(Long matchId) {
        Match match = findOrThrow(matchId);
        if (match.getStatus() != MatchStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Match is not pending approval");
        }
        match.setStatus(MatchStatus.CANCELLED);
        return toMatchResponse(matchRepository.save(match), null);
    }

    private Match findOrThrow(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Match not found"));
    }

    private MatchResponse toMatchResponse(Match match, User currentUser) {
        List<MatchParticipant> confirmed = match.getParticipants().stream()
                .filter(p -> p.getStatus() == ParticipantStatus.CONFIRMED)
                .toList();

        int confirmedCount = confirmed.size();
        int spotsOpen = Math.max(0, match.getMaxPlayers() - confirmedCount);

        List<String> initials = confirmed.stream()
                .limit(3)
                .map(p -> toInitials(p.getUser().getName()))
                .collect(Collectors.toList());

        int extra = Math.max(0, confirmedCount - 3);

        boolean isJoined = currentUser != null && confirmed.stream()
                .anyMatch(p -> p.getUser().getId().equals(currentUser.getId()));

        return MatchResponse.builder()
                .id(match.getId())
                .hostName(match.getHost().getName())
                .hostInitials(toInitials(match.getHost().getName()))
                .hostId(match.getHost().getId())
                .sportName(match.getSport().getName())
                .sportId(match.getSport().getId())
                .title(match.getTitle())
                .venue(match.getVenue())
                .scheduledAt(match.getScheduledAt())
                .maxPlayers(match.getMaxPlayers())
                .confirmedPlayers(confirmedCount)
                .spotsOpen(spotsOpen)
                .status(match.getStatus().name())
                .score(match.getScore())
                .imageUrls(new ArrayList<>(match.getImageUrls()))
                .isJoined(isJoined)
                .participantInitials(initials)
                .extraParticipants(extra)
                .build();
    }

    private String toInitials(String name) {
        if (name == null || name.isBlank()) return "?";
        String[] parts = name.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();
        for (String part : parts) {
            if (!part.isEmpty()) sb.append(Character.toUpperCase(part.charAt(0)));
        }
        return sb.toString();
    }
}
