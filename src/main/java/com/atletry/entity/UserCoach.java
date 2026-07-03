package com.atletry.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;


@Entity
@Table(name = "user_coaches",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "coach_id"}))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class UserCoach {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "coach_id", nullable = false)
    private Coach coach;

    @CreationTimestamp
    @Column(name = "created_date", nullable = false, updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;
}
