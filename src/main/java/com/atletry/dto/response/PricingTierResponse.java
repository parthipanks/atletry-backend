package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;


@Data
@Builder
public class PricingTierResponse {
    private Long         id;
    private String       label;
    private Set<DayOfWeek> daysOfWeek;
    private LocalTime    startTime;
    private LocalTime    endTime;
    private BigDecimal   pricePerHour;
}
