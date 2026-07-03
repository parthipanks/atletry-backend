package com.atletry.dto.request;

import com.atletry.enums.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateAcademyRequest {

    @NotBlank
    private String name;
    private String tagline;
    private String description;
    private Integer foundedYear;
    private String city;
    private String state;
    private AcademyType academyType;
    private AcademySeason season;
    private List<Long> sportIds;
    private List<SkillTier> skillTiers;
    private List<AgeGroup> ageGroupsServed;
    private List<String> languages;
    private GenderPolicy genderPolicy;

    // Residential
    private Boolean isResidential;
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

    // Documents
    private BusinessRegType businessRegType;
    private String registrationNumber;
    private String gstin;
    private String panNumber;
    private Boolean pocsoCompliant;
    private Boolean hasInsurance;
    private Boolean hasSafetyCert;
    private String bankAccountFull;
    private String ifscCode;
    private String bankName;
    private String upiId;

    // Inline children (optional, for wizard single-shot submit)
    @Valid
    private List<AcademyBranchRequest> branches;
    @Valid
    private List<AcademyStaffRequest> staff;
    @Valid
    private List<AcademyBatchRequest> batches;
    @Valid
    private List<AcademyFeeComponentRequest> feeComponents;
    @Valid
    private List<AcademyScholarshipRequest> scholarships;
    @Valid
    private List<AcademyAdmissionStepRequest> admissionSteps;
    @Valid
    private List<AcademyNotableAlumniRequest> notableAlumni;
    @Valid
    private List<AcademyTeamAchievementRequest> teamAchievements;
    @Valid
    private List<AcademyAffiliationRequest> affiliations;
}
