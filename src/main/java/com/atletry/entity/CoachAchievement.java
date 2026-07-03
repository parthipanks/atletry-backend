package com.atletry.entity;

import com.atletry.enums.AchievementProofType;
import com.atletry.enums.AchievementType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "coach_achievements")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AchievementType achievementType;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 150)
    private String organization;

    private Integer year;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "proof_uri", length = 500)
    private String proofUri;

    @Enumerated(EnumType.STRING)
    @Column(name = "proof_type", length = 20)
    private AchievementProofType proofType;

    @Column(name = "is_verified")
    @Builder.Default
    private boolean isVerified = false;
}
