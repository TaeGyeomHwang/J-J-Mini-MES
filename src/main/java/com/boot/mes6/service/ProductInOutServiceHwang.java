package com.boot.mes6.service;

import com.boot.mes6.constant.OrderType;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.Order;
import com.boot.mes6.entity.ProductInOut;
import com.boot.mes6.repository.ProductInOutRepositoryHwang;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductInOutServiceHwang {
    private final ProductInOutRepositoryHwang productInOutRepositoryHwang;
    private final CurrentProductServiceHwang currentProductServiceHwang;

    //  더미 완제품 입출고 이력 데이터 생성
    public void createDummyProductLog() {
        ProductInOut productInOut1 = new ProductInOut();
        productInOut1.setProductName(ProductName.CABBAGE_JUICE);
        productInOut1.setProductInoutAmount(123L);
        productInOut1.setProductInDate(LocalDateTime.of(2024, 6, 25, 12, 0, 0));

        ProductInOut productInOut2 = new ProductInOut();
        productInOut2.setProductName(ProductName.GARLIC_JUICE);
        productInOut2.setProductInoutAmount(246L);
        productInOut2.setProductInDate(LocalDateTime.of(2024, 7, 25, 12, 0, 0));

        ProductInOut productInOut3 = new ProductInOut();
        productInOut3.setProductName(ProductName.POMEGRANATE_JELLY);
        productInOut3.setProductInoutAmount(90L);
        productInOut3.setProductInDate(LocalDateTime.of(2024, 6, 25, 12, 0, 0));

        ProductInOut productInOut4 = new ProductInOut();
        productInOut4.setProductName(ProductName.PLUM_JELLY);
        productInOut4.setProductInoutAmount(100L);
        productInOut4.setProductInDate(LocalDateTime.of(2024, 7, 25, 12, 0, 0));

        productInOutRepositoryHwang.save(productInOut1);
        productInOutRepositoryHwang.save(productInOut2);
        productInOutRepositoryHwang.save(productInOut3);
        productInOutRepositoryHwang.save(productInOut4);
    }

    //  해당 날짜 사이의 완제품 입출고 이력을 찾아 리스트로 리턴하는 메소드
    public List<ProductInOut> getProductInOutByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return productInOutRepositoryHwang.findByProductInDateBetween(startDate, endDate);
    }

    //완제품 입출고 이력 테이블 띄울 때 페이징으로 10개 단위로 DB에서 가져오기
    public Page<ProductInOut> getProductInOutPage(Pageable pageable) {
        return productInOutRepositoryHwang.findAllOrderByProductNoDesc(pageable);
    }

    //완제품 입고(생산이 완료되어 입고, 완제품 재고에 추가)
    //order에서 업체인지 개인인지, 긴급인지 아닌지
    //완제품 입출고 이력에 추가할 때 필요 정보는 완제품명(order.getProductType()), 입고량(productAmount, 입고일(inDate)
    public void productIn(Order order, Long productAmount, LocalDateTime inDate) {
        try {
            //완제품 입출고 이력에 추가
            //(업체와 개인이) 긴급인지 긴급이라면 안전 재고량으로 입고 아니면 밑으로
            //긴급일 때
            if (order.isOrderIsEmergency()) {
                //완제품 입출고 이력에 추가(입고)
                productInOutRepositoryHwang.save(createInData(order.getOrderProductType(), productAmount, inDate, 1));

                //완제품 재고량에 안전 재고량 추가(입고)
                currentProductServiceHwang.addSafeProduct(order.getOrderProductType(), productAmount);

            } else {
                //긴급 아닐 때
                //업체인지 개인인지, 업체면 일반 재고량 입고, 개인이면 안전 재고량 입고
                //업체일 때
                if (order.getOrderType().equals(OrderType.COMPANY)) {
                    //완제품 입출고 이력에 추가(입고)
                    ProductInOut savedProductInOut = productInOutRepositoryHwang.save(createInData(order.getOrderProductType(), productAmount, inDate, 1));
                    System.out.println("Saved ProductInOut: " + savedProductInOut);

                    //완제품 재고량에 일반 재고량 추가(입고)
                    currentProductServiceHwang.addNormalProduct(order.getOrderProductType(), productAmount);

                } else {
                    //개인일 때
                    productInOutRepositoryHwang.save(createInData(order.getOrderProductType(), productAmount, inDate, 1));

                    //완제품 재고량에 안전 재고량 추가(입고)
                    currentProductServiceHwang.addSafeProduct(order.getOrderProductType(), productAmount);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred during productIn: " + e.getMessage());
            e.printStackTrace();
            throw e; // 예외를 다시 던져서 트랜잭션 관리가 계속되도록 합니다.
        }
    }

    //완제품 출고(수주에 대한 생산이 완료되어 출고, 왼제품 재고에서 마이너스)
    public void productOut(Order order, Long productAmount, LocalDateTime outDate) {
        //완제품 입출고 이력에 추가
        //(업체와 개인이) 긴급인지 긴급이라면 안전 재고량으로 출고 아니면 밑으로
        //업체인지 개인인지, 업체면 일반 재고량 출고, 개인이면 안전 재고량 출고
        //긴급일 때
        if (order.isOrderIsEmergency()) {
            //완제품 입출고 이력에 추가(출고)
            productInOutRepositoryHwang.save(createInData(order.getOrderProductType(), productAmount, outDate, 0));

            //완제품 재고량에 안전 재고량 추가(출고)
            currentProductServiceHwang.minusSafeProduct(order.getOrderProductType(), productAmount);

        } else {
            //긴급 아닐 때
            //업체인지 개인인지, 업체면 일반 재고량 출고, 개인이면 안전 재고량 출고
            //업체일 때
            if (order.getOrderType().equals(OrderType.COMPANY)) {
                //완제품 입출고 이력에 추가(출고)
                productInOutRepositoryHwang.save(createInData(order.getOrderProductType(), productAmount, outDate, 0));

                //완제품 재고량에 일반 재고량 추가(출고)
                currentProductServiceHwang.minusNormalProduct(order.getOrderProductType(), productAmount);

            } else {
                //개인일 때
                productInOutRepositoryHwang.save(createInData(order.getOrderProductType(), productAmount, outDate, 0));

                //완제품 재고량에 안전 재고량 추가(출고)
                currentProductServiceHwang.minusSafeProduct(order.getOrderProductType(), productAmount);
            }
        }
    }

    //입고 객체 만들어주는 메서드
    public ProductInOut createInData(ProductName productName, Long productAmount, LocalDateTime Date, int isIn) {
        ProductInOut productInOut = new ProductInOut();
        productInOut.setProductName(productName);
        productInOut.setProductInoutAmount(productAmount);
        if (isIn == 1) {
            productInOut.setProductInDate(Date); //입고일
        } else {
            productInOut.setProductOutDate(Date); //출고일
        }

        System.out.println("ProductInOut to save: " + productInOut);
        return productInOut;
    }
}
