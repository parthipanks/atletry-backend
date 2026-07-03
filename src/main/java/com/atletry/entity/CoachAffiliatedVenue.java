package com.atletry.entity;

import com.atletry.enums.VenueRelationshipType;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "coach_affiliated_venues")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachAffiliatedVenue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(name = "venue_ref_id", length = 100)
    private String venueRefId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String area;

    @Column(length = 100)
    private String city;

    private Double latitude;
    private Double longitude;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private VenueRelationshipType relationship;

    @Column(name = "is_primary")
    @Builder.Default
    private boolean isPrimary = false;
}
