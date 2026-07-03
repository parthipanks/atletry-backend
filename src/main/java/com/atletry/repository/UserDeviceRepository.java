package com.atletry.repository;

import com.atletry.entity.UserDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserDeviceRepository extends JpaRepository<UserDevice, Long> {

    List<UserDevice> findByUserId(Long userId);

    Optional<UserDevice> findByUserIdAndDeviceId(Long userId, String deviceId);

    List<UserDevice> findByFcmToken(String fcmToken);

    List<UserDevice> findAllByIsActiveTrue();

    boolean existsByUserIdAndDeviceId(Long userId, String deviceId);
}
