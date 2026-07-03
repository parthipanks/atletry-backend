package com.atletry.repository;

import com.atletry.entity.UserSport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserSportRepository extends JpaRepository<UserSport, Long> {
    List<UserSport>   findByUserId(Long userId);
    Optional<UserSport> findByUserIdAndSportId(Long userId, Long sportId);
    int  countByUserId(Long userId);
    boolean existsByUserIdAndSportId(Long userId, Long sportId);
    void deleteAllByUserId(Long userId);
}
