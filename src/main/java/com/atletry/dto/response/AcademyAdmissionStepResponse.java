package com.atletry.dto.response;

import com.atletry.enums.AdmissionStepType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AcademyAdmissionStepResponse {
    private Long id;
    private AdmissionStepType type;
    private String label;
    private boolean isFree;
    private BigDecimal fee;
    private int sortOrder;
}
