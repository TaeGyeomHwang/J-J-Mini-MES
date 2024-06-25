package com.boot.mes6.service;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.ProductInOut;
import com.boot.mes6.repository.ProductInOutRepositoryHwang;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductInOutServiceHwang {
    private final ProductInOutRepositoryHwang productInOutRepositoryHwang;
    
    //  더미 완제품 입출고 이력 데이터 생성
    public void createDummyProductLog(){
        ProductInOut productInOut1 = new ProductInOut();
        productInOut1.setProductName(ProductName.CABBAGE_JUICE);
        productInOut1.setProductInoutAmount(123L);
        productInOut1.setProductInDate(LocalDateTime.of(2024,6,25,12,0, 0));

        ProductInOut productInOut2 = new ProductInOut();
        productInOut2.setProductName(ProductName.GARLIC_JUICE);
        productInOut2.setProductInoutAmount(246L);
        productInOut2.setProductInDate(LocalDateTime.of(2024,7,25,12,0, 0));

        ProductInOut productInOut3 = new ProductInOut();
        productInOut3.setProductName(ProductName.POMEGRANATE_JELLY);
        productInOut3.setProductInoutAmount(90L);
        productInOut3.setProductInDate(LocalDateTime.of(2024,6,25,12,0, 0));

        ProductInOut productInOut4 = new ProductInOut();
        productInOut4.setProductName(ProductName.PLUM_JELLY);
        productInOut4.setProductInoutAmount(100L);
        productInOut4.setProductInDate(LocalDateTime.of(2024,7,25,12,0, 0));

        productInOutRepositoryHwang.save(productInOut1);
        productInOutRepositoryHwang.save(productInOut2);
        productInOutRepositoryHwang.save(productInOut3);
        productInOutRepositoryHwang.save(productInOut4);
    }

    //  해당 날짜 사이의 완제품 입출고 이력을 찾아 리스트로 리턴하는 메소드
    public List<ProductInOut> getProductInOutByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return productInOutRepositoryHwang.findByProductInDateBetween(startDate, endDate);
    }
}
