package com.atletry.entity;

import com.atletry.enums.BranchFacility;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "academy_branches")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyBranch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academy_id", nullable = false)
    private Academy academy;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String address;

    @Column(length = 100)
    private String area;

    @Column(length = 100)
    private String city;

    @Column(length = 6)
    private String pincode;

    private Double latitude;
    private Double longitude;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_branch_facilities", joinColumns = @JoinColumn(name = "branch_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "facility", length = 30)
    @Builder.Default
    private List<BranchFacility> facilities = new ArrayList<>();

    @Column(name = "is_residential")
    @Builder.Default
    private boolean isResidential = false;

    @Column(name = "is_primary")
    @Builder.Default
    private boolean isPrimary = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_venue_id")
    private Ground linkedVenue;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_branch_photos", joinColumns = @JoinColumn(name = "branch_id"))
    @Column(name = "photo_url", length = 500)
    @OrderColumn(name = "sort_order")
    @Builder.Default
    private List<String> photoUrls = new ArrayList<>();

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_branch_sports", joinColumns = @JoinColumn(name = "branch_id"))
    @Column(name = "sport_name", length = 100)
    @Builder.Default
    private List<String> sports = new ArrayList<>();

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;
}
