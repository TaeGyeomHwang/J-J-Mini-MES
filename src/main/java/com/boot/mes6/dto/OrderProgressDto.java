package com.boot.mes6.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class OrderProgressDto {
    private Long orderNo;
    private String orderProductType;
    private Long orderAmount;
    private String orderCustomerName;
    private String orderProgress;
}
