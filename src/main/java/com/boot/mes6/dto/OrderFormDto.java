package com.boot.mes6.dto;

import com.boot.mes6.constant.OrderType;
import com.boot.mes6.constant.ProductName;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderFormDto {

    private Long id;
    private OrderType orderType;
    private ProductName productName;
    private Long orderAmount;
    private String orderCustomerName;
    private boolean orderIsEmergency;
}
