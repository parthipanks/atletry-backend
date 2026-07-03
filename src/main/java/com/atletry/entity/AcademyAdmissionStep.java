package com.atletry.entity;

import com.atletry.enums.AdmissionStepType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "academy_admission_steps")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyAdmissionStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academy_id", nullable = false)
    private Academy academy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AdmissionStepType type;

    @Column(nullable = false, length = 100)
    private String label;

    @Column(name = "is_free")
    @Builder.Default
    private boolean isFree = true;

    @Column(precision = 10, scale = 2)
    private BigDecimal fee;

    @Column(name = "sort_order")
    @Builder.Default
    private int sortOrder = 0;
}
