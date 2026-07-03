package com.atletry.entity;

import com.atletry.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "academies")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Academy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    // Core profile
    @Column(nullable = false, length = 200)
    private String name;

    @Column(length = 300)
    private String tagline;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "logo_uri", length = 500)
    private String logoUri;

    @Column(name = "cover_photo_url", length = 500)
    private String coverPhotoUrl;

    @Column(name = "founded_year")
    private Integer foundedYear;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "state", length = 100)
    private String state;

    // Type & season
    @Enumerated(EnumType.STRING)
    @Column(name = "academy_type", length = 30)
    private AcademyType academyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", length = 20)
    private AcademySeason season;

    // Sports offered
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "academy_sports",
        joinColumns = @JoinColumn(name = "academy_id"),
        inverseJoinColumns = @JoinColumn(name = "sport_id")
    )
    @Builder.Default
    private List<Sport> sportsOffered = new ArrayList<>();

    // Skill tiers
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_skill_tiers", joinColumns = @JoinColumn(name = "academy_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_tier", length = 20)
    @Builder.Default
    private List<SkillTier> skillTiers = new ArrayList<>();

    // Age groups
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_age_groups", joinColumns = @JoinColumn(name = "academy_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", length = 20)
    @Builder.Default
    private List<AgeGroup> ageGroupsServed = new ArrayList<>();

    // Languages
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_languages", joinColumns = @JoinColumn(name = "academy_id"))
    @Column(name = "language", length = 50)
    @Builder.Default
    private List<String> languages = new ArrayList<>();

    // Residential
    @Column(name = "is_residential")
    @Builder.Default
    private boolean isResidential = false;

    @Column(name = "residential_capacity")
    private Integer residentialCapacity;

    @Enumerated(EnumType.STRING)
    @Column(name = "mess_type", length = 15)
    private MessType messType;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_residential_age_groups", joinColumns = @JoinColumn(name = "academy_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", length = 20)
    @Builder.Default
    private List<AgeGroup> residentialAgeGroups = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_residential_amenities", joinColumns = @JoinColumn(name = "academy_id"))
    @Column(name = "amenity", length = 100)
    @Builder.Default
    private List<String> residentialAmenities = new ArrayList<>();

    // Gender policy
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_policy", length = 10)
    private GenderPolicy genderPolicy;

    // Status
    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", length = 20)
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

    @Column(name = "approved_at")
    private Instant approvedAt;

    @Column(name = "rejected_at")
    private Instant rejectedAt;

    @Column(name = "suspended_at")
    private Instant suspendedAt;

    // Analytics
    @Column(name = "avg_rating", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal avgRating = BigDecimal.ZERO;

    @Column(name = "review_count")
    @Builder.Default
    private int reviewCount = 0;

    @Column(name = "total_students_enrolled")
    @Builder.Default
    private int totalStudentsEnrolled = 0;

    // Embedded
    @Embedded
    @Builder.Default
    private AcademyContactInfo contactInfo = new AcademyContactInfo();

    @Embedded
    @Builder.Default
    private AcademyDocuments documents = new AcademyDocuments();

    // Timestamps
    @Column(name = "created_at", nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @Builder.Default
    private Instant updatedAt = Instant.now();

    // Children
    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("isPrimary DESC, id ASC")
    @Builder.Default
    private List<AcademyBranch> branches = new ArrayList<>();

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("isHeadCoach DESC, id ASC")
    @Builder.Default
    private List<AcademyStaffMember> staff = new ArrayList<>();

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private List<AcademyBatch> batches = new ArrayList<>();

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private List<AcademyFeeComponent> feeComponents = new ArrayList<>();

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private List<AcademyScholarship> scholarships = new ArrayList<>();

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortOrder ASC")
    @Builder.Default
    private List<AcademyAdmissionStep> admissionSteps = new ArrayList<>();

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("year DESC, id ASC")
    @Builder.Default
    private List<AcademyNotableAlumni> notableAlumni = new ArrayList<>();

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("year DESC, id ASC")
    @Builder.Default
    private List<AcademyTeamAchievement> teamAchievements = new ArrayList<>();

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    @Builder.Default
    private List<AcademyAffiliation> affiliations = new ArrayList<>();

    @OneToMany(mappedBy = "academy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("timestamp ASC")
    @Builder.Default
    private List<AcademyVerificationLog> verificationLog = new ArrayList<>();
}
