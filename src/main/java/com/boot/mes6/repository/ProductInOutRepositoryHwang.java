package com.boot.mes6.repository;

import com.boot.mes6.entity.ProductInOut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductInOutRepositoryHwang extends JpaRepository<ProductInOut, Long> {
    List<ProductInOut> findByProductInDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    //완제품 입출고 이력 테이블 띄울 때 페이징으로 10개 단위로 DB에서 가져오기, product_no 기준 내림차순으로
    @Query(value = "select * from product_in_out order by product_no desc",
            countQuery = "select count(*) from product_in_out", nativeQuery = true)
    Page<ProductInOut> findAllOrderByProductNoDesc(Pageable pageable);

}

