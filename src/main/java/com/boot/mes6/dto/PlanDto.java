package com.boot.mes6.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PlanDto {
    private Long planNo;
    private String planProductType;
    private Long planAmount;
    private LocalDateTime planStartDate;
    private LocalDateTime planExpectFinishDate;
    private LocalDateTime planFinishDate;
}
