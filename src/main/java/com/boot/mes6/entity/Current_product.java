package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "current_product")
@Getter
@Setter
public class Current_product {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long current_product_no;

    @Column(nullable = false)
    private String current_product_name;

    @Column(nullable = false)
    private Long current_inventory;

    @Column(nullable = false)
    private Long current_safe_inventory;
}
