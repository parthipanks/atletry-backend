package com.atletry.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;


@Data
public class CreateTournamentRequest {

    @NotNull
    private Long sportId;

    @NotBlank
    private String name;

    @NotBlank
    private String format;

    @NotBlank
    private String entryUnit;

    @Min(2)
    private int maxSlots;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal entryFee;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal prizePool;

    @NotNull
    @Future
    private LocalDate startsAt;

    @NotNull
    @Future
    private Instant registrationDeadline;
}
