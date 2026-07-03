package com.atletry.repository;

import com.atletry.entity.GroundCourt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroundCourtRepository extends JpaRepository<GroundCourt, Long> {

    List<GroundCourt> findByGroundIdAndIsActiveTrueOrderByDisplayOrderAsc(Long groundId);

    Optional<GroundCourt> findByIdAndGroundId(Long courtId, Long groundId);
}
