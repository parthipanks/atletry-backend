package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "coach_educations")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class CoachEducation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @Column(nullable = false, length = 200)
    private String qualification;

    @Column(nullable = false, length = 200)
    private String institution;

    private Integer year;

    @Column(name = "document_uri", length = 500)
    private String documentUri;
}
