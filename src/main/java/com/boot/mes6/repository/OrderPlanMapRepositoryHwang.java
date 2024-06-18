package com.boot.mes6.repository;

import com.boot.mes6.entity.OrderPlanMap;
import com.boot.mes6.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderPlanMapRepositoryHwang extends JpaRepository<OrderPlanMap, Long> {
    List<OrderPlanMap> findAllByPlan(Plan plan);
}
