package com.atletry.repository;

import com.atletry.entity.Academy;
import com.atletry.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcademyRepository extends JpaRepository<Academy, Long> {
    List<Academy> findByOwnerIdOrderByCreatedAtDesc(Long ownerId);
    List<Academy> findByApprovalStatusOrderByCreatedAtDesc(ApprovalStatus status);
    List<Academy> findByIsPublishedTrueAndIsActiveTrueOrderByCreatedAtDesc();
}
