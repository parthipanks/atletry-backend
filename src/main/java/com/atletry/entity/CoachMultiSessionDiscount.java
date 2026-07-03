package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "coach_multi_session_discounts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachMultiSessionDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(name = "min_sessions", nullable = false)
    private int minSessions;

    @Column(name = "discount_pct", nullable = false)
    private int discountPct;
}
