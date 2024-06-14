package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "end_product_log")
public class End_product_log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long end_product_no;

    @ManyToOne
    @JoinColumn(name = "order_no", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String end_product_name;

    @Column(nullable = false)
    private boolean is_inout;

    @Column(nullable = false)
    private Long inout_amount;

    @Column(nullable = false)
    private LocalDateTime in_date;

    @Column(nullable = false)
    private LocalDateTime out_date;
}
