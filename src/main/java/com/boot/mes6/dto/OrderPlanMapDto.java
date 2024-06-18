package com.boot.mes6.dto;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.constant.ProductType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPlanMapDto {

    private Long orderNo;

    private ProductName productName;

    private Long orderAmount;
}
