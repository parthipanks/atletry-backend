package com.atletry.dto.response;

import com.atletry.enums.ActorType;
import com.atletry.enums.VerificationStage;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;


@Data @Builder
public class CoachVerificationLogResponse {
    private Long id;
    private VerificationStage stage;
    private String note;
    private Instant timestamp;
    private ActorType actorType;
    private String actorName;
}
