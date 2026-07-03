package com.atletry.dto.request;

import com.atletry.enums.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Data
public class AcademyBatchRequest {

    @NotBlank
    private String name;
    private Long sportId;
    private SkillTier skillTier;
    private List<AgeGroup> ageGroups;
    private List<DayOfWeek> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer capacity;
    private Long branchId;
    private Long assignedStaffId;
    private BigDecimal fee;
    private FeeFrequency feeFrequency;
    private Boolean isOpen;
    private GenderPolicy genderPolicy;
    private String description;
}
