package com.atletry.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Update sport — only provided fields are changed")
public class UpdateSportRequest {

    @Size(min = 2, max = 100)
    private String name;

    private String description;
    private String iconUrl;
    private Integer displayOrder;
    private Boolean isActive;
}
