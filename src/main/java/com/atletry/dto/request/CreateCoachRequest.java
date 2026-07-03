package com.atletry.dto.request;

import com.atletry.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
@Schema(description = "Register a new coach profile")
public class CreateCoachRequest {

    // ── Core profile ──────────────────────────────────────────────────────────
    @NotBlank @Size(min = 2, max = 100)
    private String fullName;

    @Size(max = 100)
    private String displayName;

    private LocalDate dateOfBirth;
    private Gender gender;
    private List<String> languages = new ArrayList<>();

    private int yearsOfExperience = 0;

    @Size(max = 200)
    private String bio;

    @Size(max = 200)
    private String tagline;

    @Size(max = 15)
    private String phone;

    private CoachingPhilosophy coachingPhilosophy;
    private String philosophyDescription;

    // ── Sports ────────────────────────────────────────────────────────────────
    private Long primarySportId;
    private List<Long> secondarySportIds = new ArrayList<>();
    private List<String> specializations = new ArrayList<>();
    private List<CoachLevel> levelsCoached = new ArrayList<>();
    private List<AgeGroup> ageGroupsCoached = new ArrayList<>();
    private boolean coachesFemaleAthletes = true;
    private boolean coachesMaleAthletes   = true;
    private boolean coachesMixedGroups    = true;

    // ── Training modes ────────────────────────────────────────────────────────
    private List<TrainingMode> trainingModes = new ArrayList<>();
    private Integer travelRadiusKm;
    private TravelChargesType travelChargesType;
    private BigDecimal travelChargePerKm;
    private BigDecimal fixedTravelCharge;
    private List<OnlinePlatform> onlinePlatforms = new ArrayList<>();

    // ── Background ────────────────────────────────────────────────────────────
    private HighestPlayingLevel highestPlayingLevel;
    private boolean hasFirstAidCert;
    private boolean hasCprCert;
    private boolean hasNutritionCert;
    private boolean hasSportsScienceCert;

    // ── Pricing ───────────────────────────────────────────────────────────────
    private String currency = "INR";
    private BigDecimal baseHourlyRate;
    private BigDecimal trialSessionPrice;
    private Integer trialSessionDurationMinutes;
    private BigDecimal groupSessionPriceMin;
    private BigDecimal groupSessionPriceMax;
    private boolean gstApplicable;
    private String gstin;
    private String panNumber;

    // ── Scheduling ────────────────────────────────────────────────────────────
    private Integer slotDurationMinutes;
    private Integer bufferMinutes;
    private Integer bookingLeadHours;
    private Integer advanceBookingDays;
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
    private boolean pocsoConsent;
    private boolean codeOfConductAccepted;

    // ── Nested collections (submit all at once from wizard or add later) ───────
    @Valid private List<AffiliatedVenueRequest> affiliatedVenues    = new ArrayList<>();
    @Valid private List<CoachAchievementRequest> achievements        = new ArrayList<>();
    @Valid private List<PressMentionRequest> pressMentions           = new ArrayList<>();
    @Valid private List<NotableStudentRequest> notableStudents       = new ArrayList<>();
    @Valid private List<CoachCertificationRequest> certifications    = new ArrayList<>();
    @Valid private List<CoachEducationRequest> educationQualifications = new ArrayList<>();
    @Valid private List<CoachProgramRequest> programs                = new ArrayList<>();
    @Valid private List<WeeklySlotRequest> weeklyAvailability        = new ArrayList<>();
    @Valid private List<MultiSessionDiscountRequest> multiSessionDiscounts = new ArrayList<>();
    @Valid private List<CoachReferenceRequest> references            = new ArrayList<>();
}
