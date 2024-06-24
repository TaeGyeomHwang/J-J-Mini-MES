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

        // Use a map to aggregate progress and status for each facility
        Map<String, FacilityDto> facilityDtoMap = new HashMap<>();

        workOrders.forEach(workOrder -> {
            String facilityName = workOrder.getWorkOrderFacilityName().getDescription();

            // Determine facility status based on work order status
            String facilityStatus;
            if (workOrder.getWorkOrderStatus() == WorkOrderStatus.WAITING) {
                facilityStatus = "비가동";
            } else if (workOrder.getWorkOrderStatus() == WorkOrderStatus.PROCESSING) {
                facilityStatus = "가동중";
            } else {
                facilityStatus = "비가동"; // Default status for other cases
            }

            // Calculate facility progress as percentage of time elapsed
            LocalDateTime workOrderStartDate = workOrder.getWorkOrderStartDate();
            LocalDateTime workOrderExpectDate = workOrder.getWorkOrderExpectDate();
            long totalDuration = Duration.between(workOrderStartDate, workOrderExpectDate).toMinutes();
            long elapsedDuration = Duration.between(workOrderStartDate, currentTime).toMinutes();

            double progress;
            if (totalDuration <= 0 || elapsedDuration < 0 || elapsedDuration > totalDuration) {
                progress = 0.0; // Handle edge cases where duration calculation is invalid
            } else if (workOrder.getWorkOrderStatus() == WorkOrderStatus.COMPLETE) {
                progress = 0.0; // Work order is complete, progress is 0%
            } else {
                progress = (double) elapsedDuration / totalDuration * 100;
            }

            // Update or add FacilityDto in the map
            if (!facilityDtoMap.containsKey(facilityName)) {
                facilityDtoMap.put(facilityName, new FacilityDto(facilityName, facilityStatus, String.format("%.2f%%", progress)));
            } else {
                // Update status and progress if necessary (e.g., for ongoing work orders)
                FacilityDto existingDto = facilityDtoMap.get(facilityName);
                if (facilityStatus.equals("가동중")) {
                    existingDto.setStatus("가동중");
                }
                if (progress > 0.0) {
                    existingDto.setProgress(String.format("%.2f%%", progress));
                }
            }
        });

        // Convert map values to list
        List<FacilityDto> facilityDtoList = new ArrayList<>(facilityDtoMap.values());

        // Sort facilityDtoList by facility name (optional)
        facilityDtoList.sort(Comparator.comparing(FacilityDto::getFacilityName));

        // Prepare DataTableResponse
        DataTableResponse<FacilityDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(facilityDtoList.size());
        response.setRecordsFiltered(facilityDtoList.size());
        response.setData(facilityDtoList);

        return ResponseEntity.ok().body(response);
    }

    //  설비 가동 상황 테이블 정보 제공 API
    @GetMapping(value = "/api/report/capa/progress")
    public ResponseEntity<DataTableResponse<CapaDto>> apiCapa(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length) {

        List<WorkOrder> workOrders = workOrderServiceHwang.getCapaWorkOrders();

        // Filter work orders to include only the specified facility names
        List<FacilityName> targetFacilities = Arrays.asList(
                FacilityName.EXTRACTOR_1,
                FacilityName.EXTRACTOR_2,
                FacilityName.STERILIZER_1,
                FacilityName.STERILIZER_2,
                FacilityName.MIXER,
                FacilityName.FILTER);

        List<CapaDto> capaDtoList = workOrders.stream()
                .filter(workOrder -> targetFacilities.contains(workOrder.getWorkOrderFacilityName()))
                .sorted(Comparator.comparingLong(WorkOrder::getWorkOrderNo))
                .map(workOrder -> {
                    String capaName = workOrder.getWorkOrderFacilityName().getDescription();

                    // Calculate capa progress as percentage of workOrderInput compared to max capacity
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
                                maxCapacity = 0.0; // Handle other cases if necessary
                        }
                        progress = maxCapacity == 0 ? 0.0 : (double) workOrderInput / maxCapacity * 100;
                    } else {
                        progress = 0.0; // Default progress for other cases
                    }

                    return new CapaDto(capaName, String.format("%.2f%%", progress));
                })
                .distinct() // Ensure distinct CapaDto objects based on facility name
                .collect(Collectors.toList());

        DataTableResponse<CapaDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(capaDtoList.size());
        response.setRecordsFiltered(capaDtoList.size());
        response.setData(capaDtoList);

        return ResponseEntity.ok().body(response);
    }

}
