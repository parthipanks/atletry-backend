package com.atletry.dto.request;

import com.atletry.enums.CertificationLevel;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;


@Data
public class CoachCertificationRequest {
    private String catalogId;
    @NotBlank private String name;
    @NotBlank private String organization;
    private CertificationLevel level;
    private Integer yearObtained;
    private LocalDate expiresAt;
    private String certificateNumber;
    private String documentUri;
}
