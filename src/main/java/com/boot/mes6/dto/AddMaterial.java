package com.boot.mes6.dto;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.constant.MaterialStatus;
import com.boot.mes6.constant.MaterialSupplierName;
import com.boot.mes6.entity.MaterialInOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor
@Getter
public class AddMaterial {
    private MaterialName materialName;
    private Long materialInoutAmount;
    private MaterialSupplierName materialSupplierName;
}
