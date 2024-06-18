package com.boot.mes6.entity;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.constant.OrderStatus;
import com.boot.mes6.constant.OrderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderNo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType orderType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductName orderProductType;

    @Column(nullable = false)
    private Long orderAmount;

    @Column(nullable = false)
    private String orderCustomerName;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    private LocalDateTime orderExpectShipDate;

    private LocalDateTime orderOutDate;

    @Column(nullable = false)
    private boolean orderIsEmergency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
}
