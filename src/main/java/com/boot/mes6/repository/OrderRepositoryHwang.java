package com.boot.mes6.repository;

import com.boot.mes6.constant.OrderStatus;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepositoryHwang extends JpaRepository<Order, Long> {

    // 원자재 발주 접수 전 & 동일한 제품명인 수주 중 제일 최신 수주 찾는 쿼리
    @Query(value = "SELECT * FROM orders o WHERE o.order_status = 'BEFORE_REGISTER' " +
            "AND o.order_product_type = :productType " +
            "AND o.order_no <> :orderNo " +
            "ORDER BY o.order_no DESC LIMIT 1", nativeQuery = true)
    Order findTopByOrderProductTypeOrderByOrderNoDesc(
            @Param("productType") String productType,
            @Param("orderNo") Long orderNo);

    //  대기중인 수주 중 제품명이 양배추즙인 최신 수주
    @Query(value = "SELECT * FROM orders o WHERE o.order_status = 'BEFORE_REGISTER' " +
            "AND o.order_product_type = 'CABBAGE_JUICE' " +
            "AND o.order_no <> :orderNo " +
            "ORDER BY o.order_no DESC LIMIT 1", nativeQuery = true)
    Order findTopCabbageJuiceBeforeRegisterOrder(
            @Param("orderNo") Long orderNo
    );
    //  대기중인 수주 중 제품명이 흑마늘즙인 최신 수주
    @Query(value = "SELECT * FROM orders o WHERE o.order_status = 'BEFORE_REGISTER' " +
            "AND o.order_product_type = 'GARLIC_JUICE' " +
            "AND o.order_no <> :orderNo " +
            "ORDER BY o.order_no DESC LIMIT 1", nativeQuery = true)
    Order findTopGarlicJuiceBeforeRegisterOrder(
            @Param("orderNo") Long orderNo
    );
    //  대기중인 수주 중 제품명이 석류 젤리스틱인 최신 수주
    @Query(value = "SELECT * FROM orders o WHERE o.order_status = 'BEFORE_REGISTER' " +
            "AND o.order_product_type = 'POMEGRANATE_JELLY' " +
            "AND o.order_no <> :orderNo " +
            "ORDER BY o.order_no DESC LIMIT 1", nativeQuery = true)
    Order findTopPomegranateJellyBeforeRegisterOrder(
            @Param("orderNo") Long orderNo
    );
    //  대기중인 수주 중 제품명이 매실 젤리스틱인 최신 수주
    @Query(value = "SELECT * FROM orders o WHERE o.order_status = 'BEFORE_REGISTER' " +
            "AND o.order_product_type = 'PLUM_JELLY' " +
            "AND o.order_no <> :orderNo " +
            "ORDER BY o.order_no DESC LIMIT 1", nativeQuery = true)
    Order findTopPlumJellyBeforeRegisterOrder(
            @Param("orderNo") Long orderNo
    );
}
