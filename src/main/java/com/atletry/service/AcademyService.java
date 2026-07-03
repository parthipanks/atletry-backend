package com.atletry.service;

import com.atletry.dto.request.*;
import com.atletry.dto.response.AcademyResponse;
import com.atletry.entity.*;
import com.atletry.enums.*;
import com.atletry.exception.BadRequestException;
import com.atletry.exception.ResourceNotFoundException;
import com.atletry.mapper.AcademyMapper;
import com.atletry.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AcademyService {

    private final AcademyRepository             academyRepo;
    private final AcademyBranchRepository       branchRepo;
    private final AcademyStaffRepository        staffRepo;
    private final AcademyBatchRepository        batchRepo;
    private final AcademyVerificationLogRepository verificationLogRepo;
    private final SportRepository               sportRepo;
    private final GroundRepository              groundRepo;
    private final CoachRepository               coachRepo;
    private final AcademyMapper                 academyMapper;
    private final MediaUploadService            mediaUploadService;
    private final PushNotificationService       pushNotificationService;

    // ── Browse ────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<AcademyResponse> getAllPublished() {
        return academyRepo.findByIsPublishedTrueAndIsActiveTrueOrderByCreatedAtDesc()
                .stream().map(academyMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AcademyResponse> getPending() {
        return academyRepo.findByApprovalStatusOrderByCreatedAtDesc(ApprovalStatus.PENDING_APPROVAL)
                .stream().map(academyMapper::toResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AcademyResponse getById(Long id) {
        return academyMapper.toResponse(findOrThrow(id));
    }

    @Transactional(readOnly = true)
    public List<AcademyResponse> getMyAcademies(User owner) {
        return academyRepo.findByOwnerIdOrderByCreatedAtDesc(owner.getId())
                .stream().map(academyMapper::toResponse).collect(Collectors.toList());
    }

    // ── Create / Update / Delete ───────────────────────────────────────────────

    @Transactional
    public AcademyResponse create(CreateAcademyRequest req, List<MultipartFile> images, User owner) {
        Academy academy = Academy.builder()
                .owner(owner)
                .name(req.getName())
                .tagline(req.getTagline())
                .description(req.getDescription())
                .foundedYear(req.getFoundedYear())
                .city(req.getCity())
                .state(req.getState())
                .academyType(req.getAcademyType())
                .season(req.getSeason())
                .skillTiers(req.getSkillTiers() != null ? req.getSkillTiers() : new ArrayList<>())
                .ageGroupsServed(req.getAgeGroupsServed() != null ? req.getAgeGroupsServed() : new ArrayList<>())
                .languages(req.getLanguages() != null ? req.getLanguages() : new ArrayList<>())
                .genderPolicy(req.getGenderPolicy())
                .isResidential(Boolean.TRUE.equals(req.getIsResidential()))
                .residentialCapacity(req.getResidentialCapacity())
                .messType(req.getMessType())
                .residentialAgeGroups(req.getResidentialAgeGroups() != null ? req.getResidentialAgeGroups() : new ArrayList<>())
                .residentialAmenities(req.getResidentialAmenities() != null ? req.getResidentialAmenities() : new ArrayList<>())
                .contactInfo(buildContactInfo(req))
                .documents(buildDocuments(req))
                .build();

        // Resolve sports
        if (req.getSportIds() != null) {
            List<Sport> sports = sportRepo.findAllById(req.getSportIds());
            academy.getSportsOffered().addAll(sports);
        }

        academy = academyRepo.save(academy);

        // Upload logo (index 0) and cover (index 1)
        if (images != null && !images.isEmpty()) {
            List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.ACADEMY, academy.getId(), owner);
            if (!urls.isEmpty()) academy.setLogoUri(urls.get(0));
            if (urls.size() > 1) academy.setCoverPhotoUrl(urls.get(1));
        }

        // Inline wizard children
        applyBranches(academy, req.getBranches());
        applyStaff(academy, req.getStaff());
        applyBatches(academy, req.getBatches());
        applyFeeComponents(academy, req.getFeeComponents());
        applyScholarships(academy, req.getScholarships());
        applyAdmissionSteps(academy, req.getAdmissionSteps());
        applyNotableAlumni(academy, req.getNotableAlumni());
        applyTeamAchievements(academy, req.getTeamAchievements());
        applyAffiliations(academy, req.getAffiliations());

        appendVerificationLog(academy, VerificationStage.SUBMITTED, "Academy profile submitted", ActorType.ACADEMY, owner.getName());

        return academyMapper.toResponse(academyRepo.save(academy));
    }

    @Transactional
    public AcademyResponse update(Long id, UpdateAcademyRequest req, List<MultipartFile> images, User owner) {
        Academy academy = findOrThrow(id);
        requireOwner(academy, owner);

        if (req.getName() != null) academy.setName(req.getName());
        if (req.getTagline() != null) academy.setTagline(req.getTagline());
        if (req.getDescription() != null) academy.setDescription(req.getDescription());
        if (req.getFoundedYear() != null) academy.setFoundedYear(req.getFoundedYear());
        if (req.getCity() != null) academy.setCity(req.getCity());
        if (req.getState() != null) academy.setState(req.getState());
        if (req.getAcademyType() != null) academy.setAcademyType(req.getAcademyType());
        if (req.getSeason() != null) academy.setSeason(req.getSeason());
        if (req.getSkillTiers() != null) { academy.getSkillTiers().clear(); academy.getSkillTiers().addAll(req.getSkillTiers()); }
        if (req.getAgeGroupsServed() != null) { academy.getAgeGroupsServed().clear(); academy.getAgeGroupsServed().addAll(req.getAgeGroupsServed()); }
        if (req.getLanguages() != null) { academy.getLanguages().clear(); academy.getLanguages().addAll(req.getLanguages()); }
        if (req.getGenderPolicy() != null) academy.setGenderPolicy(req.getGenderPolicy());
        if (req.getIsResidential() != null) academy.setResidential(req.getIsResidential());
        if (req.getResidentialCapacity() != null) academy.setResidentialCapacity(req.getResidentialCapacity());
        if (req.getMessType() != null) academy.setMessType(req.getMessType());
        if (req.getResidentialAgeGroups() != null) { academy.getResidentialAgeGroups().clear(); academy.getResidentialAgeGroups().addAll(req.getResidentialAgeGroups()); }
        if (req.getResidentialAmenities() != null) { academy.getResidentialAmenities().clear(); academy.getResidentialAmenities().addAll(req.getResidentialAmenities()); }

        if (req.getSportIds() != null) {
            List<Sport> sports = sportRepo.findAllById(req.getSportIds());
            academy.getSportsOffered().clear();
            academy.getSportsOffered().addAll(sports);
        }

        // Partial update contact info
        AcademyContactInfo ci = academy.getContactInfo() != null ? academy.getContactInfo() : new AcademyContactInfo();
        if (req.getContactPersonName() != null) ci.setContactPersonName(req.getContactPersonName());
        if (req.getContactDesignation() != null) ci.setDesignation(req.getContactDesignation());
        if (req.getContactPhone() != null) ci.setPhone(req.getContactPhone());
        if (req.getContactEmail() != null) ci.setEmail(req.getContactEmail());
        if (req.getContactWhatsapp() != null) ci.setWhatsapp(req.getContactWhatsapp());
        if (req.getWebsiteUrl() != null) ci.setWebsiteUrl(req.getWebsiteUrl());
        if (req.getInstagramHandle() != null) ci.setInstagramHandle(req.getInstagramHandle());
        academy.setContactInfo(ci);

        // Partial update documents
        AcademyDocuments docs = academy.getDocuments() != null ? academy.getDocuments() : new AcademyDocuments();
        if (req.getBusinessRegType() != null) docs.setBusinessRegType(req.getBusinessRegType());
        if (req.getRegistrationNumber() != null) docs.setRegistrationNumber(req.getRegistrationNumber());
        if (req.getGstin() != null) docs.setGstin(req.getGstin());
        if (req.getPanNumber() != null) docs.setPanNumber(req.getPanNumber());
        if (req.getPocsoCompliant() != null) docs.setPocsoCompliant(req.getPocsoCompliant());
        if (req.getHasInsurance() != null) docs.setHasInsurance(req.getHasInsurance());
        if (req.getHasSafetyCert() != null) docs.setHasSafetyCert(req.getHasSafetyCert());
        if (req.getBankAccountFull() != null) {
            docs.setBankAccountFull(req.getBankAccountFull());
            docs.setBankAccountLast4(req.getBankAccountFull().length() >= 4 ? req.getBankAccountFull().substring(req.getBankAccountFull().length() - 4) : req.getBankAccountFull());
        }
        if (req.getIfscCode() != null) docs.setIfscCode(req.getIfscCode());
        if (req.getBankName() != null) docs.setBankName(req.getBankName());
        if (req.getUpiId() != null) docs.setUpiId(req.getUpiId());
        academy.setDocuments(docs);

        if (images != null && !images.isEmpty()) {
            List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.ACADEMY, academy.getId(), owner);
            if (!urls.isEmpty()) academy.setLogoUri(urls.get(0));
            if (urls.size() > 1) academy.setCoverPhotoUrl(urls.get(1));
        }

        academy.setUpdatedAt(Instant.now());
        return academyMapper.toResponse(academyRepo.save(academy));
    }

    @Transactional
    public void delete(Long id, User owner) {
        Academy academy = findOrThrow(id);
        requireOwner(academy, owner);
        academyRepo.delete(academy);
    }

    // ── Approval ──────────────────────────────────────────────────────────────

    @Transactional
    public AcademyResponse approve(Long id) {
        Academy academy = findOrThrow(id);
        if (academy.getApprovalStatus() == ApprovalStatus.APPROVED) {
            throw new BadRequestException("Academy is already approved");
        }
        academy.setApprovalStatus(ApprovalStatus.APPROVED);
        academy.setApprovedAt(Instant.now());
        appendVerificationLog(academy, VerificationStage.APPROVED, "Approved by admin", ActorType.ADMIN, "Admin");
        return academyMapper.toResponse(academyRepo.save(academy));
    }

    @Transactional
    public AcademyResponse reject(Long id) {
        Academy academy = findOrThrow(id);
        if (academy.getApprovalStatus() == ApprovalStatus.REJECTED) {
            throw new BadRequestException("Academy is already rejected");
        }
        academy.setApprovalStatus(ApprovalStatus.REJECTED);
        academy.setRejectedAt(Instant.now());
        appendVerificationLog(academy, VerificationStage.REJECTED, "Rejected by admin", ActorType.ADMIN, "Admin");
        return academyMapper.toResponse(academyRepo.save(academy));
    }

    // ── Branch sub-resource ───────────────────────────────────────────────────

    @Transactional
    public AcademyResponse addBranch(Long academyId, AcademyBranchRequest req, List<MultipartFile> images, User owner) {
        Academy academy = findOrThrow(academyId);
        requireOwner(academy, owner);
        AcademyBranch branch = buildBranch(academy, req);
        branch = branchRepo.save(branch);
        if (images != null && !images.isEmpty()) {
            List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.ACADEMY_BRANCH, branch.getId(), owner);
            branch.getPhotoUrls().addAll(urls);
            branchRepo.save(branch);
        }
        academy.getBranches().add(branch);
        return academyMapper.toResponse(academyRepo.save(academy));
    }

    @Transactional
    public AcademyResponse updateBranch(Long academyId, Long branchId, AcademyBranchRequest req, List<MultipartFile> images, User owner) {
        Academy academy = findOrThrow(academyId);
        requireOwner(academy, owner);
        AcademyBranch branch = branchRepo.findByIdAndAcademyId(branchId, academyId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        branch.setName(req.getName());
        if (req.getAddress() != null) branch.setAddress(req.getAddress());
        if (req.getArea() != null) branch.setArea(req.getArea());
        if (req.getCity() != null) branch.setCity(req.getCity());
        if (req.getPincode() != null) branch.setPincode(req.getPincode());
        if (req.getLatitude() != null) branch.setLatitude(req.getLatitude());
        if (req.getLongitude() != null) branch.setLongitude(req.getLongitude());
        if (req.getFacilities() != null) { branch.getFacilities().clear(); branch.getFacilities().addAll(req.getFacilities()); }
        if (req.getIsResidential() != null) branch.setResidential(req.getIsResidential());
        if (req.getIsPrimary() != null) branch.setPrimary(req.getIsPrimary());
        if (req.getSports() != null) { branch.getSports().clear(); branch.getSports().addAll(req.getSports()); }
        if (req.getLinkedVenueId() != null) {
            Ground venue = groundRepo.findById(req.getLinkedVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue not found"));
            branch.setLinkedVenue(venue);
        }
        if (images != null && !images.isEmpty()) {
            List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.ACADEMY_BRANCH, branch.getId(), owner);
            branch.getPhotoUrls().addAll(urls);
        }
        branchRepo.save(branch);
        return academyMapper.toResponse(findOrThrow(academyId));
    }

    @Transactional
    public AcademyResponse removeBranch(Long academyId, Long branchId, User owner) {
        Academy academy = findOrThrow(academyId);
        requireOwner(academy, owner);
        AcademyBranch branch = branchRepo.findByIdAndAcademyId(branchId, academyId)
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));
        academy.getBranches().remove(branch);
        branchRepo.delete(branch);
        return academyMapper.toResponse(academyRepo.save(academy));
    }

    // ── Staff sub-resource ────────────────────────────────────────────────────

    @Transactional
    public AcademyResponse addStaff(Long academyId, AcademyStaffRequest req, MultipartFile photo, User owner) {
        Academy academy = findOrThrow(academyId);
        requireOwner(academy, owner);
        AcademyStaffMember member = buildStaffMember(academy, req);
        member = staffRepo.save(member);
        if (photo != null && !photo.isEmpty()) {
            String url = mediaUploadService.uploadAllAndRecord(List.of(photo), MediaUploadEntityType.ACADEMY, member.getId(), owner).stream().findFirst().orElse(null);
            if (url != null) { member.setPhotoUri(url); staffRepo.save(member); }
        }
        academy.getStaff().add(member);
        return academyMapper.toResponse(academyRepo.save(academy));
    }

    @Transactional
    public AcademyResponse updateStaff(Long academyId, Long staffId, AcademyStaffRequest req, MultipartFile photo, User owner) {
        Academy academy = findOrThrow(academyId);
        requireOwner(academy, owner);
        AcademyStaffMember member = staffRepo.findByIdAndAcademyId(staffId, academyId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found"));

        if (req.getName() != null) member.setName(req.getName());
        if (req.getRole() != null) member.setRole(req.getRole());
        if (req.getSports() != null) { member.getSports().clear(); member.getSports().addAll(req.getSports()); }
        if (req.getYearsOfExperience() != null) member.setYearsOfExperience(req.getYearsOfExperience());
        if (req.getCertifications() != null) { member.getCertifications().clear(); member.getCertifications().addAll(req.getCertifications()); }
        if (req.getPlayingBackground() != null) member.setPlayingBackground(req.getPlayingBackground());
        if (req.getIsHeadCoach() != null) member.setHeadCoach(req.getIsHeadCoach());
        if (req.getBio() != null) member.setBio(req.getBio());
        if (req.getLinkedCoachId() != null) {
            Coach coach = coachRepo.findById(req.getLinkedCoachId())
                    .orElseThrow(() -> new ResourceNotFoundException("Coach not found"));
            member.setLinkedCoach(coach);
        }
        if (photo != null && !photo.isEmpty()) {
            String url = mediaUploadService.uploadAllAndRecord(List.of(photo), MediaUploadEntityType.ACADEMY, member.getId(), owner).stream().findFirst().orElse(null);
            if (url != null) member.setPhotoUri(url);
        }
        staffRepo.save(member);
        return academyMapper.toResponse(findOrThrow(academyId));
    }

    @Transactional
    public AcademyResponse removeStaff(Long academyId, Long staffId, User owner) {
        Academy academy = findOrThrow(academyId);
        requireOwner(academy, owner);
        AcademyStaffMember member = staffRepo.findByIdAndAcademyId(staffId, academyId)
                .orElseThrow(() -> new ResourceNotFoundException("Staff member not found"));
        academy.getStaff().remove(member);
        staffRepo.delete(member);
        return academyMapper.toResponse(academyRepo.save(academy));
    }

    // ── Batch sub-resource ────────────────────────────────────────────────────

    @Transactional
    public AcademyResponse addBatch(Long academyId, AcademyBatchRequest req, User owner) {
        Academy academy = findOrThrow(academyId);
        requireOwner(academy, owner);
        AcademyBatch batch = buildBatch(academy, req);
        academy.getBatches().add(batch);
        return academyMapper.toResponse(academyRepo.save(academy));
    }

    @Transactional
    public AcademyResponse updateBatch(Long academyId, Long batchId, AcademyBatchRequest req, User owner) {
        Academy academy = findOrThrow(academyId);
        requireOwner(academy, owner);
        AcademyBatch batch = batchRepo.findByIdAndAcademyId(batchId, academyId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));

        if (req.getName() != null) batch.setName(req.getName());
        if (req.getSportId() != null) {
            Sport sport = sportRepo.findById(req.getSportId()).orElseThrow(() -> new ResourceNotFoundException("Sport not found"));
            batch.setSport(sport);
        }
        if (req.getSkillTier() != null) batch.setSkillTier(req.getSkillTier());
        if (req.getAgeGroups() != null) { batch.getAgeGroups().clear(); batch.getAgeGroups().addAll(req.getAgeGroups()); }
        if (req.getDays() != null) { batch.getDays().clear(); batch.getDays().addAll(req.getDays()); }
        if (req.getStartTime() != null) batch.setStartTime(req.getStartTime());
        if (req.getEndTime() != null) batch.setEndTime(req.getEndTime());
        if (req.getCapacity() != null) batch.setCapacity(req.getCapacity());
        if (req.getFee() != null) batch.setFee(req.getFee());
        if (req.getFeeFrequency() != null) batch.setFeeFrequency(req.getFeeFrequency());
        if (req.getIsOpen() != null) batch.setOpen(req.getIsOpen());
        if (req.getGenderPolicy() != null) batch.setGenderPolicy(req.getGenderPolicy());
        if (req.getDescription() != null) batch.setDescription(req.getDescription());

        batchRepo.save(batch);
        return academyMapper.toResponse(findOrThrow(academyId));
    }

    @Transactional
    public AcademyResponse removeBatch(Long academyId, Long batchId, User owner) {
        Academy academy = findOrThrow(academyId);
        requireOwner(academy, owner);
        AcademyBatch batch = batchRepo.findByIdAndAcademyId(batchId, academyId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));
        academy.getBatches().remove(batch);
        batchRepo.delete(batch);
        return academyMapper.toResponse(academyRepo.save(academy));
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private Academy findOrThrow(Long id) {
        return academyRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Academy not found with id: " + id));
    }

    private void requireOwner(Academy academy, User user) {
        if (!academy.getOwner().getId().equals(user.getId())) {
            throw new BadRequestException("You do not own this academy");
        }
    }

    private AcademyContactInfo buildContactInfo(CreateAcademyRequest req) {
        return AcademyContactInfo.builder()
                .contactPersonName(req.getContactPersonName())
                .designation(req.getContactDesignation())
                .phone(req.getContactPhone())
                .email(req.getContactEmail())
                .whatsapp(req.getContactWhatsapp())
                .websiteUrl(req.getWebsiteUrl())
                .instagramHandle(req.getInstagramHandle())
                .build();
    }

    private AcademyDocuments buildDocuments(CreateAcademyRequest req) {
        AcademyDocuments.AcademyDocumentsBuilder b = AcademyDocuments.builder()
                .businessRegType(req.getBusinessRegType())
                .registrationNumber(req.getRegistrationNumber())
                .gstin(req.getGstin())
                .panNumber(req.getPanNumber())
                .pocsoCompliant(Boolean.TRUE.equals(req.getPocsoCompliant()))
                .hasInsurance(Boolean.TRUE.equals(req.getHasInsurance()))
                .hasSafetyCert(Boolean.TRUE.equals(req.getHasSafetyCert()))
                .ifscCode(req.getIfscCode())
                .bankName(req.getBankName())
                .upiId(req.getUpiId());
        if (req.getBankAccountFull() != null) {
            b.bankAccountFull(req.getBankAccountFull())
             .bankAccountLast4(req.getBankAccountFull().length() >= 4 ? req.getBankAccountFull().substring(req.getBankAccountFull().length() - 4) : req.getBankAccountFull());
        }
        return b.build();
    }

    private AcademyBranch buildBranch(Academy academy, AcademyBranchRequest req) {
        AcademyBranch.AcademyBranchBuilder b = AcademyBranch.builder()
                .academy(academy)
                .name(req.getName())
                .address(req.getAddress())
                .area(req.getArea())
                .city(req.getCity())
                .pincode(req.getPincode())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .isResidential(Boolean.TRUE.equals(req.getIsResidential()))
                .isPrimary(Boolean.TRUE.equals(req.getIsPrimary()));
        if (req.getFacilities() != null) b.facilities(new ArrayList<>(req.getFacilities()));
        if (req.getSports() != null) b.sports(new ArrayList<>(req.getSports()));
        if (req.getLinkedVenueId() != null) {
            groundRepo.findById(req.getLinkedVenueId()).ifPresent(b::linkedVenue);
        }
        return b.build();
    }

    private AcademyStaffMember buildStaffMember(Academy academy, AcademyStaffRequest req) {
        AcademyStaffMember.AcademyStaffMemberBuilder b = AcademyStaffMember.builder()
                .academy(academy)
                .name(req.getName())
                .role(req.getRole())
                .yearsOfExperience(req.getYearsOfExperience())
                .playingBackground(req.getPlayingBackground())
                .isHeadCoach(Boolean.TRUE.equals(req.getIsHeadCoach()))
                .bio(req.getBio());
        if (req.getSports() != null) b.sports(new ArrayList<>(req.getSports()));
        if (req.getCertifications() != null) b.certifications(new ArrayList<>(req.getCertifications()));
        if (req.getLinkedCoachId() != null) {
            coachRepo.findById(req.getLinkedCoachId()).ifPresent(b::linkedCoach);
        }
        return b.build();
    }

    private AcademyBatch buildBatch(Academy academy, AcademyBatchRequest req) {
        AcademyBatch.AcademyBatchBuilder b = AcademyBatch.builder()
                .academy(academy)
                .name(req.getName())
                .skillTier(req.getSkillTier())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .capacity(req.getCapacity())
                .fee(req.getFee())
                .feeFrequency(req.getFeeFrequency())
                .isOpen(req.getIsOpen() == null || req.getIsOpen())
                .genderPolicy(req.getGenderPolicy())
                .description(req.getDescription());
        if (req.getAgeGroups() != null) b.ageGroups(new ArrayList<>(req.getAgeGroups()));
        if (req.getDays() != null) b.days(new ArrayList<>(req.getDays()));
        if (req.getSportId() != null) {
            sportRepo.findById(req.getSportId()).ifPresent(b::sport);
        }
        if (req.getBranchId() != null) {
            branchRepo.findById(req.getBranchId()).ifPresent(b::branch);
        }
        if (req.getAssignedStaffId() != null) {
            staffRepo.findById(req.getAssignedStaffId()).ifPresent(b::assignedStaff);
        }
        return b.build();
    }

    private void applyBranches(Academy academy, List<AcademyBranchRequest> list) {
        if (list == null) return;
        for (AcademyBranchRequest r : list) {
            academy.getBranches().add(buildBranch(academy, r));
        }
    }

    private void applyStaff(Academy academy, List<AcademyStaffRequest> list) {
        if (list == null) return;
        for (AcademyStaffRequest r : list) {
            academy.getStaff().add(buildStaffMember(academy, r));
        }
    }

    private void applyBatches(Academy academy, List<AcademyBatchRequest> list) {
        if (list == null) return;
        for (AcademyBatchRequest r : list) {
            academy.getBatches().add(buildBatch(academy, r));
        }
    }

    private void applyFeeComponents(Academy academy, List<AcademyFeeComponentRequest> list) {
        if (list == null) return;
        for (AcademyFeeComponentRequest r : list) {
            academy.getFeeComponents().add(AcademyFeeComponent.builder()
                    .academy(academy)
                    .label(r.getLabel())
                    .amount(r.getAmount())
                    .frequency(r.getFrequency())
                    .isMandatory(r.getIsMandatory() == null || r.getIsMandatory())
                    .isRefundable(Boolean.TRUE.equals(r.getIsRefundable()))
                    .notes(r.getNotes())
                    .build());
        }
    }

    private void applyScholarships(Academy academy, List<AcademyScholarshipRequest> list) {
        if (list == null) return;
        for (AcademyScholarshipRequest r : list) {
            academy.getScholarships().add(AcademyScholarship.builder()
                    .academy(academy)
                    .name(r.getName())
                    .description(r.getDescription())
                    .discountPct(r.getDiscountPct())
                    .fixedAmount(r.getFixedAmount())
                    .eligibility(r.getEligibility())
                    .build());
        }
    }

    private void applyAdmissionSteps(Academy academy, List<AcademyAdmissionStepRequest> list) {
        if (list == null) return;
        for (AcademyAdmissionStepRequest r : list) {
            academy.getAdmissionSteps().add(AcademyAdmissionStep.builder()
                    .academy(academy)
                    .type(r.getType())
                    .label(r.getLabel())
                    .isFree(r.getIsFree() == null || r.getIsFree())
                    .fee(r.getFee())
                    .sortOrder(r.getSortOrder() != null ? r.getSortOrder() : 0)
                    .build());
        }
    }

    private void applyNotableAlumni(Academy academy, List<AcademyNotableAlumniRequest> list) {
        if (list == null) return;
        for (AcademyNotableAlumniRequest r : list) {
            academy.getNotableAlumni().add(AcademyNotableAlumni.builder()
                    .academy(academy)
                    .name(r.getName())
                    .achievement(r.getAchievement())
                    .year(r.getYear())
                    .sport(r.getSport())
                    .consent(Boolean.TRUE.equals(r.getHasConsent()))
                    .build());
        }
    }

    private void applyTeamAchievements(Academy academy, List<AcademyTeamAchievementRequest> list) {
        if (list == null) return;
        for (AcademyTeamAchievementRequest r : list) {
            academy.getTeamAchievements().add(AcademyTeamAchievement.builder()
                    .academy(academy)
                    .title(r.getTitle())
                    .organization(r.getOrganization())
                    .year(r.getYear())
                    .sport(r.getSport())
                    .build());
        }
    }

    private void applyAffiliations(Academy academy, List<AcademyAffiliationRequest> list) {
        if (list == null) return;
        for (AcademyAffiliationRequest r : list) {
            academy.getAffiliations().add(AcademyAffiliation.builder()
                    .academy(academy)
                    .body(r.getBody())
                    .label(r.getLabel())
                    .registrationNumber(r.getRegistrationNumber())
                    .build());
        }
    }

    private void appendVerificationLog(Academy academy, VerificationStage stage, String note, ActorType actorType, String actorName) {
        academy.getVerificationLog().add(AcademyVerificationLog.builder()
                .academy(academy)
                .stage(stage)
                .note(note)
                .actorType(actorType)
                .actorName(actorName)
                .build());
    }
}
