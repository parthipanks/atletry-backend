package com.atletry.dto.response;

import com.atletry.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;


@Data
public class CoachResponse {

    // ── Identity ──────────────────────────────────────────────────────────────
    private Long   id;
    private Long   ownerId;

    // ── Core profile ──────────────────────────────────────────────────────────
    private String fullName;
    private String displayName;
    private String profilePhotoUrl;
    private String coverPhotoUrl;
    private LocalDate dateOfBirth;
    private Gender gender;
    private List<String> languages;
    private int yearsOfExperience;
    private String bio;
    private String tagline;
    private String phone;
    private CoachingPhilosophy coachingPhilosophy;
    private String philosophyDescription;

    // ── Sports & specialisation ───────────────────────────────────────────────
    private Long   primarySportId;
    private String primarySportName;
    private Set<Long>   secondarySportIds;
    private Set<String> secondarySportNames;
    private List<String> specializations;
    private List<CoachLevel> levelsCoached;
    private List<AgeGroup> ageGroupsCoached;
    private boolean coachesFemaleAthletes;
    private boolean coachesMaleAthletes;
    private boolean coachesMixedGroups;

    // ── Training modes & locations ────────────────────────────────────────────
    private List<TrainingMode> trainingModes;
    private Integer travelRadiusKm;
    private TravelChargesType travelChargesType;
    private BigDecimal travelChargePerKm;
    private BigDecimal fixedTravelCharge;
    private List<OnlinePlatform> onlinePlatforms;

    // ── Playing background ────────────────────────────────────────────────────
    private HighestPlayingLevel highestPlayingLevel;

    // ── Credentials ───────────────────────────────────────────────────────────
    private boolean hasFirstAidCert;
    private boolean hasCprCert;
    private boolean hasNutritionCert;
    private boolean hasSportsScienceCert;

    // ── Pricing ───────────────────────────────────────────────────────────────
    private String currency;
    private BigDecimal baseHourlyRate;
    private BigDecimal trialSessionPrice;
    private Integer trialSessionDurationMinutes;
    private BigDecimal groupSessionPriceMin;
    private BigDecimal groupSessionPriceMax;
    private boolean gstApplicable;

    // ── Scheduling ────────────────────────────────────────────────────────────
    private Integer slotDurationMinutes;
    private Integer bufferMinutes;
    private Integer bookingLeadHours;
    private Integer advanceBookingDays;
    private boolean isOnVacation;
    private LocalDate vacationUntil;
    private Integer maxSessionsPerDay;

    // ── Policies ──────────────────────────────────────────────────────────────
    private CancellationPolicy cancellationPolicy;
    private Integer freeCancellationHours;
    private NoShowPolicy noShowPolicy;
    private Integer lateArrivalGraceMinutes;
    private Integer rescheduleFreeBeforeHours;
    private Integer refundTimelineDays;
    private String codeOfConduct;
    private PhotoVideoConsent photoVideoConsent;
    private boolean parentalConsentRequired;

    // ── Trust & safety ────────────────────────────────────────────────────────
    private BackgroundCheckStatus backgroundCheckStatus;
    private Instant backgroundCheckCompletedAt;
    private boolean pocsoConsent;
    private boolean codeOfConductAccepted;

    // ── Status ────────────────────────────────────────────────────────────────
    private ApprovalStatus approvalStatus;
    private boolean isPublished;
    private boolean isActive;
    private int wizardStep;
    private int wizardCompletion;
    private Instant approvedAt;
    private Instant rejectedAt;
    private Instant suspendedAt;
    private boolean isVerifiedCoach;

    // ── Analytics ────────────────────────────────────────────────────────────
    private BigDecimal avgRating;
    private int reviewCount;
    private Integer avgResponseMinutes;
    private Integer responseRate24hPct;
    private int totalStudentsEverCoached;
    private int activeStudentCount;
    private int totalSessionsCompleted;
    private Integer studentRetentionPct;

    // ── Timestamps ────────────────────────────────────────────────────────────
    private ZonedDateTime createdDate;
    private ZonedDateTime updatedDate;

    // ── Nested collections ───────────────────────────────────────────────────
    private List<CoachAffiliatedVenueResponse>  affiliatedVenues;
    private List<CoachAchievementResponse>       playerAchievements;
    private List<CoachAchievementResponse>       coachAchievements;
    private List<CoachPressMentionResponse>      pressMentions;
    private List<CoachNotableStudentResponse>    notableStudents;
    private List<CoachCertificationResponse>     certifications;
    private List<CoachEducationResponse>         educationQualifications;
    private List<CoachProgramResponse>           programs;
    private List<CoachWeeklySlotResponse>        weeklyAvailability;
    private List<CoachBlockedDateResponse>       blockedDates;
    private List<CoachMultiSessionDiscountResponse> multiSessionDiscounts;
    private List<CoachReferenceResponse>         references;
    private List<CoachVerificationLogResponse>   verificationLog;
}
