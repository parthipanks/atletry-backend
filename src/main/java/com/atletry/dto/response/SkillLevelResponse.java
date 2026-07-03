package com.atletry.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "One skill level option for a sport")
public class SkillLevelResponse {
    private String levelCode;    // JUST_STARTING | INTERMEDIATE | COMPETITIVE
    private String label;
    private String description;
}
