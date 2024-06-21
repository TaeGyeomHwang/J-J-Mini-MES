package com.boot.mes6.controller;

import com.boot.mes6.constant.ProductType;
import com.boot.mes6.dto.DataTableResponse;
import com.boot.mes6.dto.WorkOrderDto;
import com.boot.mes6.entity.OrderPlanMap;
import com.boot.mes6.entity.WorkOrder;
import com.boot.mes6.repository.OrderPlanMapRepositoryHwang;
import com.boot.mes6.repository.OrderRepositoryHwang;
import com.boot.mes6.repository.WorkOrderRepositoryHwang;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderRepositoryHwang workOrderRepositoryHwang;
    private final OrderPlanMapRepositoryHwang orderPlanMapRepositoryHwang;
    private final OrderRepositoryHwang orderRepositoryHwang;

    // 즙 작업지시 목록 조회 페이지
    @GetMapping(value = "/juice/workorders")
    public String juiceMng(Model model) {
        return "workOrder/juiceWorkOrderMng";
    }

    // 즙 작업지시 테이블 정보 제공 API
    @GetMapping(value = "/api/juice/workorders")
    public ResponseEntity<DataTableResponse<WorkOrderDto>> apiJuiceWorkOrders(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required = false) String selectedDate) {

        Page<WorkOrder> workOrdersPage;

        if (selectedDate != null && !selectedDate.isEmpty()) {
            // 클라이언트에서 전달된 날짜를 파싱하여 필요한 형식으로 변환
            LocalDate date = LocalDate.parse(selectedDate);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            workOrdersPage = workOrderRepositoryHwang.findByWorkOrderTypeAndWorkOrderStartDateBetween(
                    ProductType.JUICE, startOfDay, endOfDay, PageRequest.of(start / length, length));
        } else {
            workOrdersPage = workOrderRepositoryHwang.findByWorkOrderType(
                    ProductType.JUICE, PageRequest.of(start / length, length));
        }

        List<WorkOrderDto> workOrderDtos = workOrdersPage.getContent().stream()
                .map(workOrder -> {
                    WorkOrderDto workOrderDto = new WorkOrderDto();
                    System.out.println("생산계획번호:"+workOrder.getPlan().getPlanNo());
                    workOrderDto.setPlanNo(workOrder.getPlan().getPlanNo());
                    System.out.println("생산계획번호:"+workOrderDto.getPlanNo());
                    workOrderDto.setWorkOrderProcessName(workOrder.getWorkOrderProcessName().getDescription());
                    workOrderDto.setWorkOrderFacilityName(workOrder.getWorkOrderFacilityName().getDescription());
                    workOrderDto.setWorkOrderInput(workOrder.getWorkOrderInput());
                    workOrderDto.setWorkOrderOutput(workOrder.getWorkOrderOutput());
                    workOrderDto.setWorkOrderStartDate(workOrder.getWorkOrderStartDate());
                    workOrderDto.setWorkOrderExpectDate(workOrder.getWorkOrderExpectDate());
                    workOrderDto.setWorkOrderFinishDate(workOrder.getWorkOrderFinishDate());
                    workOrderDto.setWorkOrderStatus(workOrder.getWorkOrderStatus().getDescription());

                    Long planNo = workOrder.getPlan().getPlanNo();
                    List<OrderPlanMap> orderPlanMaps = orderPlanMapRepositoryHwang.findListByPlanNo(planNo);
                    if (!orderPlanMaps.isEmpty()) {
                        Long orderNo = orderPlanMaps.get(0).getOrder().getOrderNo();
                        String productName = orderRepositoryHwang.findById(orderNo)
                                .orElseThrow(EntityNotFoundException::new).getOrderProductType().getDescription();
                        workOrderDto.setWorkOrderProductType(productName);
                    }
                    return workOrderDto;
                })
                .collect(Collectors.toList());

        DataTableResponse<WorkOrderDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(workOrdersPage.getTotalElements());
        response.setRecordsFiltered(workOrdersPage.getTotalElements());
        response.setData(workOrderDtos);

        return ResponseEntity.ok().body(response);
    }

    // 젤리 작업지시 목록 조회 페이지
    @GetMapping(value = "/jelly/workorders")
    public String jellyMng(Model model) {
        return "workOrder/jellyWorkOrderMng";
    }

    // 젤리 작업지시 테이블 정보 제공 API
    @GetMapping(value = "/api/jelly/workorders")
    public ResponseEntity<DataTableResponse<WorkOrderDto>> apiJellyWorkOrders(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required = false) String selectedDate) {

        Page<WorkOrder> workOrdersPage;

        if (selectedDate != null && !selectedDate.isEmpty()) {
            // 클라이언트에서 전달된 날짜를 파싱하여 필요한 형식으로 변환
            LocalDate date = LocalDate.parse(selectedDate);
            LocalDateTime startOfDay = date.atStartOfDay();
            LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

            workOrdersPage = workOrderRepositoryHwang.findByWorkOrderTypeAndWorkOrderStartDateBetween(
                    ProductType.JELLY, startOfDay, endOfDay, PageRequest.of(start / length, length));
        } else {
            workOrdersPage = workOrderRepositoryHwang.findByWorkOrderType(
                    ProductType.JELLY, PageRequest.of(start / length, length));
        }

        List<WorkOrderDto> workOrderDtos = workOrdersPage.getContent().stream()
                .map(workOrder -> {
                    WorkOrderDto workOrderDto = new WorkOrderDto();
                    System.out.println("생산계획번호:"+workOrder.getPlan().getPlanNo());
                    workOrderDto.setPlanNo(workOrder.getPlan().getPlanNo());
                    System.out.println("생산계획번호:"+workOrderDto.getPlanNo());
                    workOrderDto.setWorkOrderProcessName(workOrder.getWorkOrderProcessName().getDescription());
                    workOrderDto.setWorkOrderFacilityName(workOrder.getWorkOrderFacilityName().getDescription());
                    workOrderDto.setWorkOrderInput(workOrder.getWorkOrderInput());
                    workOrderDto.setWorkOrderOutput(workOrder.getWorkOrderOutput());
                    workOrderDto.setWorkOrderStartDate(workOrder.getWorkOrderStartDate());
                    workOrderDto.setWorkOrderExpectDate(workOrder.getWorkOrderExpectDate());
                    workOrderDto.setWorkOrderFinishDate(workOrder.getWorkOrderFinishDate());
                    workOrderDto.setWorkOrderStatus(workOrder.getWorkOrderStatus().getDescription());

                    Long planNo = workOrder.getPlan().getPlanNo();
                    List<OrderPlanMap> orderPlanMaps = orderPlanMapRepositoryHwang.findListByPlanNo(planNo);
                    if (!orderPlanMaps.isEmpty()) {
                        Long orderNo = orderPlanMaps.get(0).getOrder().getOrderNo();
                        String productName = orderRepositoryHwang.findById(orderNo)
                                .orElseThrow(EntityNotFoundException::new).getOrderProductType().getDescription();
                        workOrderDto.setWorkOrderProductType(productName);
                    }
                    return workOrderDto;
                })
                .collect(Collectors.toList());

        DataTableResponse<WorkOrderDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(workOrdersPage.getTotalElements());
        response.setRecordsFiltered(workOrdersPage.getTotalElements());
        response.setData(workOrderDtos);

        return ResponseEntity.ok().body(response);
    }

}
