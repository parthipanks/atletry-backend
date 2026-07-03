package com.atletry.dto.request;

import com.atletry.enums.GroundAmenity;
import com.atletry.enums.VenueType;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;


@Data
public class UpdateGroundRequest {

    @Size(min = 2, max = 100, message = "Name must be 2–100 characters")
    private String name;

    private VenueType venueType;

    private String description;

    @Pattern(regexp = "\\d{6}", message = "Pincode must be exactly 6 digits")
    private String pincode;

    private String  addressLine1;
    private String  addressLine2;
    private String  address;
    private Double  latitude;
    private Double  longitude;
    private Integer yearOpened;

    private Set<GroundAmenity> amenities;

    @Size(max = 200, message = "Custom amenities must be at most 200 characters")
    private String  customAmenities;

    private Long    sportId;
    private Boolean isActive;
}
