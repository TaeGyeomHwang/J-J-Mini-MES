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

    @GetMapping("/time")
    public ResponseEntity<LocalDateTime> systemTime() {
        return ResponseEntity.ok(timeService.getCurrentTime());
    }

    @PostMapping("/increaseTime")
    public ResponseEntity<Void> increaseTime(@RequestParam int minutes) {
        timeService.increaseTime(minutes);
        workOrderServiceHwang.setWorkOrderStatusProcessing();
        workOrderServiceHwang.setWorkOrderStatusComplete();
        return ResponseEntity.ok().build();
    }

}
