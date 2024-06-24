package com.boot.mes6.service;

import com.boot.mes6.entity.CurrentTime;
import com.boot.mes6.repository.CurrentTimeRepositoryHwang;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrentTimeServiceHwang {
    private final CurrentTimeRepositoryHwang currentTimeRepositoryHwang;

    public CurrentTime saveCurrentTime() {
        CurrentTime currentTime = new CurrentTime();
        currentTime.setCurrentTimeValue(LocalDateTime.now());
        return currentTimeRepositoryHwang.save(currentTime);
    }

    public void initializeCurrentTime() {
        if (currentTimeRepositoryHwang.count() == 0) {
            saveCurrentTime();
        }
    }

    public LocalDateTime getCurrentTime() {
        return currentTimeRepositoryHwang.findById(1L)
                .orElseThrow(EntityNotFoundException::new).getCurrentTimeValue();
    }
}
