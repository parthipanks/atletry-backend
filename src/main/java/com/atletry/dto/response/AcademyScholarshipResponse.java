package com.atletry.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AcademyScholarshipResponse {
    private Long id;
    private String name;
    private String description;
    private Integer discountPct;
    private BigDecimal fixedAmount;
    private String eligibility;
}
