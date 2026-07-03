package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "coach_press_mentions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachPressMention {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(nullable = false, length = 150)
    private String publication;

    @Column(length = 1000)
    private String url;

    private LocalDate date;
}
