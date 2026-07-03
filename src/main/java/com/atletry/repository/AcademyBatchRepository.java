package com.atletry.repository;

import com.atletry.entity.AcademyBatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademyBatchRepository extends JpaRepository<AcademyBatch, Long> {
    List<AcademyBatch> findByAcademyIdOrderByIdAsc(Long academyId);
    Optional<AcademyBatch> findByIdAndAcademyId(Long id, Long academyId);
}
