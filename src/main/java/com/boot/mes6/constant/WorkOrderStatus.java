package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorkOrderStatus {
    WAITING("대기"),
    PROCESSING("진행 중"),
    COMPLETE("완료");

    private final String description;
}
