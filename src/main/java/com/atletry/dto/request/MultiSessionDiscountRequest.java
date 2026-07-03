package com.atletry.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Data;


@Data
public class MultiSessionDiscountRequest {
    @Min(2) private int minSessions;
    @Min(1) private int discountPct;
}
