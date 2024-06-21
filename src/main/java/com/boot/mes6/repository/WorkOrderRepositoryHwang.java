package com.boot.mes6.repository;

import com.boot.mes6.entity.WorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WorkOrderRepositoryHwang extends JpaRepository<WorkOrder, Long> {

    // 특정 생산계획ID를 가지는 값들 중 공정명이 추출 또는 혼합인 작업지시 찾기
    @Query("SELECT w FROM WorkOrder w WHERE w.plan.planNo = :planNo AND (w.workOrderProcessName = 'A2' OR w.workOrderProcessName = 'B1')")
    WorkOrder findByPlanNoAndProcessNames(@Param("planNo") Long planNo);

    // 특정 생산계획ID를 가지는 모든 작업지시 찾기
    @Query("SELECT w FROM WorkOrder w WHERE w.plan.planNo = :planNo")
    List<WorkOrder> findAllByPlanNo(@Param("planNo") Long planNo);
}
