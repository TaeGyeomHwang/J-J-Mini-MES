package com.boot.mes6.service;

import com.boot.mes6.constant.OrderStatus;
import com.boot.mes6.constant.OrderType;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.dto.OrderFormDto;
import com.boot.mes6.entity.CurrentTime;
import com.boot.mes6.entity.Order;
import com.boot.mes6.repository.CurrentTimeRepositoryHwang;
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
    private final CurrentTimeRepositoryHwang currentTimeRepositoryHwang;

    //  더미 수주 추가
    public Long saveOrder() {
        Order order = new Order();

        order.setOrderType(OrderType.COMPANY);
        order.setOrderProductType(ProductName.CABBAGE_JUICE);
        order.setOrderAmount(123L);
        order.setOrderCustomerName("xx식품");
        order.setOrderDate(LocalDateTime.now());
        System.out.println("현재시각: " + LocalDateTime.now());
        order.setOrderIsEmergency(false);
        order.setOrderStatus(OrderStatus.BEFORE_REGISTER);

        orderRepositoryHwang.save(order);

        return order.getOrderNo();
    }

    //  수주 수동 추가
    public Long saveOrderManual(OrderFormDto orderFormDto) {
        Order order = new Order();
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
}
