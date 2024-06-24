package com.boot.mes6.service;

import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.CurrentProduct;
import com.boot.mes6.entity.CurrentTime;
import com.boot.mes6.repository.CurrentProductRepositoryHwang;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class CurrentProductServiceHwang {
    private final CurrentProductRepositoryHwang currentProductRepositoryHwang;

    public void saveInitProduct() {
        CurrentProduct cabbageJuice = new CurrentProduct();
        cabbageJuice.setCurrentProductName(ProductName.CABBAGE_JUICE);
        cabbageJuice.setCurrentProductAmount(0L);
        cabbageJuice.setCurrentProductSafeAmount(0L);
        currentProductRepositoryHwang.save(cabbageJuice);

        CurrentProduct garlicJuice = new CurrentProduct();
        garlicJuice.setCurrentProductName(ProductName.GARLIC_JUICE);
        garlicJuice.setCurrentProductAmount(0L);
        garlicJuice.setCurrentProductSafeAmount(0L);
        currentProductRepositoryHwang.save(garlicJuice);

        CurrentProduct pomegranateJelly = new CurrentProduct();
        pomegranateJelly.setCurrentProductName(ProductName.POMEGRANATE_JELLY);
        pomegranateJelly.setCurrentProductAmount(0L);
        pomegranateJelly.setCurrentProductSafeAmount(0L);
        currentProductRepositoryHwang.save(pomegranateJelly);

        CurrentProduct plumJelly = new CurrentProduct();
        plumJelly.setCurrentProductName(ProductName.PLUM_JELLY);
        plumJelly.setCurrentProductAmount(0L);
        plumJelly.setCurrentProductSafeAmount(0L);
        currentProductRepositoryHwang.save(plumJelly);
    }

    public void initializeCurrentProduct() {
        if (currentProductRepositoryHwang.count() == 0) {
            saveInitProduct();
        }
    }
}
