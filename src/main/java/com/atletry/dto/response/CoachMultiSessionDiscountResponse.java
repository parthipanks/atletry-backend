package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;


@Data @Builder
public class CoachMultiSessionDiscountResponse {
    private Long id;
    private int minSessions;
    private int discountPct;
}
