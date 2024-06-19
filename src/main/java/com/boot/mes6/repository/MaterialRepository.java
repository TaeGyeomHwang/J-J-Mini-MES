package com.boot.mes6.repository;

import com.boot.mes6.entity.MaterialInOut;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaterialRepository extends JpaRepository<MaterialInOut, Long> {
}
