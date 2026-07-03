package com.atletry.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;


@Data
public class BlockedDateRequest {
    @NotNull private LocalDate date;
    private String reason;
}
