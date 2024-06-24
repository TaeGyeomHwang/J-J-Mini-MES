package com.boot.mes6.service;

import com.boot.mes6.constant.OrderStatus;
import com.boot.mes6.constant.OrderType;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.dto.OrderFormDto;
import com.boot.mes6.entity.CurrentTime;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.Plan;
import com.boot.mes6.repository.CurrentTimeRepositoryHwang;
import com.boot.mes6.repository.OrderPlanMapRepositoryHwang;
import com.boot.mes6.repository.OrderRepositoryHwang;
import com.boot.mes6.repository.PlanRepositoryHwang;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceHwang {
    private final OrderRepositoryHwang orderRepositoryHwang;
    private final OrderPlanMapRepositoryHwang orderPlanMapRepositoryHwang;
    private final PlanRepositoryHwang planRepositoryHwang;
    private final CurrentTimeRepositoryHwang currentTimeRepositoryHwang;

    //  수주 이력 가져오기
    public List<Order> getAllOrder() {
        return orderRepositoryHwang.findAll();
    }

    // 수주 목록 페이징 조회
    public Page<Order> getOrders(Pageable pageable) {
        return orderRepositoryHwang.findAll(pageable);
    }

    //  수주 수동 추가
    public Long saveOrderManual(OrderFormDto orderFormDto) {
        Order order = new Order();
        OrderType orderType = orderFormDto.getOrderType();
        switch (orderType){
            case COMPANY:
                order.setOrderType(orderFormDto.getOrderType());
                order.setOrderProductType(orderFormDto.getProductName());
                order.setOrderAmount(orderFormDto.getOrderAmount());
                order.setOrderCustomerName(orderFormDto.getOrderCustomerName());
                CurrentTime currentTime = currentTimeRepositoryHwang.findById(1L)
                        .orElseThrow(EntityNotFoundException::new);
                order.setOrderDate(currentTime.getCurrentTimeValue());
                if (order.isOrderIsEmergency()){
                    order.setOrderIsEmergency(order.isOrderIsEmergency());
                    order.setOrderStatus(OrderStatus.SHIPPED);
                }else {
                    order.setOrderIsEmergency(order.isOrderIsEmergency());
                    order.setOrderStatus(OrderStatus.BEFORE_REGISTER);
                }
                orderRepositoryHwang.save(order);
            case GENERAL:
                order.setOrderType(orderFormDto.getOrderType());
                order.setOrderProductType(orderFormDto.getProductName());
                order.setOrderAmount(orderFormDto.getOrderAmount());
                order.setOrderCustomerName(orderFormDto.getOrderCustomerName());
                currentTime = currentTimeRepositoryHwang.findById(1L)
                        .orElseThrow(EntityNotFoundException::new);
                order.setOrderDate(currentTime.getCurrentTimeValue());
                if (order.isOrderIsEmergency()){
                    order.setOrderIsEmergency(order.isOrderIsEmergency());
                    order.setOrderStatus(OrderStatus.SHIPPED);
                }else {
                    order.setOrderIsEmergency(order.isOrderIsEmergency());
                    order.setOrderStatus(OrderStatus.BEFORE_REGISTER);
                }

                orderRepositoryHwang.save(order);
        }
        order.setOrderType(orderFormDto.getOrderType());
        order.setOrderProductType(orderFormDto.getProductName());
        order.setOrderAmount(orderFormDto.getOrderAmount());
        order.setOrderCustomerName(orderFormDto.getOrderCustomerName());
        CurrentTime currentTime = currentTimeRepositoryHwang.findById(1L)
                .orElseThrow(EntityNotFoundException::new);
        order.setOrderDate(currentTime.getCurrentTimeValue());
        order.setOrderIsEmergency(order.isOrderIsEmergency());
        order.setOrderStatus(OrderStatus.BEFORE_REGISTER);

        orderRepositoryHwang.save(order);
        return order.getOrderNo();
    }

    //  수주ID로 예상 납품일 수정
    public Long updateOrderExpectDate(Order order, Long planNo) {
        Plan plan = planRepositoryHwang.findById(planNo)
                .orElseThrow(EntityNotFoundException::new);
        order.setOrderExpectShipDate(plan.getPlanExpectFinishDate());
        orderRepositoryHwang.save(order);
        return order.getOrderNo();
    }
}
