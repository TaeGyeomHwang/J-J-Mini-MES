package com.boot.mes6.controller;

import com.boot.mes6.constant.FacilityName;
import com.boot.mes6.constant.WorkOrderStatus;
import com.boot.mes6.dto.*;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.WorkOrder;
import com.boot.mes6.service.CurrentTimeServiceHwang;
import com.boot.mes6.service.OrderServiceHwang;
import com.boot.mes6.service.ReportServiceHwang;
import com.boot.mes6.service.WorkOrderServiceHwang;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final OrderServiceHwang orderServiceHwang;
    private final ReportServiceHwang reportServiceHwang;
    private final WorkOrderServiceHwang workOrderServiceHwang;
    private final CurrentTimeServiceHwang currentTimeServiceHwang;

    //  수주별 진행률 조회 페이지
    @GetMapping(value = "/report/order/progress")
    public String orderProgressMng(Model model) {
        return "report/orderReport";
    }

    //  수주별 진행률 테이블 정보 제공 API
    @GetMapping(value = "/api/report/order/progress")
    public ResponseEntity<DataTableResponse<OrderProgressDto>> apiOrderProgress(
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

    //  충진카운트 조회 페이지
    @GetMapping(value = "/report/wrapper/progress")
    public String wrapperMng(Model model) {
        return "report/wrapperReport";
    }

    //  충진카운트 테이블 정보 제공 API
    @GetMapping(value = "/api/report/wrapper/progress")
    public ResponseEntity<DataTableResponse<WrapperDto>> apiWrapper(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length) {

        List<WorkOrder> workOrders = workOrderServiceHwang.getWrapperWorkOrders();

        LocalDateTime currentTime = currentTimeServiceHwang.getCurrentTime();

        List<WrapperDto> wrapperDtoList = workOrders.stream()
                .map(workOrder -> {
                    LocalDateTime workOrderStartDate = workOrder.getWorkOrderStartDate();
                    Duration duration = Duration.between(workOrderStartDate, currentTime);
                    long totalMinutes = duration.toMinutes();
                    long totalOutput = workOrder.getWorkOrderOutput();
                    long productionRate;

                    if (workOrder.getWorkOrderFacilityName().equals(FacilityName.JUICE_WRAPPER_1) ||
                            workOrder.getWorkOrderFacilityName().equals(FacilityName.JUICE_WRAPPER_2)) {
                        productionRate = 1250;
                    } else {
                        productionRate = 2000;
                    }

                    long passedOutput;
                    long failedOutput;

                    if (currentTime.isBefore(workOrderStartDate)) {
                        passedOutput = 0;
                        failedOutput = 0;
                    } else {
                        long productionCount = (long) ((totalMinutes / 60.0) * productionRate);
                        passedOutput = (long) (productionCount * 0.97);
                        failedOutput = productionCount - passedOutput;
                    }

                    return new WrapperDto(
                            workOrder.getWorkOrderFacilityName().getDescription(),
                            workOrderStartDate,
                            totalOutput,
                            passedOutput,
                            failedOutput
                    );
                })
                .collect(Collectors.toList());

        DataTableResponse<WrapperDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(wrapperDtoList.size());
        response.setRecordsFiltered(wrapperDtoList.size());
        response.setData(wrapperDtoList);

        return ResponseEntity.ok().body(response);
    }

    //  갱신시각 제공 API
    @GetMapping("/api/currentTime")
    public ResponseEntity<Map<String, String>> getCurrentTime() {
        LocalDateTime currentTime = currentTimeServiceHwang.getCurrentTime();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        String formattedCurrentTime = currentTime.format(formatter);

        Map<String, String> response = new HashMap<>();
        response.put("currentTime", formattedCurrentTime);

        return ResponseEntity.ok(response);
    }

    //  설비 가동 상황 조회 페이지
    @GetMapping(value = "/report/facility/progress")
    public String facilityMng(Model model) {
        return "report/facilityProgress";
    }

    //  설비 가동 상황 테이블 정보 제공 API
    @GetMapping(value = "/api/report/facility/progress")
    public ResponseEntity<DataTableResponse<FacilityDto>> apiFacility(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length) {

        List<WorkOrder> workOrders = workOrderServiceHwang.getFacilityWorkOrders();

        LocalDateTime currentTime = currentTimeServiceHwang.getCurrentTime();

        // map을 이용하여 시설별 진행상황 및 현황 집계
        Map<String, FacilityDto> facilityDtoMap = new HashMap<>();

        workOrders.forEach(workOrder -> {
            String facilityName = workOrder.getWorkOrderFacilityName().getDescription();

            // 작업지시 상태에 따라 설비현황 파악
            String facilityStatus;
            if (workOrder.getWorkOrderStatus() == WorkOrderStatus.WAITING) {
                facilityStatus = "비가동";
            } else if (workOrder.getWorkOrderStatus() == WorkOrderStatus.PROCESSING) {
                facilityStatus = "가동중";
            } else {
                facilityStatus = "비가동"; // Default status for other cases
            }

            // 설비 진척도를 경과한 시간의 백분율로 계산
            LocalDateTime workOrderStartDate = workOrder.getWorkOrderStartDate();
            LocalDateTime workOrderExpectDate = workOrder.getWorkOrderExpectDate();
            long totalDuration = Duration.between(workOrderStartDate, workOrderExpectDate).toMinutes();
            long elapsedDuration = Duration.between(workOrderStartDate, currentTime).toMinutes();

            double progress;
            if (totalDuration <= 0 || elapsedDuration < 0 || elapsedDuration > totalDuration) {
                progress = 0.0; // 기간 계산이 유효하지 않은 경우 0%로 처리
            } else if (workOrder.getWorkOrderStatus() == WorkOrderStatus.COMPLETE) {
                progress = 0.0; // 작업지시 완료시 진행률 0%로 처리
            } else {
                progress = (double) elapsedDuration / totalDuration * 100;
            }

            // map에 FacilityDto 업데이트 또는 추가
            if (!facilityDtoMap.containsKey(facilityName)) {
                facilityDtoMap.put(facilityName, new FacilityDto(facilityName, facilityStatus, String.format("%.2f%%", progress)));
            } else {
                // 필요한 경우 상태 및 진행 상황 업데이트(예: 진행 중인 작업 주문의 경우)
                FacilityDto existingDto = facilityDtoMap.get(facilityName);
                if (facilityStatus.equals("가동중")) {
                    existingDto.setFacilityStatus("가동중");
                }
                if (progress > 0.0) {
                    existingDto.setFacilityProgress(String.format("%.2f%%", progress));
                }
            }
        });

        // map을 list로 변환
        List<FacilityDto> facilityDtoList = new ArrayList<>(facilityDtoMap.values());

        // 설비 이름별로 설비 DtoList 정렬
        facilityDtoList.sort(Comparator.comparing(FacilityDto::getFacilityName));

        // DataTableResponse 설정
        DataTableResponse<FacilityDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(facilityDtoList.size());
        response.setRecordsFiltered(facilityDtoList.size());
        response.setData(facilityDtoList);

        return ResponseEntity.ok().body(response);
    }

    //  capa 현황 테이블 정보 제공 API
    @GetMapping(value = "/api/report/capa/progress")
    public ResponseEntity<DataTableResponse<CapaDto>> apiCapa(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length) {

        List<WorkOrder> workOrders = workOrderServiceHwang.getCapaWorkOrders();

        // 시설명 설정
        List<FacilityName> targetFacilities = Arrays.asList(
                FacilityName.EXTRACTOR_1,
                FacilityName.EXTRACTOR_2,
                FacilityName.STERILIZER_1,
                FacilityName.STERILIZER_2,
                FacilityName.MIXER,
                FacilityName.FILTER);

        // map을 이용하여 시설별 capa 집계
        Map<String, CapaDto> capaDtoMap = new HashMap<>();

        workOrders.stream()
                .filter(workOrder -> targetFacilities.contains(workOrder.getWorkOrderFacilityName()))
                .forEach(workOrder -> {
                    String capaName = workOrder.getWorkOrderFacilityName().getDescription();

                    // capa 진행률을 최대 용량 대비 workOrderInput의 백분율로 계산
                    double progress;
                    if (workOrder.getWorkOrderStatus() == WorkOrderStatus.WAITING) {
                        progress = 0.0;
                    } else if (workOrder.getWorkOrderStatus() == WorkOrderStatus.PROCESSING) {
                        long workOrderInput = workOrder.getWorkOrderInput();
                        double maxCapacity;
                        switch (workOrder.getWorkOrderFacilityName()) {
                            case EXTRACTOR_1:
                            case EXTRACTOR_2:
                                maxCapacity = 1000.0;
                                break;
                            case STERILIZER_1:
                            case STERILIZER_2:
                                maxCapacity = 100.0;
                                break;
                            case MIXER:
                                maxCapacity = 60000.0;
                                break;
                            case FILTER:
                                maxCapacity = 200.0;
                                break;
                            default:
                                maxCapacity = 0.0; // 기본값 설정
                        }
                        progress = maxCapacity == 0 ? 0.0 : (double) workOrderInput / maxCapacity * 100;
                    } else {
                        progress = 0.0; // 기본값 설정
                    }

                    // map에서 CapaDto 업데이트 또는 추가
                    if (!capaDtoMap.containsKey(capaName)) {
                        capaDtoMap.put(capaName, new CapaDto(capaName, String.format("%.2f%%", progress)));
                    } else {
                        // 필요한 경우 진행 상황 업데이트(예: 진행 중인 작업 주문의 경우)
                        CapaDto existingDto = capaDtoMap.get(capaName);
                        if (progress > 0.0) {
                            existingDto.setCapaProgress(String.format("%.2f%%", progress));
                        }
                    }
                });

        // map을 list로 변환
        List<CapaDto> capaDtoList = new ArrayList<>(capaDtoMap.values());

        // DataTableResponse 설정
        DataTableResponse<CapaDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(capaDtoList.size());
        response.setRecordsFiltered(capaDtoList.size());
        response.setData(capaDtoList);

        return ResponseEntity.ok().body(response);
    }


}
