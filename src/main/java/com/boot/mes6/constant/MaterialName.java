package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MaterialName {
    CABBAGE("양배추"),
    GARLIC("흑마늘"),
    HONEY("벌꿀"),
    POMEGRANATE("석류(농축액)"),
    PLUM("매실(농축액)"),
    COLLAGEN("콜라겐"),
    WRAPPER("포장지"),
    BOX("Box");

    private final String description;
}
