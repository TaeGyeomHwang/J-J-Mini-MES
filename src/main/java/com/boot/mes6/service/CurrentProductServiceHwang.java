package com.boot.mes6.service;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.CurrentProduct;
import com.boot.mes6.repository.CurrentProductRepositoryHwang;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrentProductServiceHwang {
    private final CurrentProductRepositoryHwang currentProductRepositoryHwang;

    public void saveInitProduct() {
        CurrentProduct cabbageJuice = new CurrentProduct();
        cabbageJuice.setCurrentProductName(ProductName.CABBAGE_JUICE);
        cabbageJuice.setCurrentProductAmount(0L);
        cabbageJuice.setCurrentProductSafeAmount(1000L);
        currentProductRepositoryHwang.save(cabbageJuice);

        CurrentProduct garlicJuice = new CurrentProduct();
        garlicJuice.setCurrentProductName(ProductName.GARLIC_JUICE);
        garlicJuice.setCurrentProductAmount(0L);
        garlicJuice.setCurrentProductSafeAmount(1000L);
        currentProductRepositoryHwang.save(garlicJuice);

        CurrentProduct pomegranateJelly = new CurrentProduct();
        pomegranateJelly.setCurrentProductName(ProductName.POMEGRANATE_JELLY);
        pomegranateJelly.setCurrentProductAmount(0L);
        pomegranateJelly.setCurrentProductSafeAmount(1000L);
        currentProductRepositoryHwang.save(pomegranateJelly);

        CurrentProduct plumJelly = new CurrentProduct();
        plumJelly.setCurrentProductName(ProductName.PLUM_JELLY);
        plumJelly.setCurrentProductAmount(0L);
        plumJelly.setCurrentProductSafeAmount(1000L);
        currentProductRepositoryHwang.save(plumJelly);
    }

    public void initializeCurrentProduct() {
        if (currentProductRepositoryHwang.count() == 0) {
            saveInitProduct();
        }
    }

    //완제품 재고 현황 정보 가져오기
    public List<CurrentProduct> getCurrentProduct() {
        return currentProductRepositoryHwang.findAll();
    }

    //완제품 안전 재고량에 추가
    public void addSafeProduct(ProductName productName, Long productAmount) {
//        Optional<CurrentProduct> optionalCurrentProduct = currentProductRepositoryHwang.getCurrentProductAmount(productName);
//
//        if (optionalCurrentProduct.isPresent()) {
//            //해당 완제품의 재고가 있을 경우 수량 업데이트
//            CurrentProduct currentProduct = optionalCurrentProduct.get();
//            Long currentProductSafeAmount = currentProduct.getCurrentProductSafeAmount() + productAmount;
//            currentProduct.setCurrentProductSafeAmount(currentProductSafeAmount);
//            currentProductRepositoryHwang.save(currentProduct);
//        }
        CurrentProduct currentProduct = currentProductRepositoryHwang.findByCurrentProductName(productName);
        currentProduct.setCurrentProductSafeAmount(currentProduct.getCurrentProductSafeAmount()+productAmount);
        currentProductRepositoryHwang.save(currentProduct);
    }

    //완제품 일반 재고량에 추가
    public void addNormalProduct(ProductName productName, Long productAmount) {
//        System.out.println("Adding normal product: " + productName + ", amount: " + productAmount);
//
//        Optional<CurrentProduct> optionalCurrentProduct = currentProductRepositoryHwang.getCurrentProductAmount(productName);
//
//        if (optionalCurrentProduct.isPresent()) {
//            CurrentProduct currentProduct = optionalCurrentProduct.get();
//            Long currentProductNormalAmount = currentProduct.getCurrentProductAmount() + productAmount;
//            currentProduct.setCurrentProductAmount(currentProductNormalAmount);
//            currentProductRepositoryHwang.save(currentProduct);
//        }
        CurrentProduct currentProduct = currentProductRepositoryHwang.findByCurrentProductName(productName);

        currentProduct.setCurrentProductAmount(currentProduct.getCurrentProductAmount()+productAmount);
        currentProductRepositoryHwang.save(currentProduct);
    }

    //완제품 안전 재고량에서 빼기
    public void minusSafeProduct(ProductName productName, Long productAmount) {
//        Optional<CurrentProduct> optionalCurrentProduct = currentProductRepositoryHwang.getCurrentProductAmount(productName);
//
//        if (optionalCurrentProduct.isPresent()) {
//            //해당 완재품의 재고가 있는 경우
//            CurrentProduct currentProduct = optionalCurrentProduct.get();
//            Long currentProductSafeAmount = currentProduct.getCurrentProductSafeAmount();
//
//            if (currentProductSafeAmount >= productAmount) {
//                //재고가 충분한 경우, 출고량을 차감
//                currentProduct.setCurrentProductSafeAmount(currentProductSafeAmount - productAmount);
//                currentProductRepositoryHwang.save(currentProduct);
//            } else {
//                //재고가 충분치 않다면 예외
//                throw new IllegalArgumentException("Insufficient stock for material: " + productName);
//            }
//        } else {
//            //해당 완제품의 재고가 없는 경우 예외
//            throw new IllegalArgumentException("Material not found in stock: " + productName);
//        }
        CurrentProduct currentProduct = currentProductRepositoryHwang.findByCurrentProductName(productName);
        currentProduct.setCurrentProductSafeAmount(currentProduct.getCurrentProductSafeAmount()-productAmount);
        currentProductRepositoryHwang.save(currentProduct);
    }

    //완제품 일반 재고량에서 빼기
    public void minusNormalProduct(ProductName productName, Long productAmount) {
//        Optional<CurrentProduct> optionalCurrentProduct = currentProductRepositoryHwang.getCurrentProductAmount(productName);
//
//        if (optionalCurrentProduct.isPresent()) {
//            //해당 완재품의 재고가 있는 경우
//            CurrentProduct currentProduct = optionalCurrentProduct.get();
//            Long currentProductAmount = currentProduct.getCurrentProductAmount();
//
//            if (currentProductAmount >= productAmount) {
//                //재고가 충분한 경우, 출고량을 차감
//                currentProduct.setCurrentProductSafeAmount(currentProductAmount - productAmount);
//                currentProductRepositoryHwang.save(currentProduct);
//            } else {
//                //재고가 충분치 않다면 예외
//                throw new IllegalArgumentException("Insufficient stock for material: " + productName);
//            }
//        } else {
//            //해당 완제품의 재고가 없는 경우 예외
//            throw new IllegalArgumentException("Material not found in stock: " + productName);
//        }
        CurrentProduct currentProduct = currentProductRepositoryHwang.findByCurrentProductName(productName);

        currentProduct.setCurrentProductAmount(currentProduct.getCurrentProductAmount()-productAmount);
        currentProductRepositoryHwang.save(currentProduct);
    }

}
