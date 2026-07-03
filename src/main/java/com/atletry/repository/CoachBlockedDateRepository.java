package com.atletry.repository;

import com.atletry.entity.CoachBlockedDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CoachBlockedDateRepository extends JpaRepository<CoachBlockedDate, Long> {
    List<CoachBlockedDate> findByCoachIdOrderByDateAsc(Long coachId);
    Optional<CoachBlockedDate> findByIdAndCoachId(Long id, Long coachId);
}
