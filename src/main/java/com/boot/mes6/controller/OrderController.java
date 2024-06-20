package com.boot.mes6.controller;

import com.boot.mes6.entity.Order;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class OrderController {
    
    //  수주 목록 조회 페이지
    @GetMapping(value = "/orders")
    public String manageOrder(Model model) {

        return "order/orderMng";
    }

    @GetMapping(value = "/order/new")
    public String addOrder(Model model) {

        return "order/orderMng";
    }
}
