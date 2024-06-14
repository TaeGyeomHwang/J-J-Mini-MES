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

    private String customer_name;

    private LocalDateTime order_date;

    private LocalDateTime expect_ship_date;

    private LocalDateTime out_date;

    private boolean is_emergency;

    private boolean is_shipped;

    private boolean is_canceled;
}
