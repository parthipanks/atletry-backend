package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Entity
@Table(name = "ground_operating_hours",
       uniqueConstraints = @UniqueConstraint(columnNames = {"ground_id", "day_of_week"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GroundOperatingHours {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ground_id", nullable = false)
    private Ground ground;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 10)
    private DayOfWeek dayOfWeek;

    @Column(name = "is_open", nullable = false)
    @Builder.Default
    private boolean isOpen = true;

    @Column(name = "open_time")
    private LocalTime openTime;

    @Column(name = "close_time")
    private LocalTime closeTime;
}
