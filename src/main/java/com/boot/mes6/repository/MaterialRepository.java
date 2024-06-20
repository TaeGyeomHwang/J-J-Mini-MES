package com.boot.mes6.repository;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MaterialRepository extends JpaRepository<MaterialInOut, Long> {
    List<MaterialInOut> findByMaterialNameAndMaterialOrderDateBetweenOrderByMaterialNo(MaterialName name, LocalDateTime start, LocalDateTime end);
}
