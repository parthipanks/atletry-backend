package com.atletry.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Data
public class OperatingHoursRequest {

    @NotNull(message = "Day of week is required")
    private DayOfWeek dayOfWeek;

    private boolean isOpen = true;

    private LocalTime openTime;

    private LocalTime closeTime;
}
