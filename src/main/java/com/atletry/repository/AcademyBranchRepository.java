package com.atletry.repository;

import com.atletry.entity.AcademyBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademyBranchRepository extends JpaRepository<AcademyBranch, Long> {
    List<AcademyBranch> findByAcademyIdOrderByIsPrimaryDescIdAsc(Long academyId);
    Optional<AcademyBranch> findByIdAndAcademyId(Long id, Long academyId);
}
