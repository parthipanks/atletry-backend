package com.atletry.repository;

import com.atletry.entity.Match;
import com.atletry.enums.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByStatusInOrderByScheduledAtAsc(List<MatchStatus> statuses);

    List<Match> findBySportIdAndStatusInOrderByScheduledAtAsc(Long sportId, List<MatchStatus> statuses);

    List<Match> findByStatusOrderByCreatedDateDesc(MatchStatus status);
}
