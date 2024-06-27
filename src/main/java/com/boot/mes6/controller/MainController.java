package com.boot.mes6.controller;

import com.boot.mes6.entity.ProductInOut;
import com.boot.mes6.service.CurrentTimeServiceHwang;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ProductInOutServiceHwang productInOutServiceHwang;
    private final CurrentTimeServiceHwang currentTimeServiceHwang;

    //  메인 페이지(생산량 그래프)
    @GetMapping("/")
    public String getProductInOutPage() {
        return "report/productGraph";
    }

    //  일일 생산량 그래프 정보 제공 API
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

    //  월간 생산량 그래프 정보 제공 API
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

    //  그래프 기본값을 위한 현재 시간 정보 제공 API
    @GetMapping("/api/currentTime/graph")
    @ResponseBody
    public Map<String, String> getCurrentTime() {
        LocalDateTime currentTime = currentTimeServiceHwang.getCurrentTime();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");

        Map<String, String> currentTimeMap = new HashMap<>();
        currentTimeMap.put("currentDate", currentTime.format(dateFormatter));
        currentTimeMap.put("currentYearMonth", currentTime.format(monthFormatter));

        return currentTimeMap;
    }
}
