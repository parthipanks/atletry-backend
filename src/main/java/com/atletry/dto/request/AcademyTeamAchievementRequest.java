package com.atletry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AcademyTeamAchievementRequest {

    @NotBlank
    private String title;
    private String organization;
    private Integer year;
    private String sport;
}
