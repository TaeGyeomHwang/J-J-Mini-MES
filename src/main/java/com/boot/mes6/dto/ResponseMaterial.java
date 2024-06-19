package com.boot.mes6.dto;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.constant.MaterialStatus;
import com.boot.mes6.constant.MaterialSupplierName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor
@Getter
@Setter
public class ResponseMaterial {
    private Long materialNo;
    private MaterialName materialName;
    private Long materialInoutAmount;
    private MaterialSupplierName materialSupplierName;
    private LocalDateTime materialOrderDate;
    private LocalDateTime materialReceiptDate;
    private LocalDateTime materialInDate;
    private LocalDateTime materialOutDate;
    private LocalDate materialExpireDate;
    private MaterialStatus materialStatus;
}
