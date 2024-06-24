package com.boot.mes6.service;

import com.boot.mes6.constant.OrderStatus;
import com.boot.mes6.constant.OrderType;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.dto.OrderFormDto;
import com.boot.mes6.entity.CurrentProduct;
import com.boot.mes6.entity.CurrentTime;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.Plan;
import com.boot.mes6.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceHwang {
    private final OrderRepositoryHwang orderRepositoryHwang;
    private final OrderPlanMapRepositoryHwang orderPlanMapRepositoryHwang;
    private final PlanRepositoryHwang planRepositoryHwang;
    private final CurrentTimeRepositoryHwang currentTimeRepositoryHwang;
    private final CurrentProductRepositoryHwang currentProductRepositoryHwang;

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
        switch (orderType) { //  업체 수주인지 일반 소비자 수주인지 확인
            case COMPANY:
                order.setOrderType(orderFormDto.getOrderType());
                order.setOrderProductType(orderFormDto.getProductName());
                order.setOrderAmount(orderFormDto.getOrderAmount());
                order.setOrderCustomerName(orderFormDto.getOrderCustomerName());
                CurrentTime currentTime = currentTimeRepositoryHwang.findById(1L)
                        .orElseThrow(EntityNotFoundException::new);
                order.setOrderDate(currentTime.getCurrentTimeValue());
                //  긴급 수주라면
                if (orderFormDto.isOrderIsEmergency()) {
                    //  안전 재고량이 부족할 경우 수주 받지 않음
                    CurrentProduct currentProduct = currentProductRepositoryHwang.findByCurrentProductName(orderFormDto.getProductName());
                    if (orderFormDto.getOrderAmount() > currentProduct.getCurrentProductSafeAmount()) {
                        return -1L;
                    }
                    order.setOrderExpectShipDate(currentTime.getCurrentTimeValue());
                    order.setOrderOutDate(currentTime.getCurrentTimeValue());
                    order.setOrderIsEmergency(orderFormDto.isOrderIsEmergency());
                    order.setOrderStatus(OrderStatus.SHIPPED);
                    /*  여기에 완제품 출고 메소드 삽입   */
                } else {
                    order.setOrderIsEmergency(orderFormDto.isOrderIsEmergency());
                    order.setOrderStatus(OrderStatus.BEFORE_REGISTER);
                }
                orderRepositoryHwang.save(order);
                break;
            case GENERAL:
                CurrentProduct currentProduct = currentProductRepositoryHwang.findByCurrentProductName(orderFormDto.getProductName());
                if (orderFormDto.getOrderAmount() > currentProduct.getCurrentProductSafeAmount()) {
                    return -1L;
                }
                order.setOrderType(orderFormDto.getOrderType());
                order.setOrderProductType(orderFormDto.getProductName());
                order.setOrderAmount(orderFormDto.getOrderAmount());
                order.setOrderCustomerName(orderFormDto.getOrderCustomerName());
                currentTime = currentTimeRepositoryHwang.findById(1L)
                        .orElseThrow(EntityNotFoundException::new);
                order.setOrderDate(currentTime.getCurrentTimeValue());
                order.setOrderExpectShipDate(currentTime.getCurrentTimeValue());
                order.setOrderOutDate(currentTime.getCurrentTimeValue());
                order.setOrderIsEmergency(orderFormDto.isOrderIsEmergency());
                order.setOrderStatus(OrderStatus.SHIPPED);
                /*  여기에 완제품 출고 메소드 삽입   */
                orderRepositoryHwang.save(order);
                break;
        }
        orderRepositoryHwang.save(order);
        return order.getOrderNo();
    }

    //  수주ID로 예상 납품일 수정
    public Long updateOrderExpectDate(Order order, Long planNo) {
        if(order.getOrderExpectShipDate()==null){
            Plan plan = planRepositoryHwang.findById(planNo)
                    .orElseThrow(EntityNotFoundException::new);
            order.setOrderExpectShipDate(plan.getPlanExpectFinishDate());
            orderRepositoryHwang.save(order);
        }
        return order.getOrderNo();
    }
}
