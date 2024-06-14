package com.boot.mes6.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "current_material")
@Getter
@Setter
public class Current_material {

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long current_material_no;

    @Column(nullable = false)
    private String material_name;

    @Column(nullable = false)
    private Long material_amount;

    @Column(nullable = false)
    private LocalDateTime expire_date;

    @Column(nullable = false)
    private String location;
}
