package com.boot.mes6.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WorkOrderStatus {
    WAITING,PROCESSING, COMPLETE;
}
