package com.boot.mes6.service;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.entity.CurrentMaterial;
import com.boot.mes6.repository.CurrentMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrentMaterialService {
    private final CurrentMaterialRepository currentMaterialRepository;

    @Transactional
    public void addCurrentMaterial(MaterialName materialName, Long amount) {
        Optional<CurrentMaterial> optionalCurrentMaterial = currentMaterialRepository.findByCurrentMaterialName(materialName);

        if (optionalCurrentMaterial.isPresent()) {
            // 이미 해당 원자재가 재고에 있을 경우, 수량을 업데이트
            CurrentMaterial currentMaterial = optionalCurrentMaterial.get();
            Long currentAmount = currentMaterial.getCurrentMaterialAmount();
            currentMaterial.setCurrentMaterialAmount(currentAmount + amount);
        } else {
            // 재고에 없는 경우, 새로 추가
            CurrentMaterial newCurrentMaterial = new CurrentMaterial();
            newCurrentMaterial.setCurrentMaterialName(materialName);
            newCurrentMaterial.setCurrentMaterialAmount(amount);
            currentMaterialRepository.save(newCurrentMaterial);
        }
    }
}
