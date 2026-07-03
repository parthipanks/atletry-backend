package com.atletry.repository;

import com.atletry.entity.MatchParticipant;
import com.atletry.enums.ParticipantStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MatchParticipantRepository extends JpaRepository<MatchParticipant, Long> {

    Optional<MatchParticipant> findByMatchIdAndUserId(Long matchId, Long userId);

    List<MatchParticipant> findByUserIdOrderByMatch_ScheduledAtAsc(Long userId);

    int countByMatchIdAndStatus(Long matchId, ParticipantStatus status);

    boolean existsByMatchIdAndUserId(Long matchId, Long userId);
}
