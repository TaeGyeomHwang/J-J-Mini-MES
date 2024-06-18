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
    private Long productNo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductName productName;

    @Column(nullable = false)
    private Long productInoutAmount;

    private LocalDateTime productInDate;

    private LocalDateTime productOutDate;
}
