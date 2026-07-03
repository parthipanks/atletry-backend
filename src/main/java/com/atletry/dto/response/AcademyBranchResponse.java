package com.atletry.dto.response;

import com.atletry.enums.BranchFacility;
import lombok.Data;

import java.util.List;

@Data
public class AcademyBranchResponse {
    private Long id;
    private String name;
    private String address;
    private String area;
    private String city;
    private String pincode;
    private Double latitude;
    private Double longitude;
    private List<BranchFacility> facilities;
    private boolean isResidential;
    private boolean isPrimary;
    private Long linkedVenueId;
    private List<String> photoUrls;
    private List<String> sports;
    private boolean isActive;
}
