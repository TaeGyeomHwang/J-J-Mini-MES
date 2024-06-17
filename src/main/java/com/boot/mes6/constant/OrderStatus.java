package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    BEFORE_REGISTER, AFTER_REGISTER, IN_PRODUCTION, SHIPPED, CANCELED;
}
