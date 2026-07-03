package com.atletry.dto.response;

import com.atletry.enums.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data @Builder
public class CoachProgramResponse {
    private Long id;
    private String name;
    private ProgramFormat format;
    private String description;
    private Integer durationMinutes;
    private Integer capacity;
    private List<CoachLevel> skillLevels;
    private List<AgeGroup> ageGroups;
    private Long sportId;
    private String sportName;
    private BigDecimal pricePerUnit;
    private ProgramFrequency frequency;
    private List<String> inclusions;
    private List<String> exclusions;
    private Integer totalSessions;
    private LocalDate campStartDate;
    private LocalDate campEndDate;
    private boolean isTrial;
    private Integer firstTimeDiscountPct;
    private CancellationPolicy cancellationPolicy;
    private List<String> photoUrls;
    private boolean isActive;
    private int displayOrder;
}
