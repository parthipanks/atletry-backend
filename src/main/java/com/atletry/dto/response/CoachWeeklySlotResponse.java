package com.atletry.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Data @Builder
public class CoachWeeklySlotResponse {
    private Long id;
    private DayOfWeek dayOfWeek;
    private boolean isAvailable;

    @Schema(type = "string", pattern = "^([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$", example = "07:00:00")
    private LocalTime startTime;

    @Schema(type = "string", pattern = "^([01]\\d|2[0-3]):([0-5]\\d):([0-5]\\d)$", example = "21:00:00")
    private LocalTime endTime;
}
