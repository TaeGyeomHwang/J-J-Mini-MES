package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProcessCode {
    A1("전처리"),
    A2("추출"),
    A3("여과"),
    A4("살균"),
    A5("충진"),
    A6("검사"),
    A7("포장"),
    B1("혼합"),
    B2("살균"),
    B3("충진"),
    B4("냉각"),
    B5("검사"),
    B6("포장");

    private final String description;
}
