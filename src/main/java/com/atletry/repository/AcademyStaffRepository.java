package com.atletry.repository;

import com.atletry.entity.AcademyStaffMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AcademyStaffRepository extends JpaRepository<AcademyStaffMember, Long> {
    List<AcademyStaffMember> findByAcademyIdOrderByIsHeadCoachDescIdAsc(Long academyId);
    Optional<AcademyStaffMember> findByIdAndAcademyId(Long id, Long academyId);
}
