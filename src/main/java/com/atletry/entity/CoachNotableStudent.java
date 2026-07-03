package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "coach_notable_students")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachNotableStudent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 300)
    private String achievement;

    private Integer year;

    @Column(name = "has_consent")
    @Builder.Default
    private boolean consent = false;
}
