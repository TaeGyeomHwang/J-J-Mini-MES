package com.boot.mes6.service;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.constant.ProductType;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.OrderPlanMap;
import com.boot.mes6.entity.Plan;
import com.boot.mes6.entity.WorkOrder;
import com.boot.mes6.repository.OrderPlanMapRepositoryHwang;
import com.boot.mes6.repository.OrderRepositoryHwang;
import com.boot.mes6.repository.PlanRepository;
import com.boot.mes6.repository.WorkOrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanService {

    private final WorkOrderService workOrderService;

    private final PlanRepository planRepository;
    private final WorkOrderRepository workOrderRepository;
    private final OrderRepositoryHwang orderRepositoryHwang;
    private final OrderPlanMapRepositoryHwang orderPlanMapRepositoryHwang;

    private static final Set<LocalDate> publicHolidays = Set.of(
            // 공휴일을 여기에 추가
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 3, 1)
    );

    private static final Long MAX_JUICE_AMOUNT = 333L;
    private static final Long MAX_JELLY_AMOUNT = 160L;

    public Plan createOrMergePlan(Order order) {
        LocalDateTime productionDate = null;
        Long orderAmountWithDefective = (long) (order.getOrderAmount() + Math.ceil(order.getOrderAmount()*0.03));
//        Long orderAmountWithDefective = null;

        // 원자재 발주 접수 전인 수주 찾기
        Order existOrder = orderRepositoryHwang.findTopByOrderProductTypeOrderByOrderNoDesc(order.getOrderProductType().name(),order.getOrderNo());

        //  원자재 발주 접수 전인 동일 제품 생산계획이 없을 경우
        if (existOrder==null){
            //  추가된 수주의 제품명에 따라 대기중인 동일한 ProductType의 수주가 있는지 확인
            ProductName addedOrderProductType = order.getOrderProductType();
            Order existWaintingOrder = null;
            switch (addedOrderProductType){
                case CABBAGE_JUICE:
                    existWaintingOrder = orderRepositoryHwang.findTopGarlicJuiceBeforeRegisterOrder(order.getOrderNo());
                    break;
                case GARLIC_JUICE:
                    existWaintingOrder = orderRepositoryHwang.findTopCabbageJuiceBeforeRegisterOrder(order.getOrderNo());
                    break;
                case POMEGRANATE_JELLY:
                    existWaintingOrder = orderRepositoryHwang.findTopPlumJellyBeforeRegisterOrder(order.getOrderNo());
                    break;
                case PLUM_JELLY:
                    existWaintingOrder = orderRepositoryHwang.findTopPomegranateJellyBeforeRegisterOrder(order.getOrderNo());
                    break;
            }
            //  해당 수주가 있다면 
            if (existWaintingOrder!=null) {
                //  해당 수주번호를 가지는 최신 생산계획ID로 추출 또는 혼합 공정인 작업지시 찾기
                OrderPlanMap orderPlanMap = orderPlanMapRepositoryHwang.findTopByOrderNoOrderByPlanNoDesc(existWaintingOrder.getOrderNo());
                if (orderPlanMap!=null){
                    WorkOrder foundWorkOrder = workOrderRepository.findByPlanNoAndProcessNames(orderPlanMap.getPlan().getPlanNo());
                    //  생산 시작일 설정
                    productionDate = foundWorkOrder.getWorkOrderExpectDate().minusHours(1);
                }
            }else{  //  동일한 ProductType의 수주가 없다면
                //  생산계획 생성
                productionDate = calculateMaterialInDate(order);
                do{
                    Long planProductionAmount = 0L;
                    ProductName productName = order.getOrderProductType();
                    ProductType productType = null;
                    switch (productName){
                        case CABBAGE_JUICE:
                        case GARLIC_JUICE:
                            if (orderAmountWithDefective>MAX_JUICE_AMOUNT){
                                planProductionAmount = MAX_JUICE_AMOUNT;
                            }else {
                                planProductionAmount = orderAmountWithDefective;
                            }
                            productType = ProductType.JUICE;
                            break;
                        case POMEGRANATE_JELLY:
                        case PLUM_JELLY:
                            if (orderAmountWithDefective>MAX_JELLY_AMOUNT){
                                planProductionAmount = MAX_JELLY_AMOUNT;
                            }else {
                                planProductionAmount = orderAmountWithDefective;
                            }
                            productType = ProductType.JELLY;
                            break;
                    }
                    Long planNo = createNewPlan(planProductionAmount, productionDate);
                    Long workOrderNo = workOrderService.createNewWorkOrder(planNo,productType);
                    productionDate = productionDate.plusHours(25);
                    orderAmountWithDefective -= planProductionAmount;
                }while(orderAmountWithDefective>0);
                Long planAmount = calculatePlanProductionAmount(order, orderAmountWithDefective);
            }

            return createNewPlan(order);
        }else{  //  원자재 발주 접수 전인 동일 제품 생산계획이 있을 경우
            System.out.println("찾은 수주 번호: "+existOrder.getOrderNo());
//            return mergeWithExistingPlan(order, existOrder);
            return new Plan();
        }
    }

    //  영업일인지 판단
    private boolean isBusinessDay(LocalDate date) {
        DayOfWeek dayOfWeek = date.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY && !publicHolidays.contains(date);
    }
    //  영업일 계산
    private LocalDate getNextBusinessDay(LocalDate date) {
        LocalDate nextBusinessDay = date;
        do {
            nextBusinessDay = nextBusinessDay.plusDays(1);
        } while (!isBusinessDay(nextBusinessDay));
        return nextBusinessDay;
    }
    //  원자재 입고 시간 계산 함수
    private LocalDateTime calculateMaterialInDate(Order order) {
        ProductName productName = order.getOrderProductType();
        LocalDateTime orderDateTime = order.getOrderDate();
        LocalDate orderDate = orderDateTime.toLocalDate();
        LocalDateTime materialOrderDateTime = null;

        switch (productName) {
            case CABBAGE_JUICE:
            case GARLIC_JUICE:
                if (isBusinessDay(orderDate) && orderDateTime.toLocalTime().isBefore(LocalTime.NOON)) {
                    materialOrderDateTime = orderDate.atTime(12, 0);
                } else {
                    LocalDate nextBusinessDay = getNextBusinessDay(orderDate);
                    materialOrderDateTime = nextBusinessDay.atTime(12, 0);
                }
                break;
            case POMEGRANATE_JELLY:
            case PLUM_JELLY:
                if (isBusinessDay(orderDate) && orderDateTime.toLocalTime().isBefore(LocalTime.of(15, 0))) {
                    materialOrderDateTime = orderDate.atTime(15, 0);
                } else {
                    LocalDate nextBusinessDay = getNextBusinessDay(orderDate);
                    materialOrderDateTime = nextBusinessDay.atTime(15, 0);
                }
                break;
        }

        LocalDateTime materialInDate = null;
        if (productName == ProductName.CABBAGE_JUICE || productName == ProductName.GARLIC_JUICE) {
            materialInDate = materialOrderDateTime.plusDays(2);
        } else if (productName == ProductName.POMEGRANATE_JELLY || productName == ProductName.PLUM_JELLY) {
            materialInDate = materialOrderDateTime.plusDays(3);
        }

        return materialInDate;
    }

    private Long calculatePlanProductionAmount(Order order, Long amountWithDefection){
        ProductName productName = order.getOrderProductType();
        Long planProductionAmount = 0L;
        switch (productName){
            case CABBAGE_JUICE:
            case GARLIC_JUICE:
                if (amountWithDefection>MAX_JUICE_AMOUNT){
                    planProductionAmount = MAX_JUICE_AMOUNT;
                }else {
                    planProductionAmount = amountWithDefection;
                }
                break;
            case POMEGRANATE_JELLY:
            case PLUM_JELLY:
                if (amountWithDefection>MAX_JELLY_AMOUNT){
                    planProductionAmount = MAX_JELLY_AMOUNT;
                }else {
                    planProductionAmount = amountWithDefection;
                }
                break;
        }
        return planProductionAmount;
    }

    private Long createNewPlan(Long planProductionAmount, LocalDateTime planStartDate){
        Plan plan = new Plan();
        plan.setPlanProductionAmount(planProductionAmount);
        plan.setPlanStartDate(planStartDate);
        planRepository.save(plan);
        return plan.getPlanNo();
    }
//    private Plan createNewPlan(Order order) {
//        Plan plan = new Plan();
//        plan.setPlan_production_amount(calculateProductionAmount(order));
//        plan.setPlan_start_date(LocalDateTime.now());
//        plan.setPlan_expect_finish_date(calculateExpectedFinishDate(plan.getPlan_start_date(), plan.getPlan_production_amount()));
//        planRepository.save(plan);
//        createWorkOrders(plan);
//        return plan;
//    }
//    private Plan mergeWithExistingPlan(Order order, Order existingPlans) {
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
