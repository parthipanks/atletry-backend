package com.atletry.mapper;

import com.atletry.dto.response.CourtResponse;
import com.atletry.dto.response.GroundResponse;
import com.atletry.dto.response.OperatingHoursResponse;
import com.atletry.dto.response.PricingTierResponse;
import com.atletry.entity.CourtPricingTier;
import com.atletry.entity.Ground;
import com.atletry.entity.GroundCourt;
import com.atletry.entity.GroundOperatingHours;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class GroundMapper {

    public GroundResponse toResponse(Ground g) {
        GroundResponse r = new GroundResponse();
        r.setId(g.getId());
        r.setName(g.getName());
        r.setVenueType(g.getVenueType());
        r.setDescription(g.getDescription());
        r.setPincode(g.getPincode());
        r.setAddressLine1(g.getAddressLine1());
        r.setAddressLine2(g.getAddressLine2());
        r.setAddress(g.getAddress());
        r.setLatitude(g.getLatitude());
        r.setLongitude(g.getLongitude());
        r.setYearOpened(g.getYearOpened());
        r.setAmenities(new HashSet<>(g.getAmenities()));
        r.setCustomAmenities(g.getCustomAmenities());
        r.setSportId(g.getSport() != null ? g.getSport().getId() : null);
        r.setSportName(g.getSport() != null ? g.getSport().getName() : null);
        r.setCreatedById(g.getCreatedBy().getId());
        r.setCreatedByName(g.getCreatedBy().getName());
        r.setImageUrls(new ArrayList<>(g.getImageUrls()));
        r.setApprovalStatus(g.getApprovalStatus());
        r.setActive(g.isActive());
        r.setCreatedDate(g.getCreatedDate());
        r.setUpdatedDate(g.getUpdatedDate());

        r.setCourts(g.getCourts().stream()
                .filter(GroundCourt::isActive)
                .map(this::toCourtResponse)
                .collect(Collectors.toList()));

        r.setOperatingHours(g.getOperatingHours().stream()
                .map(this::toOperatingHoursResponse)
                .collect(Collectors.toList()));

        return r;
    }

    public List<GroundResponse> toResponseList(List<Ground> grounds) {
        return grounds.stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CourtResponse toCourtResponse(GroundCourt c) {
        return CourtResponse.builder()
                .id(c.getId())
                .name(c.getName())
                .sportId(c.getSport() != null ? c.getSport().getId() : null)
                .sportName(c.getSport() != null ? c.getSport().getName() : null)
                .surfaceType(c.getSurfaceType())
                .isIndoor(c.isIndoor())
                .isAirConditioned(c.isAirConditioned())
                .hasFloodlights(c.isHasFloodlights())
                .lengthFt(c.getLengthFt())
                .widthFt(c.getWidthFt())
                .maxPlayers(c.getMaxPlayers())
                .notes(c.getNotes())
                .displayOrder(c.getDisplayOrder())
                .isActive(c.isActive())
                .pricingTiers(c.getPricingTiers().stream()
                        .map(this::toPricingTierResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PricingTierResponse toPricingTierResponse(CourtPricingTier t) {
        return PricingTierResponse.builder()
                .id(t.getId())
                .label(t.getLabel())
                .daysOfWeek(new HashSet<>(t.getDaysOfWeek()))
                .startTime(t.getStartTime())
                .endTime(t.getEndTime())
                .pricePerHour(t.getPricePerHour())
                .build();
    }

    public OperatingHoursResponse toOperatingHoursResponse(GroundOperatingHours h) {
        return OperatingHoursResponse.builder()
                .id(h.getId())
                .dayOfWeek(h.getDayOfWeek())
                .isOpen(h.isOpen())
                .openTime(h.getOpenTime())
                .closeTime(h.getCloseTime())
                .build();
    }
}
