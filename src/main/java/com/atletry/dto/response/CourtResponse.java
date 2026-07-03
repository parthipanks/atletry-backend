package com.atletry.dto.response;

import com.atletry.enums.SurfaceType;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class CourtResponse {
    private Long              id;
    private String            name;
    private Long              sportId;
    private String            sportName;
    private SurfaceType       surfaceType;
    private boolean           isIndoor;
    private boolean           isAirConditioned;
    private boolean           hasFloodlights;
    private Integer           lengthFt;
    private Integer           widthFt;
    private Integer           maxPlayers;
    private String            notes;
    private int               displayOrder;
    private boolean           isActive;
    private List<PricingTierResponse> pricingTiers;
}
