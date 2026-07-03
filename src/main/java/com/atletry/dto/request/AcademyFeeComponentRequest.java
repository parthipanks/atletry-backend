package com.atletry.dto.request;

import com.atletry.enums.FeeFrequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AcademyFeeComponentRequest {

    @NotBlank
    private String label;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private FeeFrequency frequency;
    private Boolean isMandatory;
    private Boolean isRefundable;
    private String notes;
}
