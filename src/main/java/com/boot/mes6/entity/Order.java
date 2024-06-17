package com.boot.mes6.entity;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.constant.OrderStatus;
import com.boot.mes6.constant.OrderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long order_no;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderType order_type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductName order_product_type;

    @Column(nullable = false)
    private Long order_amount;

    @Column(nullable = false)
    private String order_customer_name;

    @Column(nullable = false)
    private LocalDateTime order_date;

    @Column(nullable = false)
    private LocalDateTime order_expect_ship_date;

    @Column(nullable = true)
    private LocalDateTime order_out_date;

    @Column(nullable = false)
    private boolean order_is_emergency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus order_status;
}
