package com.boot.mes6.repository;

import com.boot.mes6.entity.CurrentTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TimeRepository extends JpaRepository<CurrentTime, Long> {
    CurrentTime findByCurrentTimeNo(Long current_time_no);
}
