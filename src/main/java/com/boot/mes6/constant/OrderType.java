package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderType {
    GENERAL("일반 소비자"),
    COMPANY("업체");

    private final String description;
}
