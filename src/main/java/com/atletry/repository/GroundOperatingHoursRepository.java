package com.atletry.repository;

import com.atletry.entity.GroundOperatingHours;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

public interface GroundOperatingHoursRepository extends JpaRepository<GroundOperatingHours, Long> {

    List<GroundOperatingHours> findByGroundIdOrderByDayOfWeek(Long groundId);

    Optional<GroundOperatingHours> findByGroundIdAndDayOfWeek(Long groundId, DayOfWeek dayOfWeek);

    void deleteByGroundId(Long groundId);
}
