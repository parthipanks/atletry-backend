package com.atletry.dto.response;

import lombok.Data;

@Data
public class AcademyTeamAchievementResponse {
    private Long id;
    private String title;
    private String organization;
    private Integer year;
    private String sport;
    private String proofUri;
}
