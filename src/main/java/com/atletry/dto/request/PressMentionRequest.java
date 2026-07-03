package com.atletry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;


@Data
public class PressMentionRequest {
    @NotBlank private String title;
    @NotBlank private String publication;
    private String url;
    private LocalDate date;
}
