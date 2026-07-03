package com.atletry.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;


@Data
public class UpdatePricingTierRequest {
    private String       label;
    private Set<DayOfWeek> daysOfWeek;
    private LocalTime    startTime;
    private LocalTime    endTime;
    private BigDecimal   pricePerHour;
}
