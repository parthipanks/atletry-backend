package com.atletry.dto.request;

import com.atletry.enums.StaffRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AcademyStaffRequest {

    @NotBlank
    private String name;
    private StaffRole role;
    private List<String> sports;
    private Integer yearsOfExperience;
    private List<String> certifications;
    private String playingBackground;
    private Long linkedCoachId;
    private Boolean isHeadCoach;
    private String bio;
}
