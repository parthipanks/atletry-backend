package com.atletry.mapper;

import com.atletry.dto.response.*;
import com.atletry.entity.*;
import com.atletry.enums.AchievementType;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


@Component
public class CoachMapper {

    public CoachResponse toResponse(Coach c) {
        CoachResponse r = new CoachResponse();

        r.setId(c.getId());
        r.setOwnerId(c.getOwner() != null ? c.getOwner().getId() : null);

        r.setFullName(c.getFullName());
        r.setDisplayName(c.getDisplayName());
        r.setProfilePhotoUrl(c.getProfilePhotoUrl());
        r.setCoverPhotoUrl(c.getCoverPhotoUrl());
        r.setDateOfBirth(c.getDateOfBirth());
        r.setGender(c.getGender());
        r.setLanguages(new ArrayList<>(c.getLanguages()));
        r.setYearsOfExperience(c.getYearsOfExperience());
        r.setBio(c.getBio());
        r.setTagline(c.getTagline());
        r.setPhone(c.getPhone());
        r.setCoachingPhilosophy(c.getCoachingPhilosophy());
        r.setPhilosophyDescription(c.getPhilosophyDescription());

        r.setPrimarySportId(c.getPrimarySport() != null ? c.getPrimarySport().getId() : null);
        r.setPrimarySportName(c.getPrimarySport() != null ? c.getPrimarySport().getName() : null);
        if (c.getSecondarySports() != null) {
            r.setSecondarySportIds(c.getSecondarySports().stream().map(Sport::getId).collect(Collectors.toSet()));
            r.setSecondarySportNames(c.getSecondarySports().stream().map(Sport::getName).collect(Collectors.toSet()));
        }
        r.setSpecializations(new ArrayList<>(c.getSpecializations()));
        r.setLevelsCoached(new ArrayList<>(c.getLevelsCoached()));
        r.setAgeGroupsCoached(new ArrayList<>(c.getAgeGroupsCoached()));
        r.setCoachesFemaleAthletes(c.isCoachesFemaleAthletes());
        r.setCoachesMaleAthletes(c.isCoachesMaleAthletes());
        r.setCoachesMixedGroups(c.isCoachesMixedGroups());

        r.setTrainingModes(new ArrayList<>(c.getTrainingModes()));
        r.setTravelRadiusKm(c.getTravelRadiusKm());
        r.setTravelChargesType(c.getTravelChargesType());
        r.setTravelChargePerKm(c.getTravelChargePerKm());
        r.setFixedTravelCharge(c.getFixedTravelCharge());
        r.setOnlinePlatforms(new ArrayList<>(c.getOnlinePlatforms()));

        r.setHighestPlayingLevel(c.getHighestPlayingLevel());

        r.setHasFirstAidCert(c.isHasFirstAidCert());
        r.setHasCprCert(c.isHasCprCert());
        r.setHasNutritionCert(c.isHasNutritionCert());
        r.setHasSportsScienceCert(c.isHasSportsScienceCert());

        r.setCurrency(c.getCurrency());
        r.setBaseHourlyRate(c.getBaseHourlyRate());
        r.setTrialSessionPrice(c.getTrialSessionPrice());
        r.setTrialSessionDurationMinutes(c.getTrialSessionDurationMinutes());
        r.setGroupSessionPriceMin(c.getGroupSessionPriceMin());
        r.setGroupSessionPriceMax(c.getGroupSessionPriceMax());
        r.setGstApplicable(c.isGstApplicable());

        r.setSlotDurationMinutes(c.getSlotDurationMinutes());
        r.setBufferMinutes(c.getBufferMinutes());
        r.setBookingLeadHours(c.getBookingLeadHours());
        r.setAdvanceBookingDays(c.getAdvanceBookingDays());
        r.setOnVacation(c.isOnVacation());
        r.setVacationUntil(c.getVacationUntil());
        r.setMaxSessionsPerDay(c.getMaxSessionsPerDay());

        r.setCancellationPolicy(c.getCancellationPolicy());
        r.setFreeCancellationHours(c.getFreeCancellationHours());
        r.setNoShowPolicy(c.getNoShowPolicy());
        r.setLateArrivalGraceMinutes(c.getLateArrivalGraceMinutes());
        r.setRescheduleFreeBeforeHours(c.getRescheduleFreeBeforeHours());
        r.setRefundTimelineDays(c.getRefundTimelineDays());
        r.setCodeOfConduct(c.getCodeOfConduct());
        r.setPhotoVideoConsent(c.getPhotoVideoConsent());
        r.setParentalConsentRequired(c.isParentalConsentRequired());

        r.setBackgroundCheckStatus(c.getBackgroundCheckStatus());
        r.setBackgroundCheckCompletedAt(c.getBackgroundCheckCompletedAt());
        r.setPocsoConsent(c.isPocsoConsent());
        r.setCodeOfConductAccepted(c.isCodeOfConductAccepted());

        r.setApprovalStatus(c.getApprovalStatus());
        r.setPublished(c.isPublished());
        r.setActive(c.isActive());
        r.setWizardStep(c.getWizardStep());
        r.setWizardCompletion(c.getWizardCompletion());
        r.setApprovedAt(c.getApprovedAt());
        r.setRejectedAt(c.getRejectedAt());
        r.setSuspendedAt(c.getSuspendedAt());
        r.setVerifiedCoach(c.isVerifiedCoach());

        r.setAvgRating(c.getAvgRating());
        r.setReviewCount(c.getReviewCount());
        r.setAvgResponseMinutes(c.getAvgResponseMinutes());
        r.setResponseRate24hPct(c.getResponseRate24hPct());
        r.setTotalStudentsEverCoached(c.getTotalStudentsEverCoached());
        r.setActiveStudentCount(c.getActiveStudentCount());
        r.setTotalSessionsCompleted(c.getTotalSessionsCompleted());
        r.setStudentRetentionPct(c.getStudentRetentionPct());

        r.setCreatedDate(c.getCreatedDate());
        r.setUpdatedDate(c.getUpdatedDate());

        r.setAffiliatedVenues(mapList(c.getAffiliatedVenues(), this::toVenueResponse));
        r.setPlayerAchievements(c.getAchievements().stream()
                .filter(a -> a.getAchievementType() == AchievementType.PLAYER)
                .map(this::toAchievementResponse).collect(toList()));
        r.setCoachAchievements(c.getAchievements().stream()
                .filter(a -> a.getAchievementType() == AchievementType.COACH)
                .map(this::toAchievementResponse).collect(toList()));
        r.setPressMentions(mapList(c.getPressMentions(), this::toPressMentionResponse));
        r.setNotableStudents(mapList(c.getNotableStudents(), this::toNotableStudentResponse));
        r.setCertifications(mapList(c.getCertifications(), this::toCertificationResponse));
        r.setEducationQualifications(mapList(c.getEducationQualifications(), this::toEducationResponse));
        r.setPrograms(mapList(c.getPrograms(), this::toProgramResponse));
        r.setWeeklyAvailability(mapList(c.getWeeklyAvailability(), this::toWeeklySlotResponse));
        r.setBlockedDates(mapList(c.getBlockedDates(), this::toBlockedDateResponse));
        r.setMultiSessionDiscounts(mapList(c.getMultiSessionDiscounts(), this::toDiscountResponse));
        r.setReferences(mapList(c.getReferences(), this::toReferenceResponse));
        r.setVerificationLog(mapList(c.getVerificationLog(), this::toVerificationLogResponse));

        return r;
    }

