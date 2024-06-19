package com.boot.mes6.repository;

import com.boot.mes6.entity.OrderPlanMap;
import com.boot.mes6.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderPlanMapRepositoryHwang extends JpaRepository<OrderPlanMap, Long> {

    //  해당 수주번호를 가지는 가장 최신의 생산계획 조회
    @Query(value = "SELECT * FROM order_plan_map o WHERE o.order_no = :orderNo " +
            "ORDER BY o.plan_no DESC LIMIT 1", nativeQuery = true)
    OrderPlanMap findTopByOrderNoOrderByPlanNoDesc(@Param("orderNo") Long orderNo);
}
