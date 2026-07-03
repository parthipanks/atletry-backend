package com.atletry.dto.request;

import com.atletry.enums.BranchFacility;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AcademyBranchRequest {

    @NotBlank
    private String name;
    private String address;
    private String area;
    private String city;
    private String pincode;
    private Double latitude;
    private Double longitude;
    private List<BranchFacility> facilities;
    private Boolean isResidential;
    private Boolean isPrimary;
    private Long linkedVenueId;
    private List<String> sports;
}
