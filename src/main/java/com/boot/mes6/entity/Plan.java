package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "plan")
@Getter
@Setter
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long plan_no;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no")
    private Order order;

    @Column(nullable = false)
    private String product_name;

    @Column(nullable = false)
    private Long production_amount;

    @Column(nullable = false)
    private LocalDateTime start_date;

    @Column(nullable = false)
    private LocalDateTime expect_finish_date;

    @Column(nullable = false)
    private LocalDateTime finish_date;
}
