package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "academy_affiliations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AcademyAffiliation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "academy_id", nullable = false)
    private Academy academy;

    @Column(nullable = false, length = 100)
    private String body;

    @Column(nullable = false, length = 200)
    private String label;

    @Column(name = "registration_number", length = 100)
    private String registrationNumber;

    @Column(name = "document_uri", length = 500)
    private String documentUri;

    @Column(name = "is_verified")
    @Builder.Default
    private boolean isVerified = false;
}
