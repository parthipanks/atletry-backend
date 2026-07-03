package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;


@Data @Builder
public class CoachEducationResponse {
    private Long id;
    private String qualification;
    private String institution;
    private Integer year;
    private String documentUri;
}
