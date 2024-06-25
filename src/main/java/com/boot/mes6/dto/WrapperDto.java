package com.boot.mes6.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class WrapperDto {
    private String wrapperName;
    private LocalDateTime wrapperStartDate;
    private Long wrapperTotalCount;
    private Long wrapperPassed;
    private Long wrapperFailure;
}
