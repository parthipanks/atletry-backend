package com.atletry.dto.request;

import com.atletry.enums.*;
import lombok.Data;

import java.util.List;

@Data
public class UpdateAcademyRequest {

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
}
