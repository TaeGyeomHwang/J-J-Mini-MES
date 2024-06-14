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
    private Long plan_id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_no")
    private Order order_no;

    private String product_name;

    private Long production_amount;

    private LocalDateTime start_date;

    private LocalDateTime expect_finish_date;

    private LocalDateTime finish_date;
}
