package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "material_log")
public class Material_log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long material_no;

    @ManyToOne
    @JoinColumn(name = "order_no", nullable = false)
    private Order order;

    @Column(nullable = false)
    private String material_name;

    @Column(nullable = false)
    private boolean is_inout;

    @Column(nullable = false)
    private Long inout_amount;

    @Column(nullable = false)
    private String supplier_name;

    @Column(nullable = false)
    private LocalDateTime material_order_date;

    @Column(nullable = false)
    private LocalDateTime in_date;

    @Column(nullable = false)
    private LocalDateTime out_date;

    @Column(nullable = false)
    private LocalDateTime expire_date;

    @Column(nullable = false)
    private String material_location;

    @Column(nullable = false)
    private boolean is_canceled;

    @Column(nullable = false)
    private boolean is_delivered;

    @Column(nullable = false)
    private boolean is_incommed;
}
