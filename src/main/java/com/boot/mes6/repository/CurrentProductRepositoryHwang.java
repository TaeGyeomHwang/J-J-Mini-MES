package com.boot.mes6.repository;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.CurrentProduct;
import com.boot.mes6.entity.ProductInOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CurrentProductRepositoryHwang extends JpaRepository<CurrentProduct, Long> {

    // ProductName으로 찾기
    CurrentProduct findByCurrentProductName(ProductName currentProductName);

    //완제품 기준으로 일반 재고량, 안전 재고량 가져오기
    @Query(value = "select * from current_product where current_product_name = :productName", nativeQuery = true)
    Optional<CurrentProduct> getCurrentProductAmount(ProductName productName);
}
