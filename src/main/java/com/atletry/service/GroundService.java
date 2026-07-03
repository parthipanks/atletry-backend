package com.atletry.service;

import com.atletry.dto.request.*;
import com.atletry.dto.response.CourtResponse;
import com.atletry.dto.response.GroundResponse;
import com.atletry.dto.response.OperatingHoursResponse;
import com.atletry.dto.response.PricingTierResponse;
import com.atletry.entity.*;
import com.atletry.enums.ApprovalStatus;
import com.atletry.enums.MediaUploadEntityType;
import com.atletry.exception.BadRequestException;
import com.atletry.exception.ResourceNotFoundException;
import com.atletry.mapper.GroundMapper;
import com.atletry.repository.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GroundService {

    private final GroundRepository               groundRepo;
    private final GroundCourtRepository          courtRepo;
    private final CourtPricingTierRepository     pricingRepo;
    private final GroundOperatingHoursRepository hoursRepo;
    private final GroundMapper                   groundMapper;
    private final SportService                   sportService;
    private final MediaUploadService             mediaUploadService;

    // ── Browse ────────────────────────────────────────────────────────────────

    @Transactional(readOnly = true)
    public List<GroundResponse> getAllActive() {
        List<Ground> grounds = groundRepo.findByApprovalStatusAndIsActiveTrueOrderByCreatedDateDesc(ApprovalStatus.APPROVED);
        grounds.forEach(this::initCollections);
        return groundMapper.toResponseList(grounds);
    }

    @Transactional(readOnly = true)
    public List<GroundResponse> getPendingApproval() {
        List<Ground> grounds = groundRepo.findByApprovalStatusOrderByCreatedDateDesc(ApprovalStatus.PENDING_APPROVAL);
        grounds.forEach(this::initCollections);
        return groundMapper.toResponseList(grounds);
    }

    @Transactional(readOnly = true)
    public GroundResponse getById(Long id) {
        Ground ground = findOrThrow(id);
        initCollections(ground);
        return groundMapper.toResponse(ground);
    }

    @Transactional(readOnly = true)
    public List<GroundResponse> getMyGrounds(User user) {
        List<Ground> grounds = groundRepo.findByCreatedByIdAndIsActiveTrueOrderByCreatedDateDesc(user.getId());
        grounds.forEach(this::initCollections);
        return groundMapper.toResponseList(grounds);
    }

    // ── Approval ──────────────────────────────────────────────────────────────

    @Transactional
    public GroundResponse approve(Long id) {
        Ground ground = findOrThrow(id);
        if (ground.getApprovalStatus() != ApprovalStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Ground is not pending approval");
        }
        ground.setApprovalStatus(ApprovalStatus.APPROVED);
        ground = groundRepo.save(ground);
        initCollections(ground);
        return groundMapper.toResponse(ground);
    }

    @Transactional
    public GroundResponse reject(Long id) {
        Ground ground = findOrThrow(id);
        if (ground.getApprovalStatus() != ApprovalStatus.PENDING_APPROVAL) {
            throw new BadRequestException("Ground is not pending approval");
        }
        ground.setApprovalStatus(ApprovalStatus.REJECTED);
        ground = groundRepo.save(ground);
        initCollections(ground);
        return groundMapper.toResponse(ground);
    }

    // ── Create / Update / Delete ───────────────────────────────────────────────

    // ── Helpers ── (continued) ─────────────────────────────────────────────────

    private void initCollections(Ground g) {
        Hibernate.initialize(g.getCreatedBy());
        Hibernate.initialize(g.getSport());
        Hibernate.initialize(g.getImageUrls());
        Hibernate.initialize(g.getCourts());
        g.getCourts().forEach(c -> Hibernate.initialize(c.getPricingTiers()));
        Hibernate.initialize(g.getOperatingHours());
    }

    // ── Create / Update / Delete ───────────────────────────────────────────────

    @Transactional
    public GroundResponse create(CreateGroundRequest req, User user, List<MultipartFile> images) {
        Ground ground = Ground.builder()
                .name(req.getName())
                .venueType(req.getVenueType())
                .description(req.getDescription())
                .pincode(req.getPincode())
                .addressLine1(req.getAddressLine1())
                .addressLine2(req.getAddressLine2())
                .address(req.getAddress())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .yearOpened(req.getYearOpened())
                .amenities(req.getAmenities())
                .customAmenities(req.getCustomAmenities())
                .sport(req.getSportId() != null ? sportService.findOrThrow(req.getSportId()) : null)
                .createdBy(user)
                .build();
        ground = groundRepo.save(ground);

        List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.GROUND, ground.getId(), user);
        ground.getImageUrls().addAll(urls);

        // Persist courts included in the create request
        for (int i = 0; i < req.getCourts().size(); i++) {
            ground = addCourtToGround(ground, req.getCourts().get(i), i);
        }

        // Persist operating hours included in the create request
        for (OperatingHoursRequest h : req.getOperatingHours()) {
            saveOrUpdateHours(ground, h);
        }

        ground = groundRepo.save(ground);
        initCollections(ground);
        return groundMapper.toResponse(ground);
    }

    @Transactional
    public GroundResponse update(Long id, UpdateGroundRequest req, User user, List<MultipartFile> images) {
        Ground ground = findOrThrow(id);
        if (!ground.getCreatedBy().getId().equals(user.getId())) {
            throw new BadRequestException("You can only update grounds you registered.");
        }
        if (req.getName()            != null) ground.setName(req.getName());
        if (req.getVenueType()       != null) ground.setVenueType(req.getVenueType());
        if (req.getDescription()     != null) ground.setDescription(req.getDescription());
        if (req.getPincode()         != null) ground.setPincode(req.getPincode());
        if (req.getAddressLine1()    != null) ground.setAddressLine1(req.getAddressLine1());
        if (req.getAddressLine2()    != null) ground.setAddressLine2(req.getAddressLine2());
        if (req.getAddress()         != null) ground.setAddress(req.getAddress());
        if (req.getLatitude()        != null) ground.setLatitude(req.getLatitude());
        if (req.getLongitude()       != null) ground.setLongitude(req.getLongitude());
        if (req.getYearOpened()      != null) ground.setYearOpened(req.getYearOpened());
        if (req.getAmenities()       != null) ground.setAmenities(req.getAmenities());
        if (req.getCustomAmenities() != null) ground.setCustomAmenities(req.getCustomAmenities());
        if (req.getSportId()         != null) ground.setSport(sportService.findOrThrow(req.getSportId()));
        if (req.getIsActive()        != null) ground.setActive(req.getIsActive());

        List<String> urls = mediaUploadService.uploadAllAndRecord(images, MediaUploadEntityType.GROUND, ground.getId(), user);
        ground.getImageUrls().addAll(urls);

        ground = groundRepo.save(ground);
        initCollections(ground);
        return groundMapper.toResponse(ground);
    }

    @Transactional
    public void delete(Long id, User user) {
        Ground ground = findOrThrow(id);
        if (!ground.getCreatedBy().getId().equals(user.getId())) {
            throw new BadRequestException("You can only delete grounds you registered.");
        }
        ground.setActive(false);
        groundRepo.save(ground);
    }

    // ── Courts ────────────────────────────────────────────────────────────────

    @Transactional
    public CourtResponse addCourt(Long groundId, CreateCourtRequest req, User user) {
        Ground ground = findOrThrow(groundId);
        requireOwner(ground, user);

        int displayOrder = courtRepo.findByGroundIdAndIsActiveTrueOrderByDisplayOrderAsc(groundId).size();
        ground = addCourtToGround(ground, req, displayOrder);
        groundRepo.save(ground);

        GroundCourt saved = ground.getCourts().get(ground.getCourts().size() - 1);
        return groundMapper.toCourtResponse(saved);
    }

    @Transactional
    public CourtResponse updateCourt(Long groundId, Long courtId, UpdateCourtRequest req, User user) {
        Ground ground = findOrThrow(groundId);
        requireOwner(ground, user);

        GroundCourt court = courtRepo.findByIdAndGroundId(courtId, groundId)
                .orElseThrow(() -> new ResourceNotFoundException("Court not found"));

        if (req.getName()            != null) court.setName(req.getName());
        if (req.getSportId()         != null) court.setSport(sportService.findOrThrow(req.getSportId()));
        if (req.getSurfaceType()     != null) court.setSurfaceType(req.getSurfaceType());
        if (req.getIsIndoor()        != null) court.setIndoor(req.getIsIndoor());
        if (req.getIsAirConditioned()!= null) court.setAirConditioned(req.getIsAirConditioned());
        if (req.getHasFloodlights()  != null) court.setHasFloodlights(req.getHasFloodlights());
        if (req.getLengthFt()        != null) court.setLengthFt(req.getLengthFt());
        if (req.getWidthFt()         != null) court.setWidthFt(req.getWidthFt());
        if (req.getMaxPlayers()      != null) court.setMaxPlayers(req.getMaxPlayers());
        if (req.getNotes()           != null) court.setNotes(req.getNotes());
        if (req.getIsActive()        != null) court.setActive(req.getIsActive());
        if (req.getDisplayOrder()    != null) court.setDisplayOrder(req.getDisplayOrder());

        return groundMapper.toCourtResponse(courtRepo.save(court));
    }

    @Transactional
    public void removeCourt(Long groundId, Long courtId, User user) {
        Ground ground = findOrThrow(groundId);
        requireOwner(ground, user);

        GroundCourt court = courtRepo.findByIdAndGroundId(courtId, groundId)
                .orElseThrow(() -> new ResourceNotFoundException("Court not found"));
        court.setActive(false);
        courtRepo.save(court);
    }

    // ── Pricing tiers ─────────────────────────────────────────────────────────

    @Transactional
    public PricingTierResponse addPricingTier(Long groundId, Long courtId,
                                              CreatePricingTierRequest req, User user) {
        Ground ground = findOrThrow(groundId);
        requireOwner(ground, user);

        GroundCourt court = courtRepo.findByIdAndGroundId(courtId, groundId)
                .orElseThrow(() -> new ResourceNotFoundException("Court not found"));

        CourtPricingTier tier = CourtPricingTier.builder()
                .court(court)
                .label(req.getLabel())
                .daysOfWeek(req.getDaysOfWeek())
                .startTime(req.getStartTime())
                .endTime(req.getEndTime())
                .pricePerHour(req.getPricePerHour())
                .build();

        return groundMapper.toPricingTierResponse(pricingRepo.save(tier));
    }

    @Transactional
    public PricingTierResponse updatePricingTier(Long groundId, Long courtId, Long tierId,
                                                 UpdatePricingTierRequest req, User user) {
        Ground ground = findOrThrow(groundId);
        requireOwner(ground, user);

        // Verify court belongs to ground
        courtRepo.findByIdAndGroundId(courtId, groundId)
                .orElseThrow(() -> new ResourceNotFoundException("Court not found"));

        CourtPricingTier tier = pricingRepo.findByIdAndCourtId(tierId, courtId)
                .orElseThrow(() -> new ResourceNotFoundException("Pricing tier not found"));

        if (req.getLabel()        != null) tier.setLabel(req.getLabel());
        if (req.getDaysOfWeek()   != null) tier.setDaysOfWeek(req.getDaysOfWeek());
        if (req.getStartTime()    != null) tier.setStartTime(req.getStartTime());
        if (req.getEndTime()      != null) tier.setEndTime(req.getEndTime());
        if (req.getPricePerHour() != null) tier.setPricePerHour(req.getPricePerHour());

        return groundMapper.toPricingTierResponse(pricingRepo.save(tier));
    }

    @Transactional
    public void removePricingTier(Long groundId, Long courtId, Long tierId, User user) {
        Ground ground = findOrThrow(groundId);
        requireOwner(ground, user);

        courtRepo.findByIdAndGroundId(courtId, groundId)
                .orElseThrow(() -> new ResourceNotFoundException("Court not found"));

        CourtPricingTier tier = pricingRepo.findByIdAndCourtId(tierId, courtId)
                .orElseThrow(() -> new ResourceNotFoundException("Pricing tier not found"));

        pricingRepo.delete(tier);
    }

    // ── Operating hours ───────────────────────────────────────────────────────

    @Transactional
    public List<OperatingHoursResponse> setOperatingHours(Long groundId,
                                                          List<OperatingHoursRequest> requests,
                                                          User user) {
        Ground ground = findOrThrow(groundId);
        requireOwner(ground, user);

        for (OperatingHoursRequest req : requests) {
            saveOrUpdateHours(ground, req);
        }
        groundRepo.save(ground);

        return hoursRepo.findByGroundIdOrderByDayOfWeek(groundId).stream()
                .map(groundMapper::toOperatingHoursResponse)
                .toList();
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    public Ground findOrThrow(Long id) {
        return groundRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ground not found: " + id));
    }

    private void requireOwner(Ground ground, User user) {
        if (!ground.getCreatedBy().getId().equals(user.getId())) {
            throw new BadRequestException("Only the ground owner can perform this action.");
        }
    }

    private Ground addCourtToGround(Ground ground, CreateCourtRequest req, int displayOrder) {
        GroundCourt court = GroundCourt.builder()
                .ground(ground)
                .name(req.getName())
                .sport(req.getSportId() != null ? sportService.findOrThrow(req.getSportId()) : null)
                .surfaceType(req.getSurfaceType())
                .isIndoor(req.isIndoor())
                .isAirConditioned(req.isAirConditioned())
                .hasFloodlights(req.isHasFloodlights())
                .lengthFt(req.getLengthFt())
                .widthFt(req.getWidthFt())
                .maxPlayers(req.getMaxPlayers())
                .notes(req.getNotes())
                .displayOrder(displayOrder)
                .build();

        for (CreatePricingTierRequest tierReq : req.getPricingTiers()) {
            CourtPricingTier tier = CourtPricingTier.builder()
                    .court(court)
                    .label(tierReq.getLabel())
                    .daysOfWeek(tierReq.getDaysOfWeek())
                    .startTime(tierReq.getStartTime())
                    .endTime(tierReq.getEndTime())
                    .pricePerHour(tierReq.getPricePerHour())
                    .build();
            court.getPricingTiers().add(tier);
        }

        ground.getCourts().add(court);
        return ground;
    }

    private void saveOrUpdateHours(Ground ground, OperatingHoursRequest req) {
        GroundOperatingHours hours = hoursRepo
                .findByGroundIdAndDayOfWeek(ground.getId(), req.getDayOfWeek())
                .orElseGet(() -> GroundOperatingHours.builder().ground(ground).dayOfWeek(req.getDayOfWeek()).build());

        hours.setOpen(req.isOpen());
        hours.setOpenTime(req.getOpenTime());
        hours.setCloseTime(req.getCloseTime());
        hoursRepo.save(hours);
    }
}
