package com.atletry.repository;

import com.atletry.entity.NotificationTemplate;
import com.atletry.enums.NotificationEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {

    Optional<NotificationTemplate> findByEventType(NotificationEventType eventType);

    Optional<NotificationTemplate> findByEventTypeAndIsActiveTrue(NotificationEventType eventType);

    List<NotificationTemplate> findAllByOrderByCreatedDateDesc();

    boolean existsByEventType(NotificationEventType eventType);
}
