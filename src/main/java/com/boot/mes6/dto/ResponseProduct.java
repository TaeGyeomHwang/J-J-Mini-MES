package com.boot.mes6.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor
@Getter
@Setter
public class ResponseProduct {
    private Long productNo;
    private String productName;
    private Long productInoutAmount;
    private LocalDateTime productInDate;
    private LocalDateTime productOutDate;
}
