package com.atletry.dto.response;

import lombok.Data;

@Data
public class AcademyAffiliationResponse {
    private Long id;
    private String body;
    private String label;
    private String registrationNumber;
    private String documentUri;
    private boolean isVerified;
}
