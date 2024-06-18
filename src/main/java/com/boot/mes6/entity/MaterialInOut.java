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
    private Long materialNo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialName materialName;

    @Column(nullable = false)
    private Long materialInoutAmount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialSupplierName materialSupplierName;

    private LocalDateTime materialOrderDate;

    private LocalDateTime materialReceiptDate;

    private LocalDateTime materialInDate;

    private LocalDateTime materialOutDate;

    private LocalDate materialExpireDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialStatus materialStatus;
}
