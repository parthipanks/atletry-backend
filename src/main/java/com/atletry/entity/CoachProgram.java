package com.atletry.entity;

import com.atletry.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "coach_programs")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProgramFormat format;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    private Integer capacity;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_program_skill_levels", joinColumns = @JoinColumn(name = "program_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "skill_level", length = 20)
    @Builder.Default
    private List<CoachLevel> skillLevels = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_program_age_groups", joinColumns = @JoinColumn(name = "program_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "age_group", length = 20)
    @Builder.Default
    private List<AgeGroup> ageGroups = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @Column(name = "price_per_unit", precision = 10, scale = 2)
    private BigDecimal pricePerUnit;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private ProgramFrequency frequency;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_program_inclusions", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "inclusion", length = 200)
    @OrderColumn(name = "sort_order")
    @Builder.Default
    private List<String> inclusions = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_program_exclusions", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "exclusion", length = 200)
    @OrderColumn(name = "sort_order")
    @Builder.Default
    private List<String> exclusions = new ArrayList<>();

    @Column(name = "total_sessions")
    private Integer totalSessions;

    @Column(name = "camp_start_date")
    private LocalDate campStartDate;

    @Column(name = "camp_end_date")
    private LocalDate campEndDate;

    @Column(name = "is_trial")
    @Builder.Default
    private boolean isTrial = false;

    @Column(name = "first_time_discount_pct")
    private Integer firstTimeDiscountPct;

    @Enumerated(EnumType.STRING)
    @Column(name = "cancellation_policy", length = 20)
    private CancellationPolicy cancellationPolicy;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "coach_program_photos", joinColumns = @JoinColumn(name = "program_id"))
    @Column(name = "photo_url", length = 500)
    @OrderColumn(name = "sort_order")
    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

    @Column(name = "display_order")
    @Builder.Default
    private int displayOrder = 0;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;
}
