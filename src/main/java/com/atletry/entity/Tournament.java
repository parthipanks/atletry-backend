package com.atletry.entity;

import com.atletry.enums.TournamentStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "tournaments")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Tournament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @Column(nullable = false, length = 100)
    private String format;

    @Column(nullable = false, length = 20)
    private String entryUnit;

    @Column(nullable = false)
    private int maxSlots;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal entryFee;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal prizePool;

    @Column(nullable = false)
    private LocalDate startsAt;

    @Column(nullable = false)
    private Instant registrationDeadline;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tournament_images", joinColumns = @JoinColumn(name = "tournament_id"))
    @Column(name = "image_url", length = 500)
    @OrderColumn(name = "sort_order")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private TournamentStatus status = TournamentStatus.PENDING_APPROVAL;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @OneToMany(mappedBy = "tournament", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<TournamentRegistration> registrations = new ArrayList<>();
}
