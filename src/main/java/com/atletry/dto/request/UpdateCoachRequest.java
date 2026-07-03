package com.atletry.dto.request;

import com.atletry.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
@Schema(description = "Partial update for a coach profile — only provided fields are changed")
public class UpdateCoachRequest {

    @Size(min = 2, max = 100) private String fullName;
    @Size(max = 100)          private String displayName;
    private LocalDate         dateOfBirth;
    private Gender            gender;
    private List<String>      languages;

    private Integer           yearsOfExperience;
    private String            bio;
    @Size(max = 200)
    private String            tagline;
    @Size(max = 15)
    private String            phone;
    private CoachingPhilosophy coachingPhilosophy;
    private String            philosophyDescription;

    private Long              primarySportId;
    private List<Long>        secondarySportIds;
    private List<String>      specializations;
    private List<CoachLevel>  levelsCoached;
    private List<AgeGroup>    ageGroupsCoached;
    private Boolean           coachesFemaleAthletes;
    private Boolean           coachesMaleAthletes;
    private Boolean           coachesMixedGroups;

    private List<TrainingMode>  trainingModes;
    private Integer             travelRadiusKm;
    private TravelChargesType   travelChargesType;
    private BigDecimal          travelChargePerKm;
    private BigDecimal          fixedTravelCharge;
    private List<OnlinePlatform> onlinePlatforms;

    private HighestPlayingLevel highestPlayingLevel;
    private Boolean hasFirstAidCert;
    private Boolean hasCprCert;
    private Boolean hasNutritionCert;
    private Boolean hasSportsScienceCert;

    private String     currency;
    private BigDecimal baseHourlyRate;
    private BigDecimal trialSessionPrice;
    private Integer    trialSessionDurationMinutes;
    private BigDecimal groupSessionPriceMin;
    private BigDecimal groupSessionPriceMax;
    private Boolean    gstApplicable;
    private String     gstin;
    private String     panNumber;

    private Integer slotDurationMinutes;
    private Integer bufferMinutes;
    private Integer bookingLeadHours;
    private Integer advanceBookingDays;
    private Integer maxSessionsPerDay;
    private Boolean isOnVacation;
    private LocalDate vacationUntil;

    private CancellationPolicy cancellationPolicy;
    private Integer            freeCancellationHours;
    private NoShowPolicy       noShowPolicy;
    private Integer            lateArrivalGraceMinutes;
    private Integer            rescheduleFreeBeforeHours;
    private Integer            refundTimelineDays;
    private String             codeOfConduct;
    private PhotoVideoConsent  photoVideoConsent;
    private Boolean            parentalConsentRequired;

    private Boolean pocsoConsent;
    private Boolean codeOfConductAccepted;

    private Boolean  isActive;
    private Boolean  isPublished;
    private Integer  wizardStep;
    private Integer  wizardCompletion;
}
