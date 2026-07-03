package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "academy_team_achievements")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyTeamAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academy_id", nullable = false)
    private Academy academy;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 150)
    private String organization;

    private Integer year;

    @Column(length = 100)
    private String sport;

    @Column(name = "proof_uri", length = 500)
    private String proofUri;
}
