package com.atletry.dto.response;

import com.atletry.enums.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Data
public class AcademyBatchResponse {
    private Long id;
    private String name;
    private Long sportId;
    private String sportName;
    private SkillTier skillTier;
    private List<AgeGroup> ageGroups;
    private List<DayOfWeek> days;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer capacity;
    private int enrolledCount;
    private Long branchId;
    private String branchName;
    private Long assignedStaffId;
    private String assignedStaffName;
    private BigDecimal fee;
    private FeeFrequency feeFrequency;
    private boolean isOpen;
    private GenderPolicy genderPolicy;
    private String description;
}
