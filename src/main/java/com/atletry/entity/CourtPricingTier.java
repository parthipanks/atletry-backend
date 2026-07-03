package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "court_pricing_tiers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CourtPricingTier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "court_id", nullable = false)
    private GroundCourt court;

    @Column(nullable = false, length = 100)
    private String label;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "pricing_tier_days", joinColumns = @JoinColumn(name = "tier_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", length = 10)
    @Builder.Default
    private Set<DayOfWeek> daysOfWeek = new HashSet<>();

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalTime endTime;

    @Column(name = "price_per_hour", nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;
}
