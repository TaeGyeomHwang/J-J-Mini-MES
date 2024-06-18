package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MaterialStatus {
    PREPARING_SHIP("배송 준비중"),
    SHIPPING("배송 중"),
    IN("입고 완료"),
    OUT("출고 완료"),
    CANCELED("취소 완료"),
    DISUSE("폐기");

    private final String description;
}
