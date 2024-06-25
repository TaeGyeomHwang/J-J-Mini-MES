package com.boot.mes6.service;

import com.boot.mes6.entity.CurrentProduct;
import com.boot.mes6.entity.CurrentTime;
import com.boot.mes6.repository.CurrentTimeRepositoryHwang;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ReportServiceHwang {

    private final CurrentTimeRepositoryHwang currentTimeRepositoryHwang;

    public String calculateProgress(LocalDateTime orderDate, LocalDateTime orderExpectShipDate) {

        CurrentTime currentTimeEntity = currentTimeRepositoryHwang.findById(1L)
                .orElseThrow(EntityNotFoundException::new);
        LocalDateTime currentTime = currentTimeEntity.getCurrentTimeValue();

        Duration totalDuration = Duration.between(orderDate, orderExpectShipDate);

        Duration elapsedDuration = Duration.between(orderDate, currentTime);

        double progress = (double) elapsedDuration.toMillis() / totalDuration.toMillis() * 100;

        progress = Math.min(Math.max(progress, 0), 100);

        return String.format("%.2f%%", progress);
    }
}
