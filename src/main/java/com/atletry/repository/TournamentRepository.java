package com.atletry.repository;

import com.atletry.entity.Tournament;
import com.atletry.enums.TournamentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface TournamentRepository extends JpaRepository<Tournament, Long> {

    List<Tournament> findByStatusInOrderByStartsAtAsc(List<TournamentStatus> statuses);

    List<Tournament> findBySportIdAndStatusInOrderByStartsAtAsc(Long sportId, List<TournamentStatus> statuses);

    List<Tournament> findByStatusOrderByCreatedDateDesc(TournamentStatus status);
}
