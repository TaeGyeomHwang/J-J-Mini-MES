package com.boot.mes6.entity;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.constant.MaterialStatus;
import com.boot.mes6.constant.MaterialSupplierName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "material_in_out")
public class MaterialInOut {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long material_no;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialName material_name;

    @Column(nullable = false)
    private Long material_inout_amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialSupplierName material_supplier_name;

    private LocalDateTime material_order_date;

    private LocalDateTime material_receipt_date;

    private LocalDateTime material_in_date;

    private LocalDateTime material_out_date;

    private LocalDate material_expire_date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialStatus material_status;
}
