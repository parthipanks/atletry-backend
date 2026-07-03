package com.atletry.entity;

import com.atletry.enums.StaffRole;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "academy_staff")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyStaffMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academy_id", nullable = false)
    private Academy academy;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(length = 25)
    private StaffRole role;

    @Column(name = "photo_uri", length = 500)
    private String photoUri;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_staff_sports", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "sport_name", length = 100)
    @Builder.Default
    private List<String> sports = new ArrayList<>();

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "academy_staff_certifications", joinColumns = @JoinColumn(name = "staff_id"))
    @Column(name = "certification", length = 200)
    @Builder.Default
    private List<String> certifications = new ArrayList<>();

    @Column(name = "playing_background", length = 200)
    private String playingBackground;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "linked_coach_id")
    private Coach linkedCoach;

    @Column(name = "is_head_coach")
    @Builder.Default
    private boolean isHeadCoach = false;

    @Column(columnDefinition = "TEXT")
    private String bio;
}
