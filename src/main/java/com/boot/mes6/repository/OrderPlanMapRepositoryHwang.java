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

    //  해당 생산계획번호를 가지는 수주번호 리스트 조회
    @Query(value = "SELECT * FROM order_plan_map o WHERE o.plan_no = :planNo ", nativeQuery = true)
    List<OrderPlanMap> findListByPlanNo(@Param("planNo") Long planNo);

    // 아직 생산계획이 남아있는 수주를 제외한 완료될 수주의 리스트 조회
    @Query(value = "SELECT * FROM order_plan_map o WHERE o.plan_no = :planNo " +
            "AND o.order_no NOT IN (" +
            "  SELECT order_no FROM (" +
            "    SELECT o2.order_no FROM order_plan_map o2 " +
            "    WHERE o2.plan_no = :planNo " +
            "    ORDER BY o2.order_plan_map_no DESC LIMIT 1" +
            "  ) subquery " +
            "  WHERE (" +
            "    SELECT o3.plan_no FROM order_plan_map o3 " +
            "    WHERE o3.order_no = subquery.order_no " +
            "    ORDER BY o3.order_plan_map_no DESC LIMIT 1" +
            "  ) <> :planNo" +
            ")", nativeQuery = true)
    List<OrderPlanMap> findFilteredByPlanNo(@Param("planNo") Long planNo);
}
