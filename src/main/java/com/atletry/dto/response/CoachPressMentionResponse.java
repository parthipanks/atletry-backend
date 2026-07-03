package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data @Builder
public class CoachPressMentionResponse {
    private Long id;
    private String title;
    private String publication;
    private String url;
    private LocalDate date;
}
