package com.atletry.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Complete user profile — name and city (first-time only)")
public class CompleteProfileRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be 2–100 characters")
    @Schema(example = "Chiranjeevi D")
    private String name;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100, message = "City must be 2–100 characters")
    @Schema(description = "City name — can be resolved from GPS coordinates", example = "Hyderabad")
    private String city;

    @Schema(description = "Latitude from device GPS", example = "17.3850")
    private Double latitude;

    @Schema(description = "Longitude from device GPS", example = "78.4867")
    private Double longitude;
}
