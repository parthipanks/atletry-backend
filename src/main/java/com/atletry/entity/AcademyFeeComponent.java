package com.atletry.entity;

import com.atletry.enums.FeeFrequency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;


@Entity
@Table(name = "academy_fee_components")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyFeeComponent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academy_id", nullable = false)
    private Academy academy;

    @Column(nullable = false, length = 150)
    private String label;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private FeeFrequency frequency;

    @Column(name = "is_mandatory")
    @Builder.Default
    private boolean isMandatory = true;

    @Column(name = "is_refundable")
    @Builder.Default
    private boolean isRefundable = false;

    @Column(length = 300)
    private String notes;
}
