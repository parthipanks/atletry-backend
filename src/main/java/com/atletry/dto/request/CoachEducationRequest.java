package com.atletry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CoachEducationRequest {
    @NotBlank private String qualification;
    @NotBlank private String institution;
    private Integer year;
    private String documentUri;
}
