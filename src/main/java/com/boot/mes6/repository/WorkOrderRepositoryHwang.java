package com.boot.mes6.repository;

import com.boot.mes6.constant.ProductType;
import com.boot.mes6.entity.WorkOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WorkOrderRepositoryHwang extends JpaRepository<WorkOrder, Long> {

    // 특정 생산계획ID를 가지는 값들 중 공정명이 추출 또는 혼합인 작업지시 찾기
    @Query("SELECT w FROM WorkOrder w WHERE w.plan.planNo = :planNo AND (w.workOrderProcessName = 'A2' OR w.workOrderProcessName = 'B1')")
    WorkOrder findByPlanNoAndProcessNames(@Param("planNo") Long planNo);

    // 특정 생산계획ID를 가지는 모든 작업지시 찾기
    @Query("SELECT w FROM WorkOrder w WHERE w.plan.planNo = :planNo")
    List<WorkOrder> findAllByPlanNo(@Param("planNo") Long planNo);

    //  특정 제품 종류를 생산하는 모든 작업지시 페이징해서 찾기
    Page<WorkOrder> findByWorkOrderType(ProductType workOrderType, PageRequest pageRequest);

    // 특정 제품 종류를 생산하면서 주어진 날짜 범위 내에 있는 작업지시 페이징해서 찾기
    Page<WorkOrder> findByWorkOrderTypeAndWorkOrderStartDateBetween(
            ProductType workOrderType, LocalDateTime startDate, LocalDateTime endDate, PageRequest pageRequest);

    // waiting 상태이고 currentTimeValue가 WorkOrderStartDate와 workOrderExpectDate 사이인 값 찾기
    @Query("SELECT w FROM WorkOrder w " +
            "JOIN CurrentTime c ON c.currentTimeNo = 1 " +
            "WHERE w.workOrderStatus = 'WAITING' " +
            "AND c.currentTimeValue BETWEEN w.workOrderStartDate AND w.workOrderExpectDate")
    List<WorkOrder> findWaitingWorkOrdersWithCurrentTimeInRange();

    // processing 또는 waiting 상태이고 currentTimeValue가 workOrderExpectDate 이상인 값 찾기
    @Query("SELECT w FROM WorkOrder w " +
            "JOIN CurrentTime c ON c.currentTimeNo = 1 " +
            "WHERE (w.workOrderStatus = 'PROCESSING' OR w.workOrderStatus = 'WAITING') " +
            "AND c.currentTimeValue >= w.workOrderExpectDate")
    List<WorkOrder> findProcessingOrWaitingWorkOrdersWithCurrentTimeAfterExpectDate();


}
