package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@Data
@Builder
public class TournamentResponse {

    private Long id;
    private String name;
    private String sportName;
    private Long sportId;
    private String format;
    private String entryUnit;
    private int maxSlots;
    private int registeredCount;
    private BigDecimal entryFee;
    private BigDecimal prizePool;
    private LocalDate startsAt;
    private long daysLeft;
    private String status;
    private boolean isRegistered;
    private List<String> imageUrls;
}
