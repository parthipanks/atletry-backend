package com.atletry.dto.request;

import com.atletry.enums.SkillLevel;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;


@Data
@Schema(description = "Bulk sport selection — minimum 3 required")
public class SelectSportsRequest {

    @NotEmpty(message = "Sports list must not be empty")
    @Size(min = 3, message = "You must select at least 3 sports")
    @Valid
    private List<SportSelection> sports;

    @Data
    @Schema(description = "Single sport + skill level pairing")
    public static class SportSelection {

        @NotNull(message = "sportId is required")
        @Schema(description = "ID of the sport", example = "1")
        private Long sportId;

        @NotNull(message = "skillLevel is required")
        @Schema(description = "Chosen skill level",
                allowableValues = {"JUST_STARTING", "INTERMEDIATE", "COMPETITIVE"},
                example = "JUST_STARTING")
        private SkillLevel skillLevel;
    }
}
