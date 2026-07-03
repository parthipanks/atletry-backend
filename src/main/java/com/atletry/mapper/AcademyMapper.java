package com.atletry.mapper;

import com.atletry.dto.response.*;
import com.atletry.entity.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class AcademyMapper {

    public AcademyResponse toResponse(Academy a) {
        AcademyResponse r = new AcademyResponse();

        r.setId(a.getId());
        r.setOwnerId(a.getOwner() != null ? a.getOwner().getId() : null);
        r.setOwnerName(a.getOwner() != null ? a.getOwner().getName() : null);

        r.setName(a.getName());
        r.setTagline(a.getTagline());
        r.setDescription(a.getDescription());
        r.setLogoUri(a.getLogoUri());
        r.setCoverPhotoUrl(a.getCoverPhotoUrl());
        r.setFoundedYear(a.getFoundedYear());
        r.setCity(a.getCity());
        r.setState(a.getState());
        r.setAcademyType(a.getAcademyType());
        r.setSeason(a.getSeason());

        if (a.getSportsOffered() != null) {
            r.setSportIds(a.getSportsOffered().stream().map(Sport::getId).collect(Collectors.toList()));
            r.setSportNames(a.getSportsOffered().stream().map(Sport::getName).collect(Collectors.toList()));
        }
        r.setSkillTiers(new ArrayList<>(a.getSkillTiers()));
        r.setAgeGroupsServed(new ArrayList<>(a.getAgeGroupsServed()));
        r.setLanguages(new ArrayList<>(a.getLanguages()));
        r.setGenderPolicy(a.getGenderPolicy());

        r.setResidential(a.isResidential());
        r.setResidentialCapacity(a.getResidentialCapacity());
        r.setMessType(a.getMessType());
        r.setResidentialAgeGroups(new ArrayList<>(a.getResidentialAgeGroups()));
        r.setResidentialAmenities(new ArrayList<>(a.getResidentialAmenities()));

        if (a.getContactInfo() != null) {
            AcademyContactInfo ci = a.getContactInfo();
            r.setContactPersonName(ci.getContactPersonName());
            r.setContactDesignation(ci.getDesignation());
            r.setContactPhone(ci.getPhone());
            r.setContactEmail(ci.getEmail());
            r.setContactWhatsapp(ci.getWhatsapp());
            r.setWebsiteUrl(ci.getWebsiteUrl());
            r.setInstagramHandle(ci.getInstagramHandle());
        }

        if (a.getDocuments() != null) {
            AcademyDocuments d = a.getDocuments();
            r.setBusinessRegType(d.getBusinessRegType());
            r.setRegistrationNumber(d.getRegistrationNumber());
            r.setGstin(d.getGstin());
            r.setPanNumber(d.getPanNumber());
            r.setPocsoCompliant(d.isPocsoCompliant());
            r.setHasInsurance(d.isHasInsurance());
            r.setHasSafetyCert(d.isHasSafetyCert());
            r.setBankName(d.getBankName());
            r.setUpiId(d.getUpiId());
        }

        r.setApprovalStatus(a.getApprovalStatus());
        r.setPublished(a.isPublished());
        r.setActive(a.isActive());
        r.setWizardStep(a.getWizardStep());
        r.setApprovedAt(a.getApprovedAt());
        r.setRejectedAt(a.getRejectedAt());
        r.setSuspendedAt(a.getSuspendedAt());
        r.setCreatedAt(a.getCreatedAt());
        r.setUpdatedAt(a.getUpdatedAt());

        r.setAvgRating(a.getAvgRating());
        r.setReviewCount(a.getReviewCount());
        r.setTotalStudentsEnrolled(a.getTotalStudentsEnrolled());

        r.setBranches(mapList(a.getBranches(), this::toBranchResponse));
        r.setStaff(mapList(a.getStaff(), this::toStaffResponse));
        r.setBatches(mapList(a.getBatches(), this::toBatchResponse));
        r.setFeeComponents(mapList(a.getFeeComponents(), this::toFeeComponentResponse));
        r.setScholarships(mapList(a.getScholarships(), this::toScholarshipResponse));
        r.setAdmissionSteps(mapList(a.getAdmissionSteps(), this::toAdmissionStepResponse));
        r.setNotableAlumni(mapList(a.getNotableAlumni(), this::toNotableAlumniResponse));
        r.setTeamAchievements(mapList(a.getTeamAchievements(), this::toTeamAchievementResponse));
        r.setAffiliations(mapList(a.getAffiliations(), this::toAffiliationResponse));
        r.setVerificationLog(mapList(a.getVerificationLog(), this::toVerificationLogResponse));

        return r;
    }

    private AcademyBranchResponse toBranchResponse(AcademyBranch b) {
        AcademyBranchResponse r = new AcademyBranchResponse();
        r.setId(b.getId());
        r.setName(b.getName());
        r.setAddress(b.getAddress());
        r.setArea(b.getArea());
        r.setCity(b.getCity());
        r.setPincode(b.getPincode());
        r.setLatitude(b.getLatitude());
        r.setLongitude(b.getLongitude());
        r.setFacilities(new ArrayList<>(b.getFacilities()));
        r.setResidential(b.isResidential());
        r.setPrimary(b.isPrimary());
        r.setLinkedVenueId(b.getLinkedVenue() != null ? b.getLinkedVenue().getId() : null);
        r.setPhotoUrls(new ArrayList<>(b.getPhotoUrls()));
        r.setSports(new ArrayList<>(b.getSports()));
        r.setActive(b.isActive());
        return r;
    }

    private AcademyStaffResponse toStaffResponse(AcademyStaffMember s) {
        AcademyStaffResponse r = new AcademyStaffResponse();
        r.setId(s.getId());
        r.setName(s.getName());
        r.setRole(s.getRole());
        r.setPhotoUri(s.getPhotoUri());
        r.setSports(new ArrayList<>(s.getSports()));
        r.setYearsOfExperience(s.getYearsOfExperience());
        r.setCertifications(new ArrayList<>(s.getCertifications()));
        r.setPlayingBackground(s.getPlayingBackground());
        r.setLinkedCoachId(s.getLinkedCoach() != null ? s.getLinkedCoach().getId() : null);
        r.setHeadCoach(s.isHeadCoach());
        r.setBio(s.getBio());
        return r;
    }

    private AcademyBatchResponse toBatchResponse(AcademyBatch b) {
        AcademyBatchResponse r = new AcademyBatchResponse();
        r.setId(b.getId());
        r.setName(b.getName());
        r.setSportId(b.getSport() != null ? b.getSport().getId() : null);
        r.setSportName(b.getSport() != null ? b.getSport().getName() : null);
        r.setSkillTier(b.getSkillTier());
        r.setAgeGroups(new ArrayList<>(b.getAgeGroups()));
        r.setDays(new ArrayList<>(b.getDays()));
        r.setStartTime(b.getStartTime());
        r.setEndTime(b.getEndTime());
        r.setCapacity(b.getCapacity());
        r.setEnrolledCount(b.getEnrolledCount());
        r.setBranchId(b.getBranch() != null ? b.getBranch().getId() : null);
        r.setBranchName(b.getBranch() != null ? b.getBranch().getName() : null);
        r.setAssignedStaffId(b.getAssignedStaff() != null ? b.getAssignedStaff().getId() : null);
        r.setAssignedStaffName(b.getAssignedStaff() != null ? b.getAssignedStaff().getName() : null);
        r.setFee(b.getFee());
        r.setFeeFrequency(b.getFeeFrequency());
        r.setOpen(b.isOpen());
        r.setGenderPolicy(b.getGenderPolicy());
        r.setDescription(b.getDescription());
        return r;
    }

    private AcademyFeeComponentResponse toFeeComponentResponse(AcademyFeeComponent f) {
        AcademyFeeComponentResponse r = new AcademyFeeComponentResponse();
        r.setId(f.getId());
        r.setLabel(f.getLabel());
        r.setAmount(f.getAmount());
        r.setFrequency(f.getFrequency());
        r.setMandatory(f.isMandatory());
        r.setRefundable(f.isRefundable());
        r.setNotes(f.getNotes());
        return r;
    }

    private AcademyScholarshipResponse toScholarshipResponse(AcademyScholarship s) {
        AcademyScholarshipResponse r = new AcademyScholarshipResponse();
        r.setId(s.getId());
        r.setName(s.getName());
        r.setDescription(s.getDescription());
        r.setDiscountPct(s.getDiscountPct());
        r.setFixedAmount(s.getFixedAmount());
        r.setEligibility(s.getEligibility());
        return r;
    }

    private AcademyAdmissionStepResponse toAdmissionStepResponse(AcademyAdmissionStep s) {
        AcademyAdmissionStepResponse r = new AcademyAdmissionStepResponse();
        r.setId(s.getId());
        r.setType(s.getType());
        r.setLabel(s.getLabel());
        r.setFree(s.isFree());
        r.setFee(s.getFee());
        r.setSortOrder(s.getSortOrder());
        return r;
    }

    private AcademyNotableAlumniResponse toNotableAlumniResponse(AcademyNotableAlumni a) {
        AcademyNotableAlumniResponse r = new AcademyNotableAlumniResponse();
        r.setId(a.getId());
        r.setName(a.getName());
        r.setAchievement(a.getAchievement());
        r.setYear(a.getYear());
        r.setSport(a.getSport());
        r.setPhotoUri(a.getPhotoUri());
        r.setHasConsent(a.isConsent());
        return r;
    }

    private AcademyTeamAchievementResponse toTeamAchievementResponse(AcademyTeamAchievement a) {
        AcademyTeamAchievementResponse r = new AcademyTeamAchievementResponse();
        r.setId(a.getId());
        r.setTitle(a.getTitle());
        r.setOrganization(a.getOrganization());
        r.setYear(a.getYear());
        r.setSport(a.getSport());
        r.setProofUri(a.getProofUri());
        return r;
    }

    private AcademyAffiliationResponse toAffiliationResponse(AcademyAffiliation a) {
        AcademyAffiliationResponse r = new AcademyAffiliationResponse();
        r.setId(a.getId());
        r.setBody(a.getBody());
        r.setLabel(a.getLabel());
        r.setRegistrationNumber(a.getRegistrationNumber());
        r.setDocumentUri(a.getDocumentUri());
        r.setVerified(a.isVerified());
        return r;
    }

    private AcademyVerificationLogResponse toVerificationLogResponse(AcademyVerificationLog l) {
        AcademyVerificationLogResponse r = new AcademyVerificationLogResponse();
        r.setId(l.getId());
        r.setStage(l.getStage());
        r.setNote(l.getNote());
        r.setTimestamp(l.getTimestamp());
        r.setActorType(l.getActorType());
        r.setActorName(l.getActorName());
        return r;
    }

    private <T, R> List<R> mapList(List<T> list, java.util.function.Function<T, R> mapper) {
        if (list == null) return Collections.emptyList();
        return list.stream().map(mapper).collect(Collectors.toList());
    }
}
