package com.boot.mes6.controller;

import com.boot.mes6.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class TimeController {

    private final TimeService timeService;

    //시스템 시간 가져오기
    @GetMapping("/time")
    public ResponseEntity<LocalDateTime> systemTime() {
        return ResponseEntity.ok(timeService.getCurrentTime());
    }

    //시스템 시간 증가 시키기
    @PostMapping("/increaseTime")
    public ResponseEntity<Void> increaseTime(@RequestParam int minutes) {
        timeService.increaseTime(minutes);
        return ResponseEntity.ok().build();
    }

}