    public List<CoachResponse> toResponseList(List<Coach> coaches) {
        return coaches.stream().map(this::toResponse).collect(toList());
    }

    public UserCoachResponse toUserCoachResponse(UserCoach uc) {
        UserCoachResponse r = new UserCoachResponse();
        r.setId(uc.getId());
        Coach c = uc.getCoach();
        r.setCoachId(c.getId());
        r.setCoachFullName(c.getFullName());
        r.setCoachDisplayName(c.getDisplayName());
        r.setCoachBio(c.getBio());
        r.setCoachPhone(c.getPhone());
        r.setCoachYearsOfExperience(c.getYearsOfExperience());
        r.setCoachPrimarySportId(c.getPrimarySport() != null ? c.getPrimarySport().getId() : null);
        r.setCoachPrimarySportName(c.getPrimarySport() != null ? c.getPrimarySport().getName() : null);
        r.setCoachProfilePhotoUrl(c.getProfilePhotoUrl());
        r.setCoachBaseHourlyRate(c.getBaseHourlyRate());
        r.setCoachApprovalStatus(c.getApprovalStatus());
        r.setAssignedAt(uc.getCreatedDate());
        return r;
    }

    public List<UserCoachResponse> toUserCoachResponseList(List<UserCoach> list) {
        return list.stream().map(this::toUserCoachResponse).collect(toList());
    }

    // ── Sub-mappers ───────────────────────────────────────────────────────────

