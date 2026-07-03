package com.atletry.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;


@Data
public class CreateMatchRequest {

    @NotNull
    private Long sportId;

    @NotBlank
    private String title;

    @NotBlank
    private String venue;

    @NotNull
    @Future
    private Instant scheduledAt;

    @Min(2)
    private int maxPlayers;
}
