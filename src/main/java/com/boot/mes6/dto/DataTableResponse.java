package com.boot.mes6.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataTableResponse<T> {
    private int draw;
    private long recordsTotal;
    private long recordsFiltered;
    private List<T> data;
}