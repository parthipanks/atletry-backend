package com.atletry.dto.response;

import com.atletry.enums.FeeFrequency;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AcademyFeeComponentResponse {
    private Long id;
    private String label;
    private BigDecimal amount;
    private FeeFrequency frequency;
    private boolean isMandatory;
    private boolean isRefundable;
    private String notes;
}
