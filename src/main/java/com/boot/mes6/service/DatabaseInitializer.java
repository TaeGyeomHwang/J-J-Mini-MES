package com.boot.mes6.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
class DatabaseInitializer implements ApplicationRunner {

    private final CurrentTimeServiceHwang currentTimeServiceHwang;
    private final CurrentProductServiceHwang currentProductServiceHwang;
    private final CurrentMaterialService currentMaterialService;

    @Override
    public void run(ApplicationArguments args) {
        currentTimeServiceHwang.initializeCurrentTime();
        currentProductServiceHwang.initializeCurrentProduct();
        currentMaterialService.initializeCurrentMaterial();

    }
}
