package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "plan")
@Getter
@Setter
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long work_order_no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_no")
    private Plan plan;

    @Column(nullable = false)
    private boolean work_order_type;

    @Column(nullable = false)
    private String product_name;

    @Column(nullable = false)
    private String process_name;

    @Column(nullable = false)
    private String facility_name;

    @Column(nullable = false)
    private Long work_amount;

    @Column(nullable = false)
    private LocalDateTime start_date;

    @Column(nullable = false)
    private LocalDateTime expect_finish_date;

    @Column(nullable = false)
    private LocalDateTime finish_date;

    @Column(nullable = false)
    private boolean is_finish;

    @Column(nullable = false)
    private boolean is_processing;
}
