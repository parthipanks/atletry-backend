package com.atletry.dto.response;

import com.atletry.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
public class AcademyResponse {

    private Long id;
    private Long ownerId;
    private String ownerName;

    // Core profile
    private String name;
    private String tagline;
    private String description;
    private String logoUri;
    private String coverPhotoUrl;
    private Integer foundedYear;
    private String city;
    private String state;
    private AcademyType academyType;
    private AcademySeason season;

    // Sports & tiers
    private List<Long> sportIds;
    private List<String> sportNames;
    private List<SkillTier> skillTiers;
    private List<AgeGroup> ageGroupsServed;
    private List<String> languages;
    private GenderPolicy genderPolicy;

    // Residential
    private boolean isResidential;
    private Integer residentialCapacity;
    private MessType messType;
    private List<AgeGroup> residentialAgeGroups;
    private List<String> residentialAmenities;

    // Contact info
    private String contactPersonName;
    private String contactDesignation;
    private String contactPhone;
    private String contactEmail;
    private String contactWhatsapp;
    private String websiteUrl;
    private String instagramHandle;

    // Documents (admin-only sensitive fields omitted in non-admin mappings)
    private BusinessRegType businessRegType;
    private String registrationNumber;
    private String gstin;
    private String panNumber;
    private boolean pocsoCompliant;
    private boolean hasInsurance;
    private boolean hasSafetyCert;
    private String bankName;
    private String upiId;

    // Status
    private ApprovalStatus approvalStatus;
    private boolean isPublished;
    private boolean isActive;
    private int wizardStep;
    private Instant approvedAt;
    private Instant rejectedAt;
    private Instant suspendedAt;
    private Instant createdAt;
    private Instant updatedAt;

    // Analytics
    private BigDecimal avgRating;
    private int reviewCount;
    private int totalStudentsEnrolled;

    // Children
    private List<AcademyBranchResponse> branches;
    private List<AcademyStaffResponse> staff;
    private List<AcademyBatchResponse> batches;
    private List<AcademyFeeComponentResponse> feeComponents;
    private List<AcademyScholarshipResponse> scholarships;
    private List<AcademyAdmissionStepResponse> admissionSteps;
    private List<AcademyNotableAlumniResponse> notableAlumni;
    private List<AcademyTeamAchievementResponse> teamAchievements;
    private List<AcademyAffiliationResponse> affiliations;
    private List<AcademyVerificationLogResponse> verificationLog;
}
