package com.boot.mes6.repository;

import com.boot.mes6.entity.ProductInOut;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductInOutRepositoryHwang extends JpaRepository<ProductInOut, Long> {
    List<ProductInOut> findByProductInDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}

