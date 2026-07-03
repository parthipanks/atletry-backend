package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "academy_notable_alumni")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyNotableAlumni {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academy_id", nullable = false)
    private Academy academy;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 300)
    private String achievement;

    private Integer year;

    @Column(length = 100)
    private String sport;

    @Column(name = "photo_uri", length = 500)
    private String photoUri;

    @Column(name = "has_consent")
    @Builder.Default
    private boolean consent = false;
}
