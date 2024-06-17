package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MaterialStatus {
    PREPARING_SHIP,SHIPPING,IN,OUT,CANCELED,DISUSE;
}
