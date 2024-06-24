package com.boot.mes6.repository;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.CurrentProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentProductRepositoryHwang extends JpaRepository<CurrentProduct, Long> {

    // ProductName으로 찾기
    CurrentProduct findByCurrentProductName(ProductName currentProductName);
}
