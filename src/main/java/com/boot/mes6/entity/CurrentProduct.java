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
    @Enumerated(EnumType.STRING)
    private ProductName currentProductName;

    @Column(nullable = false)
    private Long currentProductAmount;

    @Column(nullable = false)
    private Long currentProductSafeAmount;
}
