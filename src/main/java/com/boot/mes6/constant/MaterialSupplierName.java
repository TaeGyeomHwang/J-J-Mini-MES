package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MaterialSupplierName {
    A_FARM("에이농장"),
    OO_NH("OO농협"),
    OO_WRAPPING_COMPANY("OO포장");

    private final String description;
}
