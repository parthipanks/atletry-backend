package com.atletry.service;

import com.atletry.dto.request.CreateTournamentRequest;
import com.atletry.dto.request.RegisterTournamentRequest;
import com.atletry.dto.response.TournamentResponse;
import com.atletry.entity.Sport;
import com.atletry.entity.Tournament;
import com.atletry.entity.TournamentRegistration;
import com.atletry.entity.User;
import com.atletry.enums.MediaUploadEntityType;
import com.atletry.enums.NotificationEventType;
import com.atletry.enums.TournamentStatus;
import com.atletry.exception.BadRequestException;
import com.atletry.exception.ResourceNotFoundException;
import com.atletry.repository.SportRepository;
import com.atletry.repository.TournamentRegistrationRepository;
import com.atletry.repository.TournamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TournamentService {

    private final TournamentRepository             tournamentRepository;
    private final TournamentRegistrationRepository registrationRepository;
    private final SportRepository                  sportRepository;
    private final PushNotificationService          pushNotificationService;
    private final MediaUploadService               mediaUploadService;

    @Transactional(readOnly = true)
    public List<TournamentResponse> getTournaments(User currentUser, Long sportId) {
        List<TournamentStatus> visible = Arrays.asList(TournamentStatus.OPEN, TournamentStatus.ONGOING);
        List<Tournament> tournaments = sportId != null
                ? tournamentRepository.findBySportIdAndStatusInOrderByStartsAtAsc(sportId, visible)
                : tournamentRepository.findByStatusInOrderByStartsAtAsc(visible);

        return tournaments.stream()
                .map(t -> toResponse(t, currentUser))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TournamentResponse getById(Long id, User currentUser) {
        Tournament tournament = findOrThrow(id);
        return toResponse(tournament, currentUser);
    }

    @Transactional
    public TournamentResponse create(User creator, CreateTournamentRequest req, List<MultipartFile> images) {
        Sport sport = sportRepository.findById(req.getSportId())
                .orElseThrow(() -> new ResourceNotFoundException("Sport not found"));

        Tournament tournament = Tournament.builder()
                .name(req.getName())
                .sport(sport)
                .format(req.getFormat())
                .entryUnit(req.getEntryUnit())
                .maxSlots(req.getMaxSlots())
                .entryFee(req.getEntryFee())
                .prizePool(req.getPrizePool())
                .startsAt(req.getStartsAt())
                .registrationDeadline(req.getRegistrationDeadline())
                .createdBy(creator)
                .build();

        tournament = tournamentRepository.save(tournament);

        List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.TOURNAMENT, tournament.getId(), creator);
        if (!urls.isEmpty()) {
            tournament.getImageUrls().addAll(urls);
            tournament = tournamentRepository.save(tournament);
        }

        TournamentResponse response = toResponse(tournament, creator);

        pushNotificationService.sendBroadcast(NotificationEventType.TOURNAMENT_CREATED, Map.of(
                "name",        req.getName(),
                "sportName",   sport.getName(),
                "creatorName", creator.getName() != null ? creator.getName() : ""
        ));

        return response;
    }

    @Transactional
    public TournamentResponse register(User user, Long tournamentId, RegisterTournamentRequest req) {
        Tournament tournament = findOrThrow(tournamentId);

        if (tournament.getStatus() != TournamentStatus.OPEN) {
            throw new BadRequestException("Tournament registration is closed");
        }
        if (tournament.getRegistrationDeadline().isBefore(Instant.now())) {
            throw new BadRequestException("Registration deadline has passed");
        }
        if (registrationRepository.existsByTournamentIdAndUserId(tournamentId, user.getId())) {
            throw new BadRequestException("Already registered for this tournament");
        }

        int registered = registrationRepository.countByTournamentId(tournamentId);
        if (registered >= tournament.getMaxSlots()) {
            throw new BadRequestException("Tournament is full");
        }

        TournamentRegistration registration = TournamentRegistration.builder()
                .tournament(tournament)
                .user(user)
                .teamName(req.getTeamName())
                .build();
        registrationRepository.save(registration);

        return toResponse(tournamentRepository.findById(tournamentId).orElseThrow(), user);
    }

    @Transactional
    public void cancelRegistration(User user, Long tournamentId) {
        TournamentRegistration registration = registrationRepository
                .findByTournamentIdAndUserId(tournamentId, user.getId())
                .orElseThrow(() -> new BadRequestException("Not registered for this tournament"));
        registrationRepository.delete(registration);
    }

    @Transactional(readOnly = true)
    public List<TournamentResponse> getPendingApproval() {
        return tournamentRepository.findByStatusOrderByCreatedDateDesc(TournamentStatus.PENDING_APPROVAL)
                .stream().map(t -> toResponse(t, null)).collect(Collectors.toList());
    }

    @Transactional
    public TournamentResponse approve(Long tournamentId) {
        Tournament tournament = findOrThrow(tournamentId);
        if (tournament.getStatus() != TournamentStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Tournament is not pending approval");
        }
        tournament.setStatus(TournamentStatus.OPEN);
        return toResponse(tournamentRepository.save(tournament), null);
    }

    @Transactional
    public TournamentResponse reject(Long tournamentId) {
        Tournament tournament = findOrThrow(tournamentId);
        if (tournament.getStatus() != TournamentStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Tournament is not pending approval");
        }
        tournament.setStatus(TournamentStatus.CANCELLED);
        return toResponse(tournamentRepository.save(tournament), null);
    }

    private Tournament findOrThrow(Long id) {
        return tournamentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tournament not found"));
    }

    private TournamentResponse toResponse(Tournament tournament, User currentUser) {
        int registeredCount = registrationRepository.countByTournamentId(tournament.getId());
        boolean isRegistered = currentUser != null
                && registrationRepository.existsByTournamentIdAndUserId(tournament.getId(), currentUser.getId());

        long daysLeft = daysUntil(tournament.getRegistrationDeadline());

        return TournamentResponse.builder()
                .id(tournament.getId())
                .name(tournament.getName())
                .sportName(tournament.getSport().getName())
                .sportId(tournament.getSport().getId())
                .format(tournament.getFormat())
                .entryUnit(tournament.getEntryUnit())
                .maxSlots(tournament.getMaxSlots())
                .registeredCount(registeredCount)
                .entryFee(tournament.getEntryFee())
                .prizePool(tournament.getPrizePool())
                .startsAt(tournament.getStartsAt())
                .daysLeft(Math.max(0, daysLeft))
                .status(tournament.getStatus().name())
                .isRegistered(isRegistered)
                .imageUrls(new ArrayList<>(tournament.getImageUrls()))
                .build();
    }

    private long daysUntil(Instant deadline) {
        LocalDate deadlineDate = deadline.atZone(ZoneOffset.UTC).toLocalDate();
        return LocalDate.now(ZoneOffset.UTC).until(deadlineDate).getDays();
    }
}
