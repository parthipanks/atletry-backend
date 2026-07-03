package com.atletry.dto.request;

import com.atletry.enums.SurfaceType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;


@Data
public class CreateCourtRequest {

    @NotBlank(message = "Court name is required")
    @Size(max = 40, message = "Court name must be at most 40 characters")
    private String name;

    private Long sportId;

    private SurfaceType surfaceType;

    private boolean isIndoor = false;

    private boolean isAirConditioned = false;

    private boolean hasFloodlights = false;

    private Integer lengthFt;

    private Integer widthFt;

    private Integer maxPlayers;

    @Size(max = 200, message = "Notes must be at most 200 characters")
    private String notes;

    @Valid
    private List<CreatePricingTierRequest> pricingTiers = new ArrayList<>();
}
