package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    BEFORE_REGISTER("원자재 발주 전"),
    AFTER_REGISTER("원자재 발주 접수 완료"),
    IN_PRODUCTION("생산 중"),
    SHIPPED("배송 중"),
    CANCELED("취소 완료");

    private final String description;
}
