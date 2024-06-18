package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductType {
    JUICE("즙"),
    JELLY("젤리스틱");

    private final String description;
}
