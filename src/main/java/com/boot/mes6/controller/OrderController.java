package com.boot.mes6.controller;

import com.boot.mes6.dto.DataTableResponse;
import com.boot.mes6.dto.OrderDto;
import com.boot.mes6.dto.OrderFormDto;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.OrderPlanMap;
import com.boot.mes6.repository.OrderPlanMapRepositoryHwang;
import com.boot.mes6.repository.OrderRepositoryHwang;
import com.boot.mes6.service.MaterialService;
import com.boot.mes6.service.OrderServiceHwang;
import com.boot.mes6.service.PlanServiceHwang;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Book;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceHwang orderServiceHwang;
    private final PlanServiceHwang planServiceHwang;
    private final MaterialService materialService;

    private final OrderRepositoryHwang orderRepositoryHwang;
    private final OrderPlanMapRepositoryHwang orderPlanMapRepositoryHwang;

    //  수주 목록 조회 페이지
    @GetMapping(value = "/orders")
    public String orderMng(Model model) {
        return "order/orderMng";
    }
    //  수주 목록 테이블 정보 제공 API
    @GetMapping(value = "/api/orders")
    public ResponseEntity<DataTableResponse<OrderDto>> apiOrders(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length) {

        Page<Order> ordersPage = orderServiceHwang.getOrders(PageRequest.of(start / length, length));

        List<OrderDto> orderDtos = ordersPage.getContent().stream()
                .map(order -> new OrderDto(
                        order.getOrderNo(),
                        order.getOrderType().getDescription(),
                        order.getOrderProductType().getDescription(),
                        order.getOrderAmount(),
                        order.getOrderCustomerName(),
                        order.getOrderDate(),
                        order.getOrderExpectShipDate(),
                        order.getOrderOutDate(),
                        order.isOrderIsEmergency(),
                        order.getOrderStatus().getDescription()
                ))
                .collect(Collectors.toList());

        DataTableResponse<OrderDto> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(ordersPage.getTotalElements());
        response.setRecordsFiltered(ordersPage.getTotalElements());
        response.setData(orderDtos);

        return ResponseEntity.ok().body(response);
    }


    //  수주 추가 페이지
    @GetMapping(value = "/order/new")
    public String orderForm(Model model) {
        model.addAttribute("orderFormDto", new OrderFormDto());
        return "order/orderAdd";
    }

    //  수주 추가 후 생산계획 생성 후 작업지시 생성
    @PostMapping(value = "/order/new")
    public String newOrder(OrderFormDto orderFormDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "order/orderAdd";
        }
        try {
            //  수주 추가
            Long orderNo = orderServiceHwang.saveOrderManual(orderFormDto);
            if (orderNo == -1L){
                model.addAttribute("errorMessage", "안전 재고량이 부족합니다.");
                return "order/orderAdd";
            }

            //  생산계획 추가 또는 합병
            Order order = orderRepositoryHwang.findById(orderNo)
                    .orElseThrow(EntityNotFoundException::new);
            Long planNo = planServiceHwang.createOrMergePlan(order);

            //  수주 예상 납기일 설정
            List<OrderPlanMap> orderPlanMapList = orderPlanMapRepositoryHwang.findListByPlanNo(planNo);
            for (OrderPlanMap orderPlanMap : orderPlanMapList){
                Order findOrder = orderRepositoryHwang.findById(orderPlanMap.getOrder().getOrderNo())
                        .orElseThrow(EntityNotFoundException::new);
                Long updatedOrderNo = orderServiceHwang.updateOrderExpectDate(findOrder, planNo);
            }

            //  수주 추가 후 원자재 자동 발주
            materialService.autoAddMaterial(order);

        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", "수주 등록 중 에러가 발생하였습니다.");
            return "order/orderAdd";
        }

        return "redirect:/orders";
    }
}
