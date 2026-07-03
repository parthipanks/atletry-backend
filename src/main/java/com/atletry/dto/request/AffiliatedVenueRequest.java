package com.atletry.dto.request;

import com.atletry.enums.VenueRelationshipType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class AffiliatedVenueRequest {
    private String venueRefId;
    @NotBlank private String name;
    private String address;
    private String area;
    private String city;
    private Double latitude;
    private Double longitude;
    private VenueRelationshipType relationship;
    private boolean isPrimary;
}
