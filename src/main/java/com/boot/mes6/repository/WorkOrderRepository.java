package com.boot.mes6.repository;

import com.boot.mes6.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, Long> {
//    List<WorkOrder> findAllByPlanNo(Long planNo);
}
