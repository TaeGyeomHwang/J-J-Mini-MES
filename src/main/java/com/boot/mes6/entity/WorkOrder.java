package com.boot.mes6.entity;

import com.boot.mes6.constant.FacilityName;
import com.boot.mes6.constant.ProcessCode;
import com.boot.mes6.constant.ProductType;
import com.boot.mes6.constant.WorkOrderStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "work_order")
@Getter
@Setter
public class WorkOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long workOrderNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_no")
    private Plan plan;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType workOrderType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProcessCode workOrderProcessName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FacilityName workOrderFacilityName;

    @Column(nullable = false)
    private Long workOrderInput;

    @Column(nullable = false)
    private Long workOrderOutput;

    @Column(nullable = false)
    private LocalDateTime workOrderStartDate;

    @Column(nullable = false)
    private LocalDateTime workOrderExpectDate;

    private LocalDateTime workOrderFinishDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkOrderStatus workOrderStatus;
}
