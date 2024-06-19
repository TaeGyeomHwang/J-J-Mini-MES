package com.boot.mes6.service;

import com.boot.mes6.entity.CurrentTime;
import com.boot.mes6.repository.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TimeService {
    private final TimeRepository timeRepository;

    //시스템 시간 가져오기
    public LocalDateTime getCurrentTime() {
        CurrentTime currentTime = timeRepository.findByCurrentTimeNo(1L);
        return currentTime.getCurrentTimeValue();
    }

    //시스템 시간 증가 시키기
    @Transactional
    public void increaseTime(int minutes){
        CurrentTime currentTime = timeRepository.findByCurrentTimeNo(1L);
        LocalDateTime currentTimeValue = currentTime.getCurrentTimeValue().plusMinutes(minutes);
        currentTime.setCurrentTimeValue(currentTimeValue);
        timeRepository.save(currentTime);
    }
}
