package com.atletry.entity;

import com.atletry.enums.SurfaceType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "ground_courts")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GroundCourt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ground_id", nullable = false)
    private Ground ground;

    @Column(nullable = false, length = 40)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @Enumerated(EnumType.STRING)
    @Column(name = "surface_type", length = 30)
    private SurfaceType surfaceType;

    @Column(name = "is_indoor", nullable = false)
    @Builder.Default
    private boolean isIndoor = false;

    @Column(name = "is_air_conditioned", nullable = false)
    @Builder.Default
    private boolean isAirConditioned = false;

    @Column(name = "has_floodlights", nullable = false)
    @Builder.Default
    private boolean hasFloodlights = false;

    @Column(name = "length_ft")
    private Integer lengthFt;

    @Column(name = "width_ft")
    private Integer widthFt;

    @Column(name = "max_players")
    private Integer maxPlayers;

    @Column(name = "notes", length = 200)
    private String notes;

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private int displayOrder = 0;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @OneToMany(mappedBy = "court", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CourtPricingTier> pricingTiers = new ArrayList<>();
}
