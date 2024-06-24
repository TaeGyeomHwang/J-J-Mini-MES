package com.boot.mes6.controller;

import com.boot.mes6.dto.DataTableResponse;
import com.boot.mes6.dto.OrderDto;
import com.boot.mes6.dto.OrderProgressDto;
import com.boot.mes6.entity.Order;
import com.boot.mes6.service.OrderServiceHwang;
import com.boot.mes6.service.ReportServiceHwang;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final OrderServiceHwang orderServiceHwang;
    private final ReportServiceHwang reportServiceHwang;

    //  수주별 진행률 조회 페이지
    @GetMapping(value = "/report/order/progress")
    public String orderMng(Model model) {
        return "report/orderReport";
    }

    //  수주별 진행률 테이블 정보 제공 API
    @GetMapping(value = "/api/report/order/progress")
    public ResponseEntity<DataTableResponse<OrderProgressDto>> apiOrders(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length) {

        Page<Order> ordersPage = orderServiceHwang.getOrders(PageRequest.of(start / length, length));

        List<OrderProgressDto> orderProgressDtoList = ordersPage.getContent().stream()
                .map(order -> new OrderProgressDto(
                        order.getOrderNo(),
                        order.getOrderProductType().getDescription(),
                        order.getOrderAmount(),
                        order.getOrderCustomerName(),
                        reportServiceHwang.calculateProgress(order.getOrderDate(),order.getOrderExpectShipDate())
                ))
                .collect(Collectors.toList());

        DataTableResponse<OrderProgressDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(ordersPage.getTotalElements());
        response.setRecordsFiltered(ordersPage.getTotalElements());
        response.setData(orderProgressDtoList);

        return ResponseEntity.ok().body(response);
    }
}
