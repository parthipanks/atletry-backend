package com.atletry.repository;

import com.atletry.entity.SportActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SportActivityLogRepository extends JpaRepository<SportActivityLog, Long> {
    List<SportActivityLog> findByUserIdOrderByCreatedDateDesc(Long userId);
}
