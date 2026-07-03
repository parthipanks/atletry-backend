package com.atletry.dto.request;

import com.atletry.enums.SkillLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Update skill level for an existing user-sport selection")
public class UpdateUserSportRequest {

    @NotNull(message = "skillLevel is required")
    @Schema(allowableValues = {"JUST_STARTING", "INTERMEDIATE", "COMPETITIVE"}, example = "INTERMEDIATE")
    private SkillLevel skillLevel;
}
