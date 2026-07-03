package com.atletry.repository;

import com.atletry.entity.UserCoach;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserCoachRepository extends JpaRepository<UserCoach, Long> {
    List<UserCoach> findByUserIdOrderByCreatedDateDesc(Long userId);
    boolean existsByUserIdAndCoachId(Long userId, Long coachId);
    Optional<UserCoach> findByUserIdAndCoachId(Long userId, Long coachId);
}
