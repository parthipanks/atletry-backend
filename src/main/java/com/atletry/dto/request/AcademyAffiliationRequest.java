package com.atletry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AcademyAffiliationRequest {

    @NotBlank
    private String body;
    @NotBlank
    private String label;
    private String registrationNumber;
}
