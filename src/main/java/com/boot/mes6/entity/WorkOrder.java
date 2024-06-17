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
    private Long work_order_no;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_no")
    private Plan plan;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductType work_order_type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ProcessCode work_order_process_name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private FacilityName work_order_facility_name;

    @Column(nullable = false)
    private Long work_order_input;

    @Column(nullable = false)
    private Long work_order_output;

    @Column(nullable = false)
    private LocalDateTime work_order_start_date;

    @Column(nullable = false)
    private LocalDateTime work_order_expect_date;

    private LocalDateTime work_order_finish_date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkOrderStatus work_order_status;
}
