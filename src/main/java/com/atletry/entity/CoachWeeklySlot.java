package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;


@Entity
@Table(name = "coach_weekly_slots",
       uniqueConstraints = @UniqueConstraint(columnNames = {"coach_id", "day_of_week"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachWeeklySlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false, length = 15)
    private DayOfWeek dayOfWeek;

    @Column(name = "is_available")
    @Builder.Default
    private boolean isAvailable = false;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;
}
