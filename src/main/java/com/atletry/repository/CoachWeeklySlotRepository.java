package com.atletry.repository;

import com.atletry.entity.CoachWeeklySlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;


public interface CoachWeeklySlotRepository extends JpaRepository<CoachWeeklySlot, Long> {
    List<CoachWeeklySlot> findByCoachIdOrderByDayOfWeek(Long coachId);
    Optional<CoachWeeklySlot> findByCoachIdAndDayOfWeek(Long coachId, DayOfWeek dayOfWeek);
}
