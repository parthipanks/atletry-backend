package com.atletry.dto.response;

import com.atletry.enums.ApprovalStatus;
import com.atletry.enums.GroundAmenity;
import com.atletry.enums.VenueType;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;


@Data
public class GroundResponse {
    private Long           id;
    private String         name;
    private VenueType      venueType;
    private String         description;
    private String         pincode;
    private String         addressLine1;
    private String         addressLine2;
    private String         address;          // legacy
    private Double         latitude;
    private Double         longitude;
    private Integer        yearOpened;
    private Set<GroundAmenity> amenities;
    private String         customAmenities;
    private Long           sportId;
    private String         sportName;
    private Long           createdById;
    private String         createdByName;
    private List<String>   imageUrls;
    private ApprovalStatus approvalStatus;
    private boolean        isActive;
    private ZonedDateTime  createdDate;
    private ZonedDateTime  updatedDate;
    private List<CourtResponse>          courts;
    private List<OperatingHoursResponse> operatingHours;
}
