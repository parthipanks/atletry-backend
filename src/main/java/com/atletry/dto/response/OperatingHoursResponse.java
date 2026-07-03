package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Data
@Builder
public class OperatingHoursResponse {
    private Long       id;
    private DayOfWeek  dayOfWeek;
    private boolean    isOpen;
    private LocalTime  openTime;
    private LocalTime  closeTime;
}
