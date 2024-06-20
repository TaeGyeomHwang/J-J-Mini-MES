package com.boot.mes6.service;

import com.boot.mes6.constant.OrderStatus;
import com.boot.mes6.constant.OrderType;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.dto.OrderPlanMapDto;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.OrderPlanMap;
import com.boot.mes6.entity.Plan;
import com.boot.mes6.repository.OrderPlanMapRepositoryHwang;
import com.boot.mes6.repository.OrderRepositoryHwang;
import com.boot.mes6.repository.PlanRepositoryHwang;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceHwang {
    private final OrderRepositoryHwang orderRepositoryHwang;
    private final OrderPlanMapRepositoryHwang orderPlanMapRepositoryHwang;
    private final PlanRepositoryHwang planRepository;

    public Long saveOrder(){
        Order order = new Order();

        order.setOrderType(OrderType.COMPANY);
        order.setOrderProductType(ProductName.CABBAGE_JUICE);
        order.setOrderAmount(123L);
        order.setOrderCustomerName("xx식품");
        order.setOrderDate(LocalDateTime.now());
        System.out.println("현재시각: "+LocalDateTime.now());
        order.setOrderIsEmergency(false);
        order.setOrderStatus(OrderStatus.BEFORE_REGISTER);

        orderRepositoryHwang.save(order);

        return order.getOrderNo();
    }

    public Long saveOrderPlanMap(Long orderNo, Long planNo){
        OrderPlanMap orderPlanMap = new OrderPlanMap();

        Order order = orderRepositoryHwang.findById(orderNo)
                .orElseThrow(EntityNotFoundException::new);
        Plan plan = planRepository.findById(planNo)
                .orElseThrow(EntityNotFoundException::new);

        orderPlanMap.setOrder(order);
        orderPlanMap.setPlan(plan);

        orderPlanMapRepositoryHwang.save(orderPlanMap);

        return orderPlanMap.getOrderPlanMapNo();
    }

    public OrderPlanMapDto saveOrderDto(Order order){
        OrderPlanMapDto orderPlanMapDto = new OrderPlanMapDto();
        orderPlanMapDto.setOrderNo(order.getOrderNo());
        orderPlanMapDto.setProductName(order.getOrderProductType());
        orderPlanMapDto.setOrderAmount(order.getOrderAmount());

        return orderPlanMapDto;
    }
}
