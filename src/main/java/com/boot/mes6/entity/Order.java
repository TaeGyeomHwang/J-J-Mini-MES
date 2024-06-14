package com.boot.mes6.entity;

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
    private boolean order_type;

    @Column(nullable = false)
    private String product_name;

    @Column(nullable = false)
    private Long order_amount;

    @Column(nullable = false)
    private String customer_name;

    @Column(nullable = false)
    private LocalDateTime order_date;

    @Column(nullable = false)
    private LocalDateTime expect_ship_date;

    @Column(nullable = true)
    private LocalDateTime out_date;

    @Column(nullable = false)
    private boolean is_emergency;

    @Column(nullable = false)
    private boolean is_shipped;

    @Column(nullable = false)
    private boolean is_canceled;
}
