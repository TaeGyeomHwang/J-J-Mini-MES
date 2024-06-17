package com.boot.mes6.entity;

import com.boot.mes6.constant.ProductName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "product_in_out")
public class ProductInOut {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long product_no;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductName product_name;

    @Column(nullable = false)
    private Long product_inout_amount;

    private LocalDateTime product_in_date;

    private LocalDateTime product_out_date;
}
