package com.atletry.repository;

import com.atletry.entity.CoachProgram;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CoachProgramRepository extends JpaRepository<CoachProgram, Long> {
    List<CoachProgram> findByCoachIdAndIsActiveTrueOrderByDisplayOrderAsc(Long coachId);
    Optional<CoachProgram> findByIdAndCoachId(Long id, Long coachId);
}
