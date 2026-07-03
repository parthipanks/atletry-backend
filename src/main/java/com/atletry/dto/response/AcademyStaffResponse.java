package com.atletry.dto.response;

import com.atletry.enums.StaffRole;
import lombok.Data;

import java.util.List;

@Data
public class AcademyStaffResponse {
    private Long id;
    private String name;
    private StaffRole role;
    private String photoUri;
    private List<String> sports;
    private Integer yearsOfExperience;
    private List<String> certifications;
    private String playingBackground;
    private Long linkedCoachId;
    private boolean isHeadCoach;
    private String bio;
}
