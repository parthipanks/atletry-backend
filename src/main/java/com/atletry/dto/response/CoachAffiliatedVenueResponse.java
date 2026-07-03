package com.atletry.dto.response;

import com.atletry.enums.VenueRelationshipType;
import lombok.Builder;
import lombok.Data;


@Data @Builder
public class CoachAffiliatedVenueResponse {
    private Long id;
    private String venueRefId;
    private String name;
    private String address;
    private String area;
    private String city;
    private Double latitude;
    private Double longitude;
    private VenueRelationshipType relationship;
    private boolean isPrimary;
}
