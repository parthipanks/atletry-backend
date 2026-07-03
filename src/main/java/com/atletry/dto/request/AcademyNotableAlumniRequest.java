package com.atletry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AcademyNotableAlumniRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String achievement;
    private Integer year;
    private String sport;
    private Boolean hasConsent;
}
