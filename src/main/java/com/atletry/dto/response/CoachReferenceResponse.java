package com.atletry.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;


@Data @Builder
public class CoachReferenceResponse {
    private Long id;
    private String name;
    private String relationship;
    private String phone;
    private String email;
    private boolean isVerified;
    private Instant verifiedAt;
}
