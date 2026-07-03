package com.atletry.entity;

import com.atletry.enums.AgeGroup;
import com.atletry.enums.FeeFrequency;
import com.atletry.enums.GenderPolicy;
import com.atletry.enums.SkillTier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "academy_batches")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyBatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academy_id", nullable = false)
    private Academy academy;

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @Enumerated(EnumType.STRING)
    @Column(name = "skill_tier", length = 20)
    private SkillTier skillTier;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_batch_age_groups", joinColumns = @JoinColumn(name = "batch_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", length = 20)
    @Builder.Default
    private List<AgeGroup> ageGroups = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_batch_days", joinColumns = @JoinColumn(name = "batch_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 15)
    @Builder.Default
    private List<DayOfWeek> days = new ArrayList<>();

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    private Integer capacity;

    @Column(name = "enrolled_count")
    @Builder.Default
    private int enrolledCount = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private AcademyBranch branch;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_staff_id")
    private AcademyStaffMember assignedStaff;

    @Column(precision = 10, scale = 2)
    private BigDecimal fee;

    @Enumerated(EnumType.STRING)
    @Column(name = "fee_frequency", length = 20)
    private FeeFrequency feeFrequency;

    @Column(name = "is_open")
    @Builder.Default
    private boolean isOpen = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender_policy", length = 10)
    private GenderPolicy genderPolicy;

    @Column(columnDefinition = "TEXT")
    private String description;
}
