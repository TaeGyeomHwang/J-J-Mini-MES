package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProductName {
    CABBAGE_JUICE("양배추즙"),
    GARLIC_JUICE("흑마늘즙"),
    POMEGRANATE_JELLY("석류 젤리스틱"),
    PLUM_JELLY("매실 젤리스틱");

    private final String description;
}
