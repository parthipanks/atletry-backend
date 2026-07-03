package com.atletry.repository;

import com.atletry.entity.CoachVerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CoachVerificationLogRepository extends JpaRepository<CoachVerificationLog, Long> {
    List<CoachVerificationLog> findByCoachIdOrderByTimestampAsc(Long coachId);
}
