package com.atletry.dto.response;

import com.atletry.enums.AchievementProofType;
import com.atletry.enums.AchievementType;
import lombok.Builder;
import lombok.Data;


@Data @Builder
public class CoachAchievementResponse {
    private Long id;
    private AchievementType achievementType;
    private String title;
    private String organization;
    private Integer year;
    private String description;
    private String proofUri;
    private AchievementProofType proofType;
    private boolean isVerified;
}
