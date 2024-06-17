package com.boot.mes6.entity;

import com.boot.mes6.constant.ProductName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "current_product")
@Getter
@Setter
public class CurrentProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private ProductName current_product_name;

    @Column(nullable = false)
    private Long current_product_amount;

    @Column(nullable = false)
    private Long current_product_safe_amount;
}
