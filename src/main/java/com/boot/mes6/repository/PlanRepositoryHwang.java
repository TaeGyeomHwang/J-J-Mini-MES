package com.boot.mes6.repository;

import com.boot.mes6.entity.Plan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface PlanRepositoryHwang extends JpaRepository<Plan, Long> {
    Page<Plan> findAllByPlanStartDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
