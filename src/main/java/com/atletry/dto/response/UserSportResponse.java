package com.atletry.dto.response;

import com.atletry.enums.SkillLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "A user's sport selection with chosen skill level")
public class UserSportResponse {
    private Long          id;
    private Long          sportId;
    private String        sportName;
    private String        iconUrl;
    private SkillLevel    skillLevel;
    private String        skillLevelLabel;
    private String        skillLevelDescription;
    private ZonedDateTime updatedDate;
}
