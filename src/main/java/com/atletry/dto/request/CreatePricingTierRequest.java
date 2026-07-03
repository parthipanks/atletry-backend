package com.atletry.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;


@Data
public class CreatePricingTierRequest {

    @NotBlank(message = "Pricing tier label is required")
    private String label;

    @NotEmpty(message = "At least one day of week is required")
    private Set<DayOfWeek> daysOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal pricePerHour;
}
