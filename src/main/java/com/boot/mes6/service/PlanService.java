package com.boot.mes6.service;

import com.boot.mes6.constant.ProductType;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.Plan;
import com.boot.mes6.entity.WorkOrder;
import com.boot.mes6.repository.OrderRepositoryHwang;
import com.boot.mes6.repository.PlanRepository;
import com.boot.mes6.repository.WorkOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {
    private final PlanRepository planRepository;
    private final WorkOrderRepository workOrderRepository;
    private final OrderRepositoryHwang orderRepositoryHwang;

    public Plan createOrMergePlan(Order order) {
        List<Plan> existingPlans = planRepository.findAllByPlan_no(order.getOrder_no());

        if (existingPlans.isEmpty()) {
            return createNewPlan(order);
        } else {
            return mergeWithExistingPlan(order, existingPlans);
        }
    }
//
//    private Plan createNewPlan(Order order) {
//        Plan plan = new Plan();
//        plan.setPlan_production_amount(calculateProductionAmount(order));
//        plan.setPlan_start_date(LocalDateTime.now());
//        plan.setPlan_expect_finish_date(calculateExpectedFinishDate(plan.getPlan_start_date(), plan.getPlan_production_amount()));
//        planRepository.save(plan);
//        createWorkOrders(plan);
//        return plan;
//    }
//    private Plan mergeWithExistingPlan(Order order, List<Plan> existingPlans) {
//        // Assuming we merge with the first plan found for simplicity
//        Plan plan = existingPlans.get(0);
//        plan.setPlan_production_amount(plan.getPlan_production_amount() + calculateProductionAmount(order));
//        plan.setPlan_expect_finish_date(calculateExpectedFinishDate(plan.getPlan_start_date(), plan.getPlan_production_amount()));
//        planRepository.save(plan);
//        updateWorkOrders(plan);
//        return plan;
//    }
//
//    private long calculateProductionAmount(Order order) {
//        // Implement logic to calculate production amount from order
//        return order.getOrder_amount() * 1000; // Simplified calculation
//    }
//
//    private LocalDateTime calculateExpectedFinishDate(LocalDateTime startDate, long productionAmount) {
//        // Implement logic to calculate expected finish date based on production amount
//        return startDate.plusDays(productionAmount / 1000); // Simplified calculation
//    }
//
//    private void createWorkOrders(Plan plan) {
//        // Implement logic to create work orders based on the plan
//        LocalDateTime currentStartTime = plan.getPlan_start_date();
//
//        // Preprocessing 수정해야함
//        WorkOrder workOrder = new WorkOrder();
//        workOrder.setPlan(plan);
//        workOrder.setWork_order_type(ProductType.JUICE);
//        workOrder.setWork_order_process_name(ProcessCode.PRETREATMENT);
//        workOrder.setWorkOrderFacilityName(FacilityName.EXTRACTOR);
//        workOrder.setWorkOrderInput(1333L);
//        workOrder.setWorkOrderOutput(1333L);
//        workOrder.setWorkOrderStartDate(currentStartTime);
//        workOrder.setWorkOrderExpectDate(currentStartTime.plusHours(2));
//        workOrderRepository.save(workOrder);
//
//         Subsequent processes follow similarly
//    }
//
//    private void updateWorkOrders(Plan plan) {
//        // Implement logic to update existing work orders based on the new plan
//        List<WorkOrder> existingWorkOrders = workOrderRepository.findAllByPlanNo(plan.getPlan_no());
//
//        // Update work orders as per the new merged plan details
//    }

}
