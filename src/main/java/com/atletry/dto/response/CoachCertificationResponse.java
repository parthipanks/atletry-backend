package com.atletry.dto.response;

import com.atletry.enums.CertificationLevel;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;


@Data @Builder
public class CoachCertificationResponse {
    private Long id;
    private String catalogId;
    private String name;
    private String organization;
    private CertificationLevel level;
    private Integer yearObtained;
    private LocalDate expiresAt;
    private String certificateNumber;
    private String documentUri;
    private boolean isVerified;
    private Instant verifiedAt;
}
