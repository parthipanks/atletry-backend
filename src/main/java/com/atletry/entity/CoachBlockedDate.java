package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "coach_blocked_dates")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachBlockedDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 200)
    private String reason;
}
