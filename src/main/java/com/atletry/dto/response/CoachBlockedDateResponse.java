package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data @Builder
public class CoachBlockedDateResponse {
    private Long id;
    private LocalDate date;
    private String reason;
}
