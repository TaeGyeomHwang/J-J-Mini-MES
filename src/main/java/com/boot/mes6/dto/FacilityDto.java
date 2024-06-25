package com.boot.mes6.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class FacilityDto {
    private String facilityName;
    private String facilityStatus;
    private String facilityProgress;
}
