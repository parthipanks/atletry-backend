package com.atletry.repository;

import com.atletry.entity.Ground;
import com.atletry.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface GroundRepository extends JpaRepository<Ground, Long> {

    @EntityGraph(attributePaths = {"sport", "createdBy"})
    List<Ground> findByApprovalStatusAndIsActiveTrueOrderByCreatedDateDesc(ApprovalStatus approvalStatus);

    @EntityGraph(attributePaths = {"sport", "createdBy"})
    List<Ground> findByApprovalStatusOrderByCreatedDateDesc(ApprovalStatus approvalStatus);

    @EntityGraph(attributePaths = {"sport", "createdBy"})
    List<Ground> findByCreatedByIdAndIsActiveTrueOrderByCreatedDateDesc(Long userId);
}
