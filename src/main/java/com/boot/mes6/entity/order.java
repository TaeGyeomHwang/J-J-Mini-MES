package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order")
@Getter
@Setter
public class order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long order_no;

    private boolean order_type;

    private String product_name;

    private
}
