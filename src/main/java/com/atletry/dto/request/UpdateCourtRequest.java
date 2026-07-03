package com.atletry.dto.request;

import com.atletry.enums.SurfaceType;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
public class UpdateCourtRequest {

    @Size(max = 40, message = "Court name must be at most 40 characters")
    private String name;

    private Long        sportId;
    private SurfaceType surfaceType;
    private Boolean     isIndoor;
    private Boolean     isAirConditioned;
    private Boolean     hasFloodlights;
    private Integer     lengthFt;
    private Integer     widthFt;
    private Integer     maxPlayers;

    @Size(max = 200, message = "Notes must be at most 200 characters")
    private String  notes;

    private Boolean isActive;
    private Integer displayOrder;
}
