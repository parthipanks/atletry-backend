package com.atletry.repository;

import com.atletry.entity.CourtPricingTier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourtPricingTierRepository extends JpaRepository<CourtPricingTier, Long> {

    List<CourtPricingTier> findByCourtIdOrderByCreatedDateAsc(Long courtId);

    Optional<CourtPricingTier> findByIdAndCourtId(Long tierId, Long courtId);
}
