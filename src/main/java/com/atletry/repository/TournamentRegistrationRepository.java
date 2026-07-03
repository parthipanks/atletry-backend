package com.atletry.repository;

import com.atletry.entity.TournamentRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TournamentRegistrationRepository extends JpaRepository<TournamentRegistration, Long> {

    Optional<TournamentRegistration> findByTournamentIdAndUserId(Long tournamentId, Long userId);

    int countByTournamentId(Long tournamentId);

    boolean existsByTournamentIdAndUserId(Long tournamentId, Long userId);
}
