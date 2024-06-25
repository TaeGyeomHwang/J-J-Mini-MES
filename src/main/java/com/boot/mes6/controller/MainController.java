package com.boot.mes6.controller;

import com.boot.mes6.entity.ProductInOut;
import com.boot.mes6.service.ProductInOutServiceHwang;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ProductInOutServiceHwang productInOutServiceHwang;

    //  메인 페이지(생산량 그래프)
    @GetMapping("/")
    public String getProductInOutPage() {
        return "report/productGraph";
    }

    //  생산량 그래프 정보 제공 API
    @GetMapping("/api/productGraph")
    @ResponseBody
    public List<ProductInOut> getProductInData(@RequestParam String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime start = LocalDateTime.parse(date + " 00:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse(date + " 23:59:59", formatter);
        List<ProductInOut> result = productInOutServiceHwang.getProductInOutByDateRange(start, end);
        System.out.println("Returning data: " + result);  // Log the result to verify
        return result;
    }
    @GetMapping("/api/monthlyProductGraph")
    @ResponseBody
    public List<ProductInOut> getMonthlyProductInData(@RequestParam String yearMonth) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonthObj = YearMonth.parse(yearMonth, formatter);

        LocalDateTime start = yearMonthObj.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonthObj.atEndOfMonth().atTime(LocalTime.MAX);

        List<ProductInOut> result = productInOutServiceHwang.getProductInOutByDateRange(start, end);
        System.out.println("Returning monthly data for " + yearMonth + ": " + result);  // Log the result to verify
        return result;
    }
}
