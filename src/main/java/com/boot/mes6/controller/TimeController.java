package com.boot.mes6.controller;

import com.boot.mes6.service.TimeService;
import com.boot.mes6.service.WorkOrderServiceHwang;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TimeController {

    private final TimeService timeService;
    private final WorkOrderServiceHwang workOrderServiceHwang;

    //시스템 시간 가져오기
    @GetMapping("/time")
    public ResponseEntity<LocalDateTime> systemTime() {
        return ResponseEntity.ok(timeService.getCurrentTime());
    }

    //시스템 시간 증가 시키기
    @PostMapping("/increaseTime")
    public ResponseEntity<Void> increaseTime(@RequestParam int minutes) {
        timeService.increaseTime(minutes);
        //발주한 원자재 배송 중인지, 도착했는지 원자재 상태 변경하는 메서드
        timeService.setMaterialStatus();
        workOrderServiceHwang.setWorkOrderStatusProcessing();
        workOrderServiceHwang.setWorkOrderStatusComplete();
        return ResponseEntity.ok().build();
    }

}
