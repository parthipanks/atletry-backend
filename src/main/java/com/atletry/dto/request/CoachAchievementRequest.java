package com.atletry.dto.request;

import com.atletry.enums.AchievementProofType;
import com.atletry.enums.AchievementType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CoachAchievementRequest {
    @NotNull private AchievementType achievementType;
    @NotBlank private String title;
    private String organization;
    private Integer year;
    private String description;
    private String proofUri;
    private AchievementProofType proofType;
}
