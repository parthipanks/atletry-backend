package com.atletry.repository;

import com.atletry.entity.Sport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SportRepository extends JpaRepository<Sport, Long> {
    List<Sport> findByIsActiveTrueOrderByDisplayOrderAsc();
    Optional<Sport> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}
