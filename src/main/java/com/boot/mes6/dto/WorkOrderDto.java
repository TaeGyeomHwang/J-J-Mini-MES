package com.boot.mes6.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class WorkOrderDto {
    private Long planNo;
    private String workOrderProductType;
    private String workOrderProcessName;
    private String workOrderFacilityName;
    private Long workOrderInput;
    private Long workOrderOutput;
    private LocalDateTime workOrderStartDate;
    private LocalDateTime workOrderExpectDate;
    private LocalDateTime workOrderFinishDate;
    private String workOrderStatus;
}
