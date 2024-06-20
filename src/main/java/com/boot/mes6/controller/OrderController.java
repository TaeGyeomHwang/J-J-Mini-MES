package com.boot.mes6.controller;

import com.boot.mes6.dto.OrderFormDto;
import com.boot.mes6.entity.Order;
import com.boot.mes6.service.OrderServiceHwang;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderServiceHwang orderServiceHwang;
    
    //  수주 목록 조회 페이지
    @GetMapping(value = "/orders")
    public String manageOrder(Model model) {

        return "order/orderMng";
    }

    @GetMapping(value = "/order/new")
    public String orderForm(Model model) {
        model.addAttribute("orderFormDto", new OrderFormDto());
        return "order/orderAdd";
    }

    @PostMapping(value = "/order/new")
    public String newOrder(OrderFormDto orderFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "order/orderMng";
        }
        try{
            Long orderNo = orderServiceHwang.saveOrderManual(orderFormDto);
        }catch(IllegalStateException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "order/orderMng";
        }

        return "order/orderMng";
    }
}
