package com.atletry.dto.request;

import com.atletry.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
public class CoachProgramRequest {
    @NotBlank private String name;
    @NotNull  private ProgramFormat format;
    private String description;
    private Integer durationMinutes;
    private Integer capacity;
    private List<CoachLevel> skillLevels = new ArrayList<>();
    private List<AgeGroup> ageGroups = new ArrayList<>();
    private Long sportId;
    private BigDecimal pricePerUnit;
    private ProgramFrequency frequency;
    private List<String> inclusions = new ArrayList<>();
    private List<String> exclusions = new ArrayList<>();
    private Integer totalSessions;
    private LocalDate campStartDate;
    private LocalDate campEndDate;
    private boolean isTrial;
    private Integer firstTimeDiscountPct;
    private CancellationPolicy cancellationPolicy;
    private boolean isActive = true;
    private int displayOrder;
}
