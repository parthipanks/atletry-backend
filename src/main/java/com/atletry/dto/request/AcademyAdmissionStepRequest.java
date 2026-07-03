package com.atletry.dto.request;

import com.atletry.enums.AdmissionStepType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AcademyAdmissionStepRequest {

    @NotNull
    private AdmissionStepType type;
    @NotBlank
    private String label;
    private Boolean isFree;
    private BigDecimal fee;
    private Integer sortOrder;
}
