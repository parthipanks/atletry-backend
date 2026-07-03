package com.atletry.service;

import com.atletry.dto.request.*;
import com.atletry.dto.response.CoachBlockedDateResponse;
import com.atletry.dto.response.CoachProgramResponse;
import com.atletry.dto.response.CoachResponse;
import com.atletry.dto.response.CoachWeeklySlotResponse;
import com.atletry.dto.response.UserCoachResponse;
import com.atletry.entity.*;
import com.atletry.enums.*;
import com.atletry.exception.BadRequestException;
import com.atletry.exception.ResourceNotFoundException;
import com.atletry.mapper.CoachMapper;
import com.atletry.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CoachService {

    private final CoachRepository              coachRepo;
    private final CoachProgramRepository       programRepo;
    private final CoachWeeklySlotRepository    slotRepo;
    private final CoachBlockedDateRepository   blockedDateRepo;
    private final CoachVerificationLogRepository verificationLogRepo;
    private final UserCoachRepository          userCoachRepo;
    private final CoachMapper                  coachMapper;
    private final SportService                 sportService;
    private final PushNotificationService      pushNotificationService;
    private final MediaUploadService           mediaUploadService;

    // ── Browse ────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<CoachResponse> getAllActive() {
        return coachMapper.toResponseList(
                coachRepo.findByApprovalStatusAndIsActiveTrueOrderByCreatedDateDesc(ApprovalStatus.APPROVED));
    }

    @Transactional(readOnly = true)
    public List<CoachResponse> getPendingApproval() {
        return coachMapper.toResponseList(
                coachRepo.findByApprovalStatusOrderByCreatedDateDesc(ApprovalStatus.PENDING_APPROVAL));
    }

    @Transactional(readOnly = true)
    public CoachResponse getById(Long id) {
        return coachMapper.toResponse(findOrThrow(id));
    }

    // ── Approval ──────────────────────────────────────────────────────────────

    @Transactional
    public CoachResponse approve(Long id) {
        Coach coach = findOrThrow(id);
        if (coach.getApprovalStatus() != ApprovalStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Coach is not pending approval");
        }
        coach.setApprovalStatus(ApprovalStatus.APPROVED);
        coach.setApprovedAt(Instant.now());
        appendVerificationLog(coach, VerificationStage.APPROVED, "Approved", ActorType.ADMIN, "Admin");
        return coachMapper.toResponse(coachRepo.save(coach));
    }

    @Transactional
    public CoachResponse reject(Long id) {
        Coach coach = findOrThrow(id);
        if (coach.getApprovalStatus() != ApprovalStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Coach is not pending approval");
        }
        coach.setApprovalStatus(ApprovalStatus.REJECTED);
        coach.setRejectedAt(Instant.now());
        appendVerificationLog(coach, VerificationStage.REJECTED, "Rejected", ActorType.ADMIN, "Admin");
        return coachMapper.toResponse(coachRepo.save(coach));
    }

    // ── Create / Update / Delete ───────────────────────────────────────────────

    @Transactional
    public CoachResponse create(CreateCoachRequest req, List<MultipartFile> images, User owner) {
        Set<Sport> secondarySports = resolveSecondarySports(req.getSecondarySportIds());

        Coach coach = Coach.builder()
                .owner(owner)
                .fullName(req.getFullName())
                .displayName(req.getDisplayName())
                .dateOfBirth(req.getDateOfBirth())
                .gender(req.getGender())
                .languages(req.getLanguages())
                .yearsOfExperience(req.getYearsOfExperience())
                .bio(req.getBio())
                .tagline(req.getTagline())
                .phone(req.getPhone())
                .coachingPhilosophy(req.getCoachingPhilosophy())
                .philosophyDescription(req.getPhilosophyDescription())
                .primarySport(req.getPrimarySportId() != null ? sportService.findOrThrow(req.getPrimarySportId()) : null)
                .secondarySports(secondarySports)
                .specializations(req.getSpecializations())
                .levelsCoached(req.getLevelsCoached())
                .ageGroupsCoached(req.getAgeGroupsCoached())
                .coachesFemaleAthletes(req.isCoachesFemaleAthletes())
                .coachesMaleAthletes(req.isCoachesMaleAthletes())
                .coachesMixedGroups(req.isCoachesMixedGroups())
                .trainingModes(req.getTrainingModes())
                .travelRadiusKm(req.getTravelRadiusKm())
                .travelChargesType(req.getTravelChargesType())
                .travelChargePerKm(req.getTravelChargePerKm())
                .fixedTravelCharge(req.getFixedTravelCharge())
                .onlinePlatforms(req.getOnlinePlatforms())
                .highestPlayingLevel(req.getHighestPlayingLevel())
                .hasFirstAidCert(req.isHasFirstAidCert())
                .hasCprCert(req.isHasCprCert())
                .hasNutritionCert(req.isHasNutritionCert())
                .hasSportsScienceCert(req.isHasSportsScienceCert())
                .currency(req.getCurrency() != null ? req.getCurrency() : "INR")
                .baseHourlyRate(req.getBaseHourlyRate())
                .trialSessionPrice(req.getTrialSessionPrice())
                .trialSessionDurationMinutes(req.getTrialSessionDurationMinutes())
                .groupSessionPriceMin(req.getGroupSessionPriceMin())
                .groupSessionPriceMax(req.getGroupSessionPriceMax())
                .gstApplicable(req.isGstApplicable())
                .gstin(req.getGstin())
                .panNumber(req.getPanNumber())
                .slotDurationMinutes(req.getSlotDurationMinutes())
                .bufferMinutes(req.getBufferMinutes())
                .bookingLeadHours(req.getBookingLeadHours())
                .advanceBookingDays(req.getAdvanceBookingDays())
                .maxSessionsPerDay(req.getMaxSessionsPerDay())
                .cancellationPolicy(req.getCancellationPolicy())
                .freeCancellationHours(req.getFreeCancellationHours())
                .noShowPolicy(req.getNoShowPolicy())
                .lateArrivalGraceMinutes(req.getLateArrivalGraceMinutes())
                .rescheduleFreeBeforeHours(req.getRescheduleFreeBeforeHours())
                .refundTimelineDays(req.getRefundTimelineDays())
                .codeOfConduct(req.getCodeOfConduct())
                .photoVideoConsent(req.getPhotoVideoConsent())
                .parentalConsentRequired(req.isParentalConsentRequired())
                .pocsoConsent(req.isPocsoConsent())
                .pocsoConsentAt(req.isPocsoConsent() ? Instant.now() : null)
                .codeOfConductAccepted(req.isCodeOfConductAccepted())
                .codeOfConductAcceptedAt(req.isCodeOfConductAccepted() ? Instant.now() : null)
                .build();

        coach = coachRepo.save(coach);

        // Upload profile/cover photos
        if (images != null && !images.isEmpty()) {
            List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.COACH, coach.getId(), owner);
            if (!urls.isEmpty()) coach.setProfilePhotoUrl(urls.get(0));
            if (urls.size() > 1) coach.setCoverPhotoUrl(urls.get(1));
        }

        // Nested collections from wizard
        applyAffiliatedVenues(coach, req.getAffiliatedVenues());
        applyAchievements(coach, req.getAchievements());
        applyPressMentions(coach, req.getPressMentions());
        applyNotableStudents(coach, req.getNotableStudents());
        applyCertifications(coach, req.getCertifications());
        applyEducation(coach, req.getEducationQualifications());
        applyPrograms(coach, req.getPrograms());
        applyWeeklyAvailability(coach, req.getWeeklyAvailability());
        applyMultiSessionDiscounts(coach, req.getMultiSessionDiscounts());
        applyReferences(coach, req.getReferences());

        appendVerificationLog(coach, VerificationStage.SUBMITTED, "Coach profile submitted", ActorType.COACH, coach.getFullName());
        Coach saved = coachRepo.save(coach);

        pushNotificationService.sendBroadcast(NotificationEventType.COACH_ADDED, Map.of(
                "name",      saved.getFullName(),
                "sportName", saved.getPrimarySport() != null ? saved.getPrimarySport().getName() : ""
        ));

        return coachMapper.toResponse(saved);
    }

    @Transactional
    public CoachResponse update(Long id, UpdateCoachRequest req, List<MultipartFile> images, User owner) {
        Coach coach = findOrThrow(id);
        requireOwner(coach, owner);

        if (req.getFullName()               != null) coach.setFullName(req.getFullName());
        if (req.getDisplayName()            != null) coach.setDisplayName(req.getDisplayName());
        if (req.getDateOfBirth()            != null) coach.setDateOfBirth(req.getDateOfBirth());
        if (req.getGender()                 != null) coach.setGender(req.getGender());
        if (req.getLanguages()              != null) coach.setLanguages(req.getLanguages());
        if (req.getYearsOfExperience()      != null) coach.setYearsOfExperience(req.getYearsOfExperience());
        if (req.getBio()                    != null) coach.setBio(req.getBio());
        if (req.getTagline()                != null) coach.setTagline(req.getTagline());
        if (req.getPhone()                  != null) coach.setPhone(req.getPhone());
        if (req.getCoachingPhilosophy()     != null) coach.setCoachingPhilosophy(req.getCoachingPhilosophy());
        if (req.getPhilosophyDescription()  != null) coach.setPhilosophyDescription(req.getPhilosophyDescription());
        if (req.getPrimarySportId()         != null) coach.setPrimarySport(sportService.findOrThrow(req.getPrimarySportId()));
        if (req.getSecondarySportIds()      != null) coach.setSecondarySports(resolveSecondarySports(req.getSecondarySportIds()));
        if (req.getSpecializations()        != null) coach.setSpecializations(req.getSpecializations());
        if (req.getLevelsCoached()          != null) coach.setLevelsCoached(req.getLevelsCoached());
        if (req.getAgeGroupsCoached()       != null) coach.setAgeGroupsCoached(req.getAgeGroupsCoached());
        if (req.getCoachesFemaleAthletes()  != null) coach.setCoachesFemaleAthletes(req.getCoachesFemaleAthletes());
        if (req.getCoachesMaleAthletes()    != null) coach.setCoachesMaleAthletes(req.getCoachesMaleAthletes());
        if (req.getCoachesMixedGroups()     != null) coach.setCoachesMixedGroups(req.getCoachesMixedGroups());
        if (req.getTrainingModes()          != null) coach.setTrainingModes(req.getTrainingModes());
        if (req.getTravelRadiusKm()         != null) coach.setTravelRadiusKm(req.getTravelRadiusKm());
        if (req.getTravelChargesType()      != null) coach.setTravelChargesType(req.getTravelChargesType());
        if (req.getTravelChargePerKm()      != null) coach.setTravelChargePerKm(req.getTravelChargePerKm());
        if (req.getFixedTravelCharge()      != null) coach.setFixedTravelCharge(req.getFixedTravelCharge());
        if (req.getOnlinePlatforms()        != null) coach.setOnlinePlatforms(req.getOnlinePlatforms());
        if (req.getHighestPlayingLevel()    != null) coach.setHighestPlayingLevel(req.getHighestPlayingLevel());
        if (req.getHasFirstAidCert()        != null) coach.setHasFirstAidCert(req.getHasFirstAidCert());
        if (req.getHasCprCert()             != null) coach.setHasCprCert(req.getHasCprCert());
        if (req.getHasNutritionCert()       != null) coach.setHasNutritionCert(req.getHasNutritionCert());
        if (req.getHasSportsScienceCert()   != null) coach.setHasSportsScienceCert(req.getHasSportsScienceCert());
        if (req.getCurrency()               != null) coach.setCurrency(req.getCurrency());
        if (req.getBaseHourlyRate()         != null) coach.setBaseHourlyRate(req.getBaseHourlyRate());
        if (req.getTrialSessionPrice()      != null) coach.setTrialSessionPrice(req.getTrialSessionPrice());
        if (req.getTrialSessionDurationMinutes() != null) coach.setTrialSessionDurationMinutes(req.getTrialSessionDurationMinutes());
        if (req.getGroupSessionPriceMin()   != null) coach.setGroupSessionPriceMin(req.getGroupSessionPriceMin());
        if (req.getGroupSessionPriceMax()   != null) coach.setGroupSessionPriceMax(req.getGroupSessionPriceMax());
        if (req.getGstApplicable()          != null) coach.setGstApplicable(req.getGstApplicable());
        if (req.getGstin()                  != null) coach.setGstin(req.getGstin());
        if (req.getPanNumber()              != null) coach.setPanNumber(req.getPanNumber());
        if (req.getSlotDurationMinutes()    != null) coach.setSlotDurationMinutes(req.getSlotDurationMinutes());
        if (req.getBufferMinutes()          != null) coach.setBufferMinutes(req.getBufferMinutes());
        if (req.getBookingLeadHours()       != null) coach.setBookingLeadHours(req.getBookingLeadHours());
        if (req.getAdvanceBookingDays()     != null) coach.setAdvanceBookingDays(req.getAdvanceBookingDays());
        if (req.getMaxSessionsPerDay()      != null) coach.setMaxSessionsPerDay(req.getMaxSessionsPerDay());
        if (req.getIsOnVacation()           != null) coach.setOnVacation(req.getIsOnVacation());
        if (req.getVacationUntil()          != null) coach.setVacationUntil(req.getVacationUntil());
        if (req.getCancellationPolicy()     != null) coach.setCancellationPolicy(req.getCancellationPolicy());
        if (req.getFreeCancellationHours()  != null) coach.setFreeCancellationHours(req.getFreeCancellationHours());
        if (req.getNoShowPolicy()           != null) coach.setNoShowPolicy(req.getNoShowPolicy());
        if (req.getLateArrivalGraceMinutes() != null) coach.setLateArrivalGraceMinutes(req.getLateArrivalGraceMinutes());
        if (req.getRescheduleFreeBeforeHours() != null) coach.setRescheduleFreeBeforeHours(req.getRescheduleFreeBeforeHours());
        if (req.getRefundTimelineDays()     != null) coach.setRefundTimelineDays(req.getRefundTimelineDays());
        if (req.getCodeOfConduct()          != null) coach.setCodeOfConduct(req.getCodeOfConduct());
        if (req.getPhotoVideoConsent()      != null) coach.setPhotoVideoConsent(req.getPhotoVideoConsent());
        if (req.getParentalConsentRequired() != null) coach.setParentalConsentRequired(req.getParentalConsentRequired());
        if (req.getPocsoConsent()           != null && req.getPocsoConsent() && !coach.isPocsoConsent()) {
            coach.setPocsoConsent(true);
            coach.setPocsoConsentAt(Instant.now());
        }
        if (req.getCodeOfConductAccepted()  != null && req.getCodeOfConductAccepted() && !coach.isCodeOfConductAccepted()) {
            coach.setCodeOfConductAccepted(true);
            coach.setCodeOfConductAcceptedAt(Instant.now());
        }
        if (req.getIsActive()               != null) coach.setActive(req.getIsActive());
        if (req.getIsPublished()            != null) coach.setPublished(req.getIsPublished());
        if (req.getWizardStep()             != null) coach.setWizardStep(req.getWizardStep());
        if (req.getWizardCompletion()       != null) coach.setWizardCompletion(req.getWizardCompletion());

        if (images != null && !images.isEmpty()) {
            List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.COACH, coach.getId(), owner);
            if (!urls.isEmpty()) coach.setProfilePhotoUrl(urls.get(0));
            if (urls.size() > 1) coach.setCoverPhotoUrl(urls.get(1));
        }

        return coachMapper.toResponse(coachRepo.save(coach));
    }

    @Transactional
    public void delete(Long id) {
        Coach coach = findOrThrow(id);
        coach.setActive(false);
        coachRepo.save(coach);
    }

    // ── Programs ──────────────────────────────────────────────────────────────

    @Transactional
    public CoachProgramResponse addProgram(Long coachId, CoachProgramRequest req,
                                           List<MultipartFile> images, User user) {
        Coach coach = findOrThrow(coachId);
        requireOwner(coach, user);

        CoachProgram program = buildProgram(coach, req);
        coach.getPrograms().add(program);
        coachRepo.save(coach);

        CoachProgram saved = coach.getPrograms().get(coach.getPrograms().size() - 1);

        if (images != null && !images.isEmpty()) {
            List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.COACH_PROGRAM, saved.getId(), user);
            saved.getPhotoUrls().addAll(urls);
            programRepo.save(saved);
        }

        return coachMapper.toProgramResponse(saved);
    }

    @Transactional
    public CoachProgramResponse updateProgram(Long coachId, Long programId,
                                              CoachProgramRequest req,
                                              List<MultipartFile> images, User user) {
        Coach coach = findOrThrow(coachId);
        requireOwner(coach, user);

        CoachProgram program = programRepo.findByIdAndCoachId(programId, coachId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));

        if (req.getName()                 != null) program.setName(req.getName());
        if (req.getFormat()               != null) program.setFormat(req.getFormat());
        if (req.getDescription()          != null) program.setDescription(req.getDescription());
        if (req.getDurationMinutes()      != null) program.setDurationMinutes(req.getDurationMinutes());
        if (req.getCapacity()             != null) program.setCapacity(req.getCapacity());
        if (req.getSkillLevels()          != null) program.setSkillLevels(req.getSkillLevels());
        if (req.getAgeGroups()            != null) program.setAgeGroups(req.getAgeGroups());
        if (req.getSportId()              != null) program.setSport(sportService.findOrThrow(req.getSportId()));
        if (req.getPricePerUnit()         != null) program.setPricePerUnit(req.getPricePerUnit());
        if (req.getFrequency()            != null) program.setFrequency(req.getFrequency());
        if (req.getInclusions()           != null) program.setInclusions(req.getInclusions());
        if (req.getExclusions()           != null) program.setExclusions(req.getExclusions());
        if (req.getTotalSessions()        != null) program.setTotalSessions(req.getTotalSessions());
        if (req.getCampStartDate()        != null) program.setCampStartDate(req.getCampStartDate());
        if (req.getCampEndDate()          != null) program.setCampEndDate(req.getCampEndDate());
        if (req.getFirstTimeDiscountPct() != null) program.setFirstTimeDiscountPct(req.getFirstTimeDiscountPct());
        if (req.getCancellationPolicy()   != null) program.setCancellationPolicy(req.getCancellationPolicy());
        program.setTrial(req.isTrial());
        program.setActive(req.isActive());
        program.setDisplayOrder(req.getDisplayOrder());

        if (images != null && !images.isEmpty()) {
            List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.COACH_PROGRAM, program.getId(), user);
            program.getPhotoUrls().addAll(urls);
        }

        return coachMapper.toProgramResponse(programRepo.save(program));
    }

    @Transactional
    public void removeProgram(Long coachId, Long programId, User user) {
        Coach coach = findOrThrow(coachId);
        requireOwner(coach, user);
        CoachProgram program = programRepo.findByIdAndCoachId(programId, coachId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));
        program.setActive(false);
        programRepo.save(program);
    }

    // ── Weekly availability ───────────────────────────────────────────────────

    @Transactional
    public List<CoachWeeklySlotResponse> setWeeklyAvailability(Long coachId,
                                                               List<WeeklySlotRequest> slots,
                                                               User user) {
        Coach coach = findOrThrow(coachId);
        requireOwner(coach, user);
        applyWeeklyAvailability(coach, slots);
        coachRepo.save(coach);
        return slotRepo.findByCoachIdOrderByDayOfWeek(coachId).stream()
                .map(s -> CoachWeeklySlotResponse.builder()
                        .id(s.getId()).dayOfWeek(s.getDayOfWeek())
                        .isAvailable(s.isAvailable()).startTime(s.getStartTime()).endTime(s.getEndTime())
                        .build())
                .collect(Collectors.toList());
    }

    // ── Blocked dates ─────────────────────────────────────────────────────────

    @Transactional
    public CoachBlockedDateResponse addBlockedDate(Long coachId, BlockedDateRequest req, User user) {
        Coach coach = findOrThrow(coachId);
        requireOwner(coach, user);
        CoachBlockedDate blocked = CoachBlockedDate.builder()
                .coach(coach).date(req.getDate()).reason(req.getReason()).build();
        coach.getBlockedDates().add(blocked);
        coachRepo.save(coach);
        CoachBlockedDate saved = coach.getBlockedDates().get(coach.getBlockedDates().size() - 1);
        return CoachBlockedDateResponse.builder().id(saved.getId()).date(saved.getDate()).reason(saved.getReason()).build();
    }

    @Transactional
    public void removeBlockedDate(Long coachId, Long dateId, User user) {
        Coach coach = findOrThrow(coachId);
        requireOwner(coach, user);
        CoachBlockedDate blocked = blockedDateRepo.findByIdAndCoachId(dateId, coachId)
                .orElseThrow(() -> new ResourceNotFoundException("Blocked date not found"));
        blockedDateRepo.delete(blocked);
    }

    // ── User–coach assignments ────────────────────────────────────────────────

    @Transactional
    public UserCoachResponse assignUser(Long coachId, User user) {
        if (userCoachRepo.existsByUserIdAndCoachId(user.getId(), coachId)) {
            throw new BadRequestException("You are already assigned to this coach.");
        }
        Coach coach = findOrThrow(coachId);
        UserCoach userCoach = UserCoach.builder().user(user).coach(coach).build();
        return coachMapper.toUserCoachResponse(userCoachRepo.save(userCoach));
    }

    @Transactional
    public void unassignUser(Long coachId, User user) {
        UserCoach userCoach = userCoachRepo.findByUserIdAndCoachId(user.getId(), coachId)
                .orElseThrow(() -> new ResourceNotFoundException("No assignment found for this coach."));
        userCoachRepo.delete(userCoach);
    }

    @Transactional(readOnly = true)
    public List<UserCoachResponse> getMyCoaches(User user) {
        return coachMapper.toUserCoachResponseList(
                userCoachRepo.findByUserIdOrderByCreatedDateDesc(user.getId()));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    public Coach findOrThrow(Long id) {
        return coachRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Coach not found: " + id));
    }

    private void requireOwner(Coach coach, User user) {
        if (coach.getOwner() == null || !coach.getOwner().getId().equals(user.getId())) {
            throw new BadRequestException("Only the coach owner can perform this action.");
        }
    }

    private Set<Sport> resolveSecondarySports(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        return ids.stream().map(sportService::findOrThrow).collect(Collectors.toSet());
    }

    private void applyAffiliatedVenues(Coach coach, List<AffiliatedVenueRequest> reqs) {
        if (reqs == null) return;
        coach.getAffiliatedVenues().clear();
        for (AffiliatedVenueRequest r : reqs) {
            coach.getAffiliatedVenues().add(CoachAffiliatedVenue.builder()
                    .coach(coach).venueRefId(r.getVenueRefId()).name(r.getName())
                    .address(r.getAddress()).area(r.getArea()).city(r.getCity())
                    .latitude(r.getLatitude()).longitude(r.getLongitude())
                    .relationship(r.getRelationship()).isPrimary(r.isPrimary())
                    .build());
        }
    }

    private void applyAchievements(Coach coach, List<CoachAchievementRequest> reqs) {
        if (reqs == null) return;
        coach.getAchievements().clear();
        for (CoachAchievementRequest r : reqs) {
            coach.getAchievements().add(CoachAchievement.builder()
                    .coach(coach).achievementType(r.getAchievementType()).title(r.getTitle())
                    .organization(r.getOrganization()).year(r.getYear()).description(r.getDescription())
                    .proofUri(r.getProofUri()).proofType(r.getProofType())
                    .build());
        }
    }

    private void applyPressMentions(Coach coach, List<PressMentionRequest> reqs) {
        if (reqs == null) return;
        coach.getPressMentions().clear();
        for (PressMentionRequest r : reqs) {
            coach.getPressMentions().add(CoachPressMention.builder()
                    .coach(coach).title(r.getTitle()).publication(r.getPublication())
                    .url(r.getUrl()).date(r.getDate())
                    .build());
        }
    }

    private void applyNotableStudents(Coach coach, List<NotableStudentRequest> reqs) {
        if (reqs == null) return;
        coach.getNotableStudents().clear();
        for (NotableStudentRequest r : reqs) {
            coach.getNotableStudents().add(CoachNotableStudent.builder()
                    .coach(coach).name(r.getName()).achievement(r.getAchievement())
                    .year(r.getYear()).consent(r.isConsent())
                    .build());
        }
    }

    private void applyCertifications(Coach coach, List<CoachCertificationRequest> reqs) {
        if (reqs == null) return;
        coach.getCertifications().clear();
        for (CoachCertificationRequest r : reqs) {
            coach.getCertifications().add(CoachCertification.builder()
                    .coach(coach).catalogId(r.getCatalogId()).name(r.getName())
                    .organization(r.getOrganization()).level(r.getLevel())
                    .yearObtained(r.getYearObtained()).expiresAt(r.getExpiresAt())
                    .certificateNumber(r.getCertificateNumber()).documentUri(r.getDocumentUri())
                    .build());
        }
    }

    private void applyEducation(Coach coach, List<CoachEducationRequest> reqs) {
        if (reqs == null) return;
        coach.getEducationQualifications().clear();
        for (CoachEducationRequest r : reqs) {
            coach.getEducationQualifications().add(CoachEducation.builder()
                    .coach(coach).qualification(r.getQualification()).institution(r.getInstitution())
                    .year(r.getYear()).documentUri(r.getDocumentUri())
                    .build());
        }
    }

    private void applyPrograms(Coach coach, List<CoachProgramRequest> reqs) {
        if (reqs == null) return;
        coach.getPrograms().clear();
        for (CoachProgramRequest r : reqs) {
            coach.getPrograms().add(buildProgram(coach, r));
        }
    }

    private CoachProgram buildProgram(Coach coach, CoachProgramRequest r) {
        return CoachProgram.builder()
                .coach(coach).name(r.getName()).format(r.getFormat()).description(r.getDescription())
                .durationMinutes(r.getDurationMinutes()).capacity(r.getCapacity())
                .skillLevels(r.getSkillLevels()).ageGroups(r.getAgeGroups())
                .sport(r.getSportId() != null ? sportService.findOrThrow(r.getSportId()) : null)
                .pricePerUnit(r.getPricePerUnit()).frequency(r.getFrequency())
                .inclusions(r.getInclusions()).exclusions(r.getExclusions())
                .totalSessions(r.getTotalSessions()).campStartDate(r.getCampStartDate()).campEndDate(r.getCampEndDate())
                .isTrial(r.isTrial()).firstTimeDiscountPct(r.getFirstTimeDiscountPct())
                .cancellationPolicy(r.getCancellationPolicy())
                .isActive(r.isActive()).displayOrder(r.getDisplayOrder())
                .build();
    }

    private void applyWeeklyAvailability(Coach coach, List<WeeklySlotRequest> reqs) {
        if (reqs == null) return;
        for (WeeklySlotRequest r : reqs) {
            CoachWeeklySlot slot = slotRepo.findByCoachIdAndDayOfWeek(coach.getId(), r.getDayOfWeek())
                    .orElseGet(() -> CoachWeeklySlot.builder().coach(coach).dayOfWeek(r.getDayOfWeek()).build());
            slot.setAvailable(r.isAvailable());
            slot.setStartTime(r.getStartTime());
            slot.setEndTime(r.getEndTime());
            slotRepo.save(slot);
        }
    }

    private void applyMultiSessionDiscounts(Coach coach, List<MultiSessionDiscountRequest> reqs) {
        if (reqs == null) return;
        coach.getMultiSessionDiscounts().clear();
        for (MultiSessionDiscountRequest r : reqs) {
            coach.getMultiSessionDiscounts().add(CoachMultiSessionDiscount.builder()
                    .coach(coach).minSessions(r.getMinSessions()).discountPct(r.getDiscountPct())
                    .build());
        }
    }

    private void applyReferences(Coach coach, List<CoachReferenceRequest> reqs) {
        if (reqs == null) return;
        coach.getReferences().clear();
        for (CoachReferenceRequest r : reqs) {
            coach.getReferences().add(CoachReference.builder()
                    .coach(coach).name(r.getName()).relationship(r.getRelationship())
                    .phone(r.getPhone()).email(r.getEmail())
                    .build());
        }
    }

    private void appendVerificationLog(Coach coach, VerificationStage stage,
                                       String note, ActorType actorType, String actorName) {
        coach.getVerificationLog().add(CoachVerificationLog.builder()
                .coach(coach).stage(stage).note(note)
                .timestamp(Instant.now()).actorType(actorType).actorName(actorName)
                .build());
    }
}
