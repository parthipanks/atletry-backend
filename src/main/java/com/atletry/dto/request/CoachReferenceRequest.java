package com.atletry.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class CoachReferenceRequest {
    @NotBlank private String name;
    @NotBlank private String relationship;
    private String phone;
    private String email;
}
