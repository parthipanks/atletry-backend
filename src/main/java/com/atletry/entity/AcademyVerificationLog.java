package com.atletry.entity;

import com.atletry.enums.ActorType;
import com.atletry.enums.VerificationStage;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;


@Entity
@Table(name = "academy_verification_logs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyVerificationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academy_id", nullable = false)
    private Academy academy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private VerificationStage stage;

    @Column(length = 500)
    private String note;

    @Column(nullable = false)
    @Builder.Default
    private Instant timestamp = Instant.now();

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_type", length = 20)
    private ActorType actorType;

    @Column(name = "actor_name", length = 100)
    private String actorName;
}
