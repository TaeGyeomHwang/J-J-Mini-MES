package com.boot.mes6.controller;

import com.boot.mes6.dto.DataTableResponse;
import com.boot.mes6.dto.OrderDto;
import com.boot.mes6.dto.PlanDto;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.OrderPlanMap;
import com.boot.mes6.entity.Plan;
import com.boot.mes6.repository.OrderPlanMapRepositoryHwang;
import com.boot.mes6.repository.OrderRepositoryHwang;
import com.boot.mes6.repository.PlanRepositoryHwang;
import com.boot.mes6.service.OrderServiceHwang;
import com.boot.mes6.service.PlanServiceHwang;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class PlanController {

    private final PlanRepositoryHwang planRepositoryHwang;
    private final OrderPlanMapRepositoryHwang orderPlanMapRepositoryHwang;

    //  생산계획 목록 조회 페이지
    @GetMapping(value = "/plans")
    public String orderMng(Model model) {
        return "plan/planMng";
    }

    //  생산계획 테이블 정보 제공 API
    @GetMapping(value = "/api/plans")
    public ResponseEntity<DataTableResponse<PlanDto>> apiPlans(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String month) {

        PageRequest pageRequest = PageRequest.of(start / length, length);
        Page<Plan> plansPage;

        if (year != null && !year.isEmpty() && month != null && !month.isEmpty()) {
            LocalDateTime startDate = LocalDateTime.of(Integer.parseInt(year), Integer.parseInt(month), 1, 0, 0);
            LocalDateTime endDate = startDate.plusMonths(1);
            plansPage = planRepositoryHwang.findAllByPlanStartDateBetween(startDate, endDate, pageRequest);
        } else {
            plansPage = planRepositoryHwang.findAll(pageRequest);
        }

        List<PlanDto> planDtos = plansPage.getContent().stream()
                .map(plan -> {
                    PlanDto planDto = new PlanDto();
                    planDto.setPlanNo(plan.getPlanNo());
                    planDto.setPlanAmount(plan.getPlanProductionAmount());
                    planDto.setPlanStartDate(plan.getPlanStartDate());
                    planDto.setPlanExpectFinishDate(plan.getPlanExpectFinishDate());
                    planDto.setPlanFinishDate(plan.getPlanFinishDate());

                    List<OrderPlanMap> orderPlanMaps = orderPlanMapRepositoryHwang.findListByPlanNo(plan.getPlanNo());
                    List<String> productTypes = orderPlanMaps.stream()
                            .map(orderPlanMap -> orderPlanMap.getOrder().getOrderProductType().getDescription())
                            .collect(Collectors.toList());

                    planDto.setPlanProductType(productTypes.get(0));

                    return planDto;
                })
                .collect(Collectors.toList());

        DataTableResponse<PlanDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(plansPage.getTotalElements());
        response.setRecordsFiltered(plansPage.getTotalElements());
        response.setData(planDtos);

        return ResponseEntity.ok().body(response);
    }

}
