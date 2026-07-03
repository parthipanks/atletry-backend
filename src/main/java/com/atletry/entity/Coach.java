package com.atletry.entity;

import com.atletry.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "coaches")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Coach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ── Ownership ─────────────────────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    // ── Core profile ──────────────────────────────────────────────────────────

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "profile_photo_url", length = 500)
    private String profilePhotoUrl;

    @Column(name = "cover_photo_url", length = 500)
    private String coverPhotoUrl;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private Gender gender;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_languages", joinColumns = @JoinColumn(name = "coach_id"))
    @Column(name = "language", length = 50)
    @Builder.Default
    private List<String> languages = new ArrayList<>();

    @Column(name = "years_of_experience")
    @Builder.Default
    private int yearsOfExperience = 0;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(length = 200)
    private String tagline;

    @Column(length = 15)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "coaching_philosophy", length = 30)
    private CoachingPhilosophy coachingPhilosophy;

    @Column(name = "philosophy_description", columnDefinition = "TEXT")
    private String philosophyDescription;

    // ── Sports & specialisation ───────────────────────────────────────────────

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "primary_sport_id")
    private Sport primarySport;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "coach_secondary_sports",
               joinColumns = @JoinColumn(name = "coach_id"),
               inverseJoinColumns = @JoinColumn(name = "sport_id"))
    @Builder.Default
    private Set<Sport> secondarySports = new HashSet<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_specializations", joinColumns = @JoinColumn(name = "coach_id"))
    @Column(name = "specialization", length = 100)
    @Builder.Default
    private List<String> specializations = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_levels_coached", joinColumns = @JoinColumn(name = "coach_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "level", length = 20)
    @Builder.Default
    private List<CoachLevel> levelsCoached = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_age_groups_coached", joinColumns = @JoinColumn(name = "coach_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", length = 20)
    @Builder.Default
    private List<AgeGroup> ageGroupsCoached = new ArrayList<>();

    @Column(name = "coaches_female_athletes")
    @Builder.Default
    private boolean coachesFemaleAthletes = true;

    @Column(name = "coaches_male_athletes")
    @Builder.Default
    private boolean coachesMaleAthletes = true;

    @Column(name = "coaches_mixed_groups")
    @Builder.Default
    private boolean coachesMixedGroups = true;

    // ── Training modes & locations ────────────────────────────────────────────

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_training_modes", joinColumns = @JoinColumn(name = "coach_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "mode", length = 25)
    @Builder.Default
    private List<TrainingMode> trainingModes = new ArrayList<>();

    @Column(name = "travel_radius_km")
    private Integer travelRadiusKm;

    @Enumerated(EnumType.STRING)
    @Column(name = "travel_charges_type", length = 20)
    private TravelChargesType travelChargesType;

    @Column(name = "travel_charge_per_km", precision = 10, scale = 2)
    private BigDecimal travelChargePerKm;

    @Column(name = "fixed_travel_charge", precision = 10, scale = 2)
    private BigDecimal fixedTravelCharge;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_online_platforms", joinColumns = @JoinColumn(name = "coach_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "platform", length = 25)
    @Builder.Default
    private List<OnlinePlatform> onlinePlatforms = new ArrayList<>();

    // ── Playing background ────────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(name = "highest_playing_level", length = 20)
    private HighestPlayingLevel highestPlayingLevel;

    // ── Credentials ───────────────────────────────────────────────────────────

    @Column(name = "has_first_aid_cert")
    @Builder.Default
    private boolean hasFirstAidCert = false;

    @Column(name = "has_cpr_cert")
    @Builder.Default
    private boolean hasCprCert = false;

    @Column(name = "has_nutrition_cert")
    @Builder.Default
    private boolean hasNutritionCert = false;

    @Column(name = "has_sports_science_cert")
    @Builder.Default
    private boolean hasSportsScienceCert = false;

    // ── Pricing ───────────────────────────────────────────────────────────────

    @Column(length = 10)
    @Builder.Default
    private String currency = "INR";

    @Column(name = "base_hourly_rate", precision = 10, scale = 2)
    private BigDecimal baseHourlyRate;

    @Column(name = "trial_session_price", precision = 10, scale = 2)
    private BigDecimal trialSessionPrice;

    @Column(name = "trial_session_duration_minutes")
    private Integer trialSessionDurationMinutes;

    @Column(name = "group_session_price_min", precision = 10, scale = 2)
    private BigDecimal groupSessionPriceMin;

    @Column(name = "group_session_price_max", precision = 10, scale = 2)
    private BigDecimal groupSessionPriceMax;

    @Column(name = "gst_applicable")
    @Builder.Default
    private boolean gstApplicable = false;

    @Column(length = 20)
    private String gstin;

    @Column(name = "pan_number", length = 20)
    private String panNumber;

    // ── Scheduling ────────────────────────────────────────────────────────────

    @Column(name = "slot_duration_minutes")
    private Integer slotDurationMinutes;

    @Column(name = "buffer_minutes")
    private Integer bufferMinutes;

    @Column(name = "booking_lead_hours")
    private Integer bookingLeadHours;

    @Column(name = "advance_booking_days")
    private Integer advanceBookingDays;

    @Column(name = "is_on_vacation")
    @Builder.Default
    private boolean isOnVacation = false;

    @Column(name = "vacation_until")
    private LocalDate vacationUntil;

    @Column(name = "max_sessions_per_day")
    private Integer maxSessionsPerDay;

    // ── Policies ──────────────────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(name = "cancellation_policy", length = 20)
    private CancellationPolicy cancellationPolicy;

    @Column(name = "free_cancellation_hours")
    private Integer freeCancellationHours;

    @Enumerated(EnumType.STRING)
    @Column(name = "no_show_policy", length = 25)
    private NoShowPolicy noShowPolicy;

    @Column(name = "late_arrival_grace_minutes")
    private Integer lateArrivalGraceMinutes;

    @Column(name = "reschedule_free_before_hours")
    private Integer rescheduleFreeBeforeHours;

    @Column(name = "refund_timeline_days")
    private Integer refundTimelineDays;

    @Column(name = "code_of_conduct", columnDefinition = "TEXT")
    private String codeOfConduct;

    @Enumerated(EnumType.STRING)
    @Column(name = "photo_video_consent", length = 20)
    private PhotoVideoConsent photoVideoConsent;

    @Column(name = "parental_consent_required")
    @Builder.Default
    private boolean parentalConsentRequired = false;

    // ── Trust & safety ────────────────────────────────────────────────────────

    @Column(name = "pocso_consent")
    @Builder.Default
    private boolean pocsoConsent = false;

    @Column(name = "pocso_consent_at")
    private Instant pocsoConsentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "background_check_status", length = 20)
    @Builder.Default
    private BackgroundCheckStatus backgroundCheckStatus = BackgroundCheckStatus.NOT_STARTED;

    @Column(name = "background_check_completed_at")
    private Instant backgroundCheckCompletedAt;

    @Column(name = "code_of_conduct_accepted")
    @Builder.Default
    private boolean codeOfConductAccepted = false;

    @Column(name = "code_of_conduct_accepted_at")
    private Instant codeOfConductAcceptedAt;

    // ── Status & publishing ───────────────────────────────────────────────────

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false, length = 20)
    @Builder.Default
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_APPROVAL;

    @Column(name = "is_published")
    @Builder.Default
    private boolean isPublished = false;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "wizard_step")
    @Builder.Default
    private int wizardStep = 1;

    @Column(name = "wizard_completion")
    @Builder.Default
    private int wizardCompletion = 0;

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "rejected_at")
    private Instant rejectedAt;

    @Column(name = "suspended_at")
    private Instant suspendedAt;

    @Column(name = "verification_notes", columnDefinition = "TEXT")
    private String verificationNotes;

    @Column(name = "is_verified_coach")
    @Builder.Default
    private boolean isVerifiedCoach = false;

    // ── Analytics (read-only, updated by background jobs) ─────────────────────

    @Column(name = "avg_rating", precision = 3, scale = 2)
    private BigDecimal avgRating;

    @Column(name = "review_count")
    @Builder.Default
    private int reviewCount = 0;

    @Column(name = "avg_response_minutes")
    private Integer avgResponseMinutes;

    @Column(name = "response_rate_24h_pct")
    private Integer responseRate24hPct;

    @Column(name = "total_students_ever_coached")
    @Builder.Default
    private int totalStudentsEverCoached = 0;

    @Column(name = "active_student_count")
    @Builder.Default
    private int activeStudentCount = 0;

    @Column(name = "total_sessions_completed")
    @Builder.Default
    private int totalSessionsCompleted = 0;

    @Column(name = "student_retention_pct")
    private Integer studentRetentionPct;

    // ── Embedded sensitive data ───────────────────────────────────────────────

    @Embedded
    @Builder.Default
    private CoachBankingDetails bankingDetails = new CoachBankingDetails();

    @Embedded
    @Builder.Default
    private CoachDocuments documents = new CoachDocuments();

    // ── Timestamps ────────────────────────────────────────────────────────────

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    // ── Child collections (cascade) ───────────────────────────────────────────

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CoachAffiliatedVenue> affiliatedVenues = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CoachAchievement> achievements = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CoachPressMention> pressMentions = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CoachNotableStudent> notableStudents = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CoachCertification> certifications = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CoachEducation> educationQualifications = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<CoachProgram> programs = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayOfWeek ASC")
    @Builder.Default
    private List<CoachWeeklySlot> weeklyAvailability = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("date ASC")
    @Builder.Default
    private List<CoachBlockedDate> blockedDates = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("minSessions ASC")
    @Builder.Default
    private List<CoachMultiSessionDiscount> multiSessionDiscounts = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CoachReference> references = new ArrayList<>();

    @OneToMany(mappedBy = "coach", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timestamp ASC")
    @Builder.Default
    private List<CoachVerificationLog> verificationLog = new ArrayList<>();
}
