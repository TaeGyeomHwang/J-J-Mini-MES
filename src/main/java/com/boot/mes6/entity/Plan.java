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

    private Long plan_production_amount;

    private LocalDateTime plan_start_date;

    private LocalDateTime plan_expect_finish_date;

    private LocalDateTime plan_finish_date;
}
