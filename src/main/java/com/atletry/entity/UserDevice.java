package com.atletry.entity;

import com.atletry.enums.DevicePlatform;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.ZonedDateTime;


@Entity
@Table(
    name = "user_devices",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "device_id"})
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class UserDevice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "device_id", nullable = false, length = 255)
    private String deviceId;

    @Column(name = "fcm_token", nullable = false, length = 500)
    private String fcmToken;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform", nullable = false, length = 10)
    private DevicePlatform platform;

    @Column(name = "is_active")
    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(name = "created_date", updatable = false)
    private ZonedDateTime createdDate;

    @UpdateTimestamp
    @Column(name = "updated_date")
    private ZonedDateTime updatedDate;
}
