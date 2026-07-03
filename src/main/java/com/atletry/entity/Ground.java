package com.atletry.entity;

import com.atletry.enums.ApprovalStatus;
import com.atletry.enums.GroundAmenity;
import com.atletry.enums.VenueType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Table(name = "grounds")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Ground {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "venue_type", length = 40)
    private VenueType venueType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 6)
    private String pincode;

    @Column(name = "address_line1", length = 255)
    private String addressLine1;

    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    @Column(length = 255)
    private String address;

    private Double latitude;
    private Double longitude;

    @Column(name = "year_opened")
    private Integer yearOpened;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "ground_amenities", joinColumns = @JoinColumn(name = "ground_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "amenity", length = 50)
    @Builder.Default
    private Set<GroundAmenity> amenities = new HashSet<>();

    @Column(name = "custom_amenities", length = 200)
    private String customAmenities;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    private Sport sport;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "ground_images", joinColumns = @JoinColumn(name = "ground_id"))
    @Column(name = "image_url", length = 500)
    @OrderColumn(name = "sort_order")
    @Builder.Default
    private List<String> imageUrls = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING_APPROVAL;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("displayOrder ASC")
    @Builder.Default
    private List<GroundCourt> courts = new ArrayList<>();

    @OneToMany(mappedBy = "ground", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("dayOfWeek ASC")
    @Builder.Default
    private List<GroundOperatingHours> operatingHours = new ArrayList<>();
}
