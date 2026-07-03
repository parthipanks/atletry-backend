package com.atletry.repository;

import com.atletry.entity.AcademyVerificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademyVerificationLogRepository extends JpaRepository<AcademyVerificationLog, Long> {
    List<AcademyVerificationLog> findByAcademyIdOrderByTimestampAsc(Long academyId);
}
