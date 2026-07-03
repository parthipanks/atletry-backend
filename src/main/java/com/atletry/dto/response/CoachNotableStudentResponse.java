package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;


@Data @Builder
public class CoachNotableStudentResponse {
    private Long id;
    private String name;
    private String achievement;
    private Integer year;
    private boolean consent;
}