    public CoachProgramResponse toProgramResponse(CoachProgram p) {
        return CoachProgramResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .format(p.getFormat())
                .description(p.getDescription())
                .durationMinutes(p.getDurationMinutes())
                .capacity(p.getCapacity())
                .skillLevels(new ArrayList<>(p.getSkillLevels()))
                .ageGroups(new ArrayList<>(p.getAgeGroups()))
                .sportId(p.getSport() != null ? p.getSport().getId() : null)
                .sportName(p.getSport() != null ? p.getSport().getName() : null)
                .pricePerUnit(p.getPricePerUnit())
                .frequency(p.getFrequency())
                .inclusions(new ArrayList<>(p.getInclusions()))
                .exclusions(new ArrayList<>(p.getExclusions()))
                .totalSessions(p.getTotalSessions())
                .campStartDate(p.getCampStartDate())
                .campEndDate(p.getCampEndDate())
                .isTrial(p.isTrial())
                .firstTimeDiscountPct(p.getFirstTimeDiscountPct())
                .cancellationPolicy(p.getCancellationPolicy())
                .photoUrls(new ArrayList<>(p.getPhotoUrls()))
                .isActive(p.isActive())
                .displayOrder(p.getDisplayOrder())
                .build();
    }

    private CoachAffiliatedVenueResponse toVenueResponse(CoachAffiliatedVenue v) {
        return CoachAffiliatedVenueResponse.builder()
                .id(v.getId()).venueRefId(v.getVenueRefId()).name(v.getName())
                .address(v.getAddress()).area(v.getArea()).city(v.getCity())
                .latitude(v.getLatitude()).longitude(v.getLongitude())
                .relationship(v.getRelationship()).isPrimary(v.isPrimary())
                .build();
    }

    private CoachAchievementResponse toAchievementResponse(CoachAchievement a) {
        return CoachAchievementResponse.builder()
                .id(a.getId()).achievementType(a.getAchievementType()).title(a.getTitle())
                .organization(a.getOrganization()).year(a.getYear()).description(a.getDescription())
                .proofUri(a.getProofUri()).proofType(a.getProofType()).isVerified(a.isVerified())
                .build();
    }

    private CoachPressMentionResponse toPressMentionResponse(CoachPressMention m) {
        return CoachPressMentionResponse.builder()
                .id(m.getId()).title(m.getTitle()).publication(m.getPublication())
                .url(m.getUrl()).date(m.getDate())
                .build();
    }

    private CoachNotableStudentResponse toNotableStudentResponse(CoachNotableStudent s) {
        return CoachNotableStudentResponse.builder()
                .id(s.getId()).name(s.getName()).achievement(s.getAchievement())
                .year(s.getYear()).consent(s.isConsent())
                .build();
    }

    private CoachCertificationResponse toCertificationResponse(CoachCertification c) {
        return CoachCertificationResponse.builder()
                .id(c.getId()).catalogId(c.getCatalogId()).name(c.getName())
                .organization(c.getOrganization()).level(c.getLevel())
                .yearObtained(c.getYearObtained()).expiresAt(c.getExpiresAt())
                .certificateNumber(c.getCertificateNumber()).documentUri(c.getDocumentUri())
                .isVerified(c.isVerified()).verifiedAt(c.getVerifiedAt())
                .build();
    }

    private CoachEducationResponse toEducationResponse(CoachEducation e) {
        return CoachEducationResponse.builder()
                .id(e.getId()).qualification(e.getQualification())
                .institution(e.getInstitution()).year(e.getYear()).documentUri(e.getDocumentUri())
                .build();
    }

    private CoachWeeklySlotResponse toWeeklySlotResponse(CoachWeeklySlot s) {
        return CoachWeeklySlotResponse.builder()
                .id(s.getId()).dayOfWeek(s.getDayOfWeek())
                .isAvailable(s.isAvailable()).startTime(s.getStartTime()).endTime(s.getEndTime())
                .build();
    }

    private CoachBlockedDateResponse toBlockedDateResponse(CoachBlockedDate d) {
        return CoachBlockedDateResponse.builder()
                .id(d.getId()).date(d.getDate()).reason(d.getReason())
                .build();
    }

    private CoachMultiSessionDiscountResponse toDiscountResponse(CoachMultiSessionDiscount d) {
        return CoachMultiSessionDiscountResponse.builder()
                .id(d.getId()).minSessions(d.getMinSessions()).discountPct(d.getDiscountPct())
                .build();
    }

    private CoachReferenceResponse toReferenceResponse(CoachReference ref) {
        return CoachReferenceResponse.builder()
                .id(ref.getId()).name(ref.getName()).relationship(ref.getRelationship())
                .phone(ref.getPhone()).email(ref.getEmail())
                .isVerified(ref.isVerified()).verifiedAt(ref.getVerifiedAt())
                .build();
    }

    private CoachVerificationLogResponse toVerificationLogResponse(CoachVerificationLog l) {
        return CoachVerificationLogResponse.builder()
                .id(l.getId()).stage(l.getStage()).note(l.getNote())
                .timestamp(l.getTimestamp()).actorType(l.getActorType()).actorName(l.getActorName())
                .build();
    }

    private <T, R> List<R> mapList(List<T> source, java.util.function.Function<T, R> mapper) {
        if (source == null) return Collections.emptyList();
        return source.stream().map(mapper).collect(toList());
    }
}
