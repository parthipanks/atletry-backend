package com.atletry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AcademyScholarshipRequest {

    @NotBlank
    private String name;
    private String description;
    private Integer discountPct;
    private BigDecimal fixedAmount;
    private String eligibility;
}
