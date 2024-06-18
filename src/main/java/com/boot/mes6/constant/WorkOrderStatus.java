package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorkOrderStatus {
    WAITING("완료"),
    PROCESSING("진행 중"),
    COMPLETE("대기");

    private final String description;
}
