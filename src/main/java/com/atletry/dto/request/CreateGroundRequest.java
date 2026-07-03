package com.atletry.dto.request;

import com.atletry.enums.GroundAmenity;
import com.atletry.enums.VenueType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data
@Schema(description = "Register a new ground / venue")
public class CreateGroundRequest {

    @NotBlank(message = "Ground name is required")
    @Size(min = 2, max = 100, message = "Name must be 2–100 characters")
    @Schema(example = "Hummingbird Cricket Stadium")
    private String name;

    @Schema(description = "Type of venue facility")
    private VenueType venueType;

    @Schema(description = "What makes this venue special")
    private String description;

    @Pattern(regexp = "\\d{6}", message = "Pincode must be exactly 6 digits")
    @Schema(example = "500072")
    private String pincode;

    @Schema(example = "123 MG Road")
    private String addressLine1;

    @Schema(example = "Banjara Hills")
    private String addressLine2;

    @Schema(description = "Legacy single-string address (optional if addressLine1 is provided)")
    private String address;

    @Schema(example = "17.3850")
    private Double latitude;

    @Schema(example = "78.4867")
    private Double longitude;

    @Schema(description = "Year the venue opened, e.g. 2018", example = "2018")
    private Integer yearOpened;

    @Schema(description = "Amenities available at this venue")
    private Set<GroundAmenity> amenities = new HashSet<>();

    @Size(max = 200, message = "Custom amenities must be at most 200 characters")
    @Schema(description = "Any special amenities not in the standard list")
    private String customAmenities;

    @Schema(description = "Primary sport this ground is used for")
    private Long sportId;

    @Valid
    @Schema(description = "Courts at this venue (optional — can be added later via /courts endpoints)")
    private List<CreateCourtRequest> courts = new ArrayList<>();

    @Valid
    @Schema(description = "Operating hours per day (optional — can be set later via /operating-hours endpoint)")
    private List<OperatingHoursRequest> operatingHours = new ArrayList<>();
}
