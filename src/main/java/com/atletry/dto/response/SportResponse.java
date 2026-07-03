package com.atletry.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;


@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Schema(description = "Sport with its three skill level options")
public class SportResponse {
    private Long   id;
    private String name;
    private String description;
    private String iconUrl;
    private boolean isActive;
    private int    displayOrder;
    private ZonedDateTime createdDate;
    private ZonedDateTime updatedDate;
    private List<SkillLevelResponse> skillLevels;
}
