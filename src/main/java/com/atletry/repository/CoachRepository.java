package com.atletry.repository;

import com.atletry.entity.Coach;
import com.atletry.enums.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CoachRepository extends JpaRepository<Coach, Long> {
    List<Coach> findByApprovalStatusAndIsActiveTrueOrderByCreatedDateDesc(ApprovalStatus approvalStatus);
    List<Coach> findByApprovalStatusOrderByCreatedDateDesc(ApprovalStatus approvalStatus);
    List<Coach> findByPrimarySportIdAndApprovalStatusAndIsActiveTrueOrderByCreatedDateDesc(Long primarySportId, ApprovalStatus approvalStatus);
}
