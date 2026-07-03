package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;


@Entity
@Table(name = "skill_levels",
       uniqueConstraints = @UniqueConstraint(columnNames = {"sport_id", "level_code"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class SkillLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @Column(name = "level_code", nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private com.atletry.enums.SkillLevel levelCode;

    @Column(nullable = false, length = 60)
    private String label;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;
}
