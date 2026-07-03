package com.atletry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class NotableStudentRequest {
    @NotBlank private String name;
    @NotBlank private String achievement;
    private Integer year;
    private boolean consent;
}
