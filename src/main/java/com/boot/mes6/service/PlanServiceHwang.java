package com.boot.mes6.service;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.constant.ProductType;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.OrderPlanMap;
import com.boot.mes6.entity.Plan;
import com.boot.mes6.entity.WorkOrder;
import com.boot.mes6.repository.OrderPlanMapRepositoryHwang;
import com.boot.mes6.repository.OrderRepositoryHwang;
import com.boot.mes6.repository.PlanRepositoryHwang;
import com.boot.mes6.repository.WorkOrderRepositoryHwang;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class PlanServiceHwang {

    private final WorkOrderServiceHwang workOrderServiceHwang;

    private final PlanRepositoryHwang planRepository;
    private final WorkOrderRepositoryHwang workOrderRepositoryHwang;
    private final OrderRepositoryHwang orderRepositoryHwang;
    private final OrderPlanMapRepositoryHwang orderPlanMapRepositoryHwang;

    //  공휴일 set
    private static final Set<LocalDate> publicHolidays = Set.of(
            // 공휴일을 여기에 추가
            LocalDate.of(2024, 1, 1),
            LocalDate.of(2024, 3, 1)
    );

    //  최대 생산량 상수
    private static final Long MAX_JUICE_AMOUNT = 333L;
    private static final Long MAX_JELLY_AMOUNT = 160L;

    //  생산계획 생성 또는 합병 메소드
    public Long createOrMergePlan(Order order) {
        LocalDateTime productionDate = null;
        Long orderAmountWithDefective = (long) (order.getOrderAmount() + Math.ceil(order.getOrderAmount() * 0.03));
        Long planNo = null;

        // 원자재 발주 접수 전인 동일 제품 수주 찾기
        Order existOrder = orderRepositoryHwang.findTopByOrderProductTypeOrderByOrderNoDesc(order.getOrderProductType().name(), order.getOrderNo());

        //  원자재 발주 접수 전인 동일 제품 생산계획이 없을 경우
        if (existOrder == null) {
            //  추가된 수주의 제품명에 따라 대기중인 동일한 ProductType의 수주가 있는지 확인
            ProductName addedOrderProductType = order.getOrderProductType();
            Order existWaintingOrder = null;
            switch (addedOrderProductType) {
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
            if (existWaintingOrder != null) {
                //  해당 수주번호를 가지는 최신 생산계획ID로 추출 또는 혼합 공정인 작업지시 찾기
                OrderPlanMap orderPlanMap = orderPlanMapRepositoryHwang.findTopByOrderNoOrderByPlanNoDesc(existWaintingOrder.getOrderNo());
                if (orderPlanMap != null) {
                    WorkOrder foundWorkOrder = workOrderRepositoryHwang.findByPlanNoAndProcessNames(orderPlanMap.getPlan().getPlanNo());
                    //  생산 시작일로 생산계획&작업지시 생성
                    productionDate = foundWorkOrder.getWorkOrderExpectDate().minusHours(1);
                    planNo = calculateAmountAndCreateWorkOrder(order, orderAmountWithDefective, productionDate);
                }
                return planNo;

            } else {  //  동일한 ProductType의 수주가 없다면
                //  생산 시작일로 생산계획&작업지시 생성
                productionDate = calculateMaterialInDate(order);
                planNo = calculateAmountAndCreateWorkOrder(order, orderAmountWithDefective, productionDate);
            }

            return planNo;
        } else {  //  원자재 발주 접수 전인 동일 제품 생산계획이 있을 경우 기존 수주에 합친다.
            System.out.println("찾은 수주 번호: " + existOrder.getOrderNo());
            //  매핑 테이블로 해당 수주 가지는 가장 최신의 생산계획 ID 찾아서 생산량 구한 후,
            OrderPlanMap orderPlanMap = orderPlanMapRepositoryHwang.findTopByOrderNoOrderByPlanNoDesc(existOrder.getOrderNo());
            Plan existPlan = orderPlanMap.getPlan();
            Long planAmount = existPlan.getPlanProductionAmount();
            //  해당 생산계획의 생산량 + 현재 수주의 생산량 > 최대 capa인지 확인해서,
            Long summedAmount = planAmount + order.getOrderAmount();
            planNo = existPlan.getPlanNo();
            productionDate = existPlan.getPlanStartDate();
            do {
                Long planProductionAmount = 0L;
                ProductName addedOrderProductType = order.getOrderProductType();
                ProductType productType = null;
                switch (addedOrderProductType) {
                    case CABBAGE_JUICE:
                    case GARLIC_JUICE:
                        if (summedAmount > MAX_JUICE_AMOUNT) {
                            planProductionAmount = MAX_JUICE_AMOUNT;
                        } else {
                            planProductionAmount = summedAmount;
                        }
                        productType = ProductType.JUICE;
                        break;
                    case POMEGRANATE_JELLY:
                    case PLUM_JELLY:
                        if (summedAmount > MAX_JELLY_AMOUNT) {
                            planProductionAmount = MAX_JELLY_AMOUNT;
                        } else {
                            planProductionAmount = summedAmount;
                        }
                        productType = ProductType.JELLY;
                        break;
                }
                Plan existPlanOrNewPlan = planRepository.findById(planNo)
                        .orElseThrow(EntityNotFoundException::new);
                existPlanOrNewPlan.setPlanProductionAmount(planProductionAmount);

                //  매핑테이블 생성
                OrderPlanMap newOrderPlanMap = new OrderPlanMap();
                newOrderPlanMap.setPlan(planRepository.findById(planNo)
                        .orElseThrow(EntityNotFoundException::new));
                newOrderPlanMap.setOrder(order);
                orderPlanMapRepositoryHwang.save(newOrderPlanMap);

                //  기존 작업지시 삭제
                List<Long> deletedWorkOrderList = workOrderServiceHwang.deleteWorkOrderWithPlanNo(planNo);

                //  생산계획ID로 작업지시 생성
                List<Long> workOrderNoList = workOrderServiceHwang.createNewWorkOrder(planNo, productType);
                summedAmount -= planProductionAmount;

                if(summedAmount>0){
                    switch (addedOrderProductType){
                        case CABBAGE_JUICE:
                        case GARLIC_JUICE:
                            productionDate = productionDate.plusHours(25);
                            break;
                        case POMEGRANATE_JELLY:
                        case PLUM_JELLY:
                            productionDate = productionDate.plusHours(23);
                            break;
                    }
                    planNo = createNewPlanWithDate(productionDate);
                }
            } while (summedAmount > 0);
            return planNo;
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

    //  생산계획 객체 생성
    private Long createNewPlan(Long planProductionAmount, LocalDateTime planStartDate) {
        Plan plan = new Plan();
        plan.setPlanProductionAmount(planProductionAmount);
        plan.setPlanStartDate(planStartDate);
        planRepository.save(plan);
        return plan.getPlanNo();
    }
    private Long createNewPlanWithDate(LocalDateTime planStartDate){
        Plan plan = new Plan();
        plan.setPlanStartDate(planStartDate);
        planRepository.save(plan);
        return plan.getPlanNo();
    }

    //  생산계획생성 후 매핑테이블 생성&생산계획마다 작업지시 생성 메소드 호출 함수
    private Long calculateAmountAndCreateWorkOrder(Order order, Long orderAmountWithDefective, LocalDateTime productionDate) {
        Long planNo = 0L;
        do {
            Long planProductionAmount = 0L;
            ProductName productName = order.getOrderProductType();
            ProductType productType = null;
            switch (productName) {
                case CABBAGE_JUICE:
                case GARLIC_JUICE:
                    if (orderAmountWithDefective > MAX_JUICE_AMOUNT) {
                        planProductionAmount = MAX_JUICE_AMOUNT;
                    } else {
                        planProductionAmount = orderAmountWithDefective;
                    }
                    productType = ProductType.JUICE;
                    break;
                case POMEGRANATE_JELLY:
                case PLUM_JELLY:
                    if (orderAmountWithDefective > MAX_JELLY_AMOUNT) {
                        planProductionAmount = MAX_JELLY_AMOUNT;
                    } else {
                        planProductionAmount = orderAmountWithDefective;
                    }
                    productType = ProductType.JELLY;
                    break;
            }
            planNo = createNewPlan(planProductionAmount, productionDate);

            //  매핑테이블 생성
            OrderPlanMap orderPlanMap = new OrderPlanMap();
            orderPlanMap.setPlan(planRepository.findById(planNo)
                    .orElseThrow(EntityNotFoundException::new));
            orderPlanMap.setOrder(order);
            orderPlanMapRepositoryHwang.save(orderPlanMap);

            //  작업지시 생성
            List<Long> workOrderNoList = workOrderServiceHwang.createNewWorkOrder(planNo, productType);
            switch (productName){
                case CABBAGE_JUICE:
                case GARLIC_JUICE:
                    productionDate = productionDate.plusHours(25);
                    break;
                case POMEGRANATE_JELLY:
                case PLUM_JELLY:
                    productionDate = productionDate.plusHours(23);
                    break;
            }
            orderAmountWithDefective -= planProductionAmount;
        } while (orderAmountWithDefective > 0);

        return planNo;
    }

}
