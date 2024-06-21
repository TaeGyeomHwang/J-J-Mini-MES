package com.boot.mes6.controller;

import com.boot.mes6.entity.Order;
import com.boot.mes6.repository.OrderRepositoryHwang;
import com.boot.mes6.service.OrderServiceHwang;
import com.boot.mes6.service.PlanServiceHwang;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PlanController {
    private final OrderServiceHwang orderServiceHwang;
    private final PlanServiceHwang planServiceHwang;

    private final OrderRepositoryHwang orderRepositoryHwang;

    //  수주 추가 > 생산계획 생성 > 작업지시 생성 시퀀스
    @GetMapping(value = "/new/practice")
    public String addOrder(Model model) {
        Long orderNo = orderServiceHwang.saveOrder();
        Order order = orderRepositoryHwang.findById(orderNo)
                .orElseThrow(EntityNotFoundException::new);
        planServiceHwang.createOrMergePlan(order);

        return "example1";
    }
}
