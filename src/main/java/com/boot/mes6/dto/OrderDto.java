package com.boot.mes6.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class OrderDto {
    private Long orderNo;
    private String orderType;
    private String orderProductType;
    private Long orderAmount;
    private String orderCustomerName;
    private LocalDateTime orderDate;
    private LocalDateTime orderExpectShipDate;
    private LocalDateTime orderOutDate;
    private boolean orderIsEmergency;
    private String orderStatus;
}
