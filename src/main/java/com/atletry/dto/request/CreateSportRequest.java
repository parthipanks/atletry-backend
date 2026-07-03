package com.atletry.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Create a new sport — three skill levels are auto-generated")
public class CreateSportRequest {

    @NotBlank(message = "Sport name is required")
    @Size(min = 2, max = 100, message = "Name must be 2–100 characters")
    @Schema(example = "Kabaddi")
    private String name;

    @Schema(description = "Short description of the sport")
    private String description;

    @Schema(description = "Position in the sport list", example = "12")
    private int displayOrder = 0;
}
