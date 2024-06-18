package com.boot.mes6.service;

import com.boot.mes6.constant.OrderStatus;
import com.boot.mes6.constant.OrderType;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.Order;
import com.boot.mes6.repository.OrderRepositoryHwang;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceHwang {
    private final OrderRepositoryHwang orderRepositoryHwang;

    public Long saveOrder(){
        Order order = new Order();
        order.setOrder_type(OrderType.COMPANY);
        order.setOrder_product_type(ProductName.CABBAGE_JUICE);
        order.setOrder_amount(123L);
        order.setOrder_customer_name("xx식품");
        order.setOrder_date(LocalDateTime.now());
        System.out.println("현재시각: "+LocalDateTime.now());
        order.setOrder_is_emergency(false);
        order.setOrder_status(OrderStatus.BEFORE_REGISTER);
        orderRepositoryHwang.save(order);
        return order.getOrder_no();
    }
}
