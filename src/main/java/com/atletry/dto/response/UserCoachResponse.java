package com.atletry.dto.response;

import com.atletry.enums.ApprovalStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;


@Data
public class UserCoachResponse {
    private Long          id;
    private Long          coachId;
    private String        coachFullName;
    private String        coachDisplayName;
    private String        coachBio;
    private String        coachPhone;
    private int           coachYearsOfExperience;
    private Long          coachPrimarySportId;
    private String        coachPrimarySportName;
    private String        coachProfilePhotoUrl;
    private BigDecimal    coachBaseHourlyRate;
    private ApprovalStatus coachApprovalStatus;
    private ZonedDateTime assignedAt;
}
