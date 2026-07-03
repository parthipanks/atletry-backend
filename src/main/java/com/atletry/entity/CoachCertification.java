package com.atletry.entity;

import com.atletry.enums.CertificationLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDate;


@Entity
@Table(name = "coach_certifications")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(name = "catalog_id", length = 100)
    private String catalogId;

    @Column(nullable = false, length = 200)
    private String name;

    @Column(nullable = false, length = 150)
    private String organization;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private CertificationLevel level;

    @Column(name = "year_obtained")
    private Integer yearObtained;

    @Column(name = "expires_at")
    private LocalDate expiresAt;

    @Column(name = "certificate_number", length = 100)
    private String certificateNumber;

    @Column(name = "document_uri", length = 500)
    private String documentUri;

    @Column(name = "is_verified")
    @Builder.Default
    private boolean isVerified = false;

    @Column(name = "verified_at")
    private Instant verifiedAt;
}
