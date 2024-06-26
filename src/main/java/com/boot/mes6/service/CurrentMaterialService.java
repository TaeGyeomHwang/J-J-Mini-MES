package com.boot.mes6.service;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.entity.CurrentMaterial;
import com.boot.mes6.repository.CurrentMaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrentMaterialService {
    private final CurrentMaterialRepository currentMaterialRepository;

    //원자재 재고 가져오기
    public List<CurrentMaterial> getCurrentMaterial() {
        //List<CurrentMaterial> currentMaterial = currentMaterialRepository.findAll();
        return currentMaterialRepository.findAll();
    }

    //원자재가 입고되었을 때 원자재 재고 현황에 추가
    @Transactional
    public void addCurrentMaterial(MaterialName materialName, Long amount) {
        Optional<CurrentMaterial> optionalCurrentMaterial = currentMaterialRepository.findByCurrentMaterialName(materialName);

        if (optionalCurrentMaterial.isPresent()) {
            // 이미 해당 원자재가 재고에 있을 경우, 수량을 업데이트
            CurrentMaterial currentMaterial = optionalCurrentMaterial.get();
            Long currentAmount = currentMaterial.getCurrentMaterialAmount();
            currentMaterial.setCurrentMaterialAmount(currentAmount + amount);
            currentMaterialRepository.save(currentMaterial);
        } /*else {
            // 재고에 없는 경우, 새로 추가
            CurrentMaterial newCurrentMaterial = new CurrentMaterial();
            newCurrentMaterial.setCurrentMaterialName(materialName);
            newCurrentMaterial.setCurrentMaterialAmount(amount);
            currentMaterialRepository.save(newCurrentMaterial);
        }*/
    }

    //원자재가 출고되었을 때 원자재 재고 현황에 빼기
    @Transactional
    public void minusCurrentMaterial(MaterialName materialName, Long outAmount) {
        Optional<CurrentMaterial> optionalCurrentMaterial = currentMaterialRepository.findByCurrentMaterialName(materialName);

        if (optionalCurrentMaterial.isPresent()) {
            // 해당 원자재가 재고에 있는 경우
            CurrentMaterial currentMaterial = optionalCurrentMaterial.get();
            Long currentAmount = currentMaterial.getCurrentMaterialAmount();

            if (currentAmount >= outAmount) {
                // 재고가 충분한 경우, 출고량을 차감
                currentMaterial.setCurrentMaterialAmount(currentAmount - outAmount);
                currentMaterialRepository.save(currentMaterial);
            } else {
                // 재고가 충분하지 않은 경우, 예외 발생
                throw new IllegalArgumentException("Insufficient stock for material: " + materialName);
            }
        } else {
            // 해당 원자재가 재고에 없는 경우, 예외 발생
            throw new IllegalArgumentException("Material not found in stock: " + materialName);
        }
    }

    //서버 실행 시 원자재 재고 테이블 init
    public void initializeCurrentMaterial() {
        //양배추
        CurrentMaterial currentCabbageMaterial = new CurrentMaterial();
        currentCabbageMaterial.setCurrentMaterialName(MaterialName.CABBAGE);
        currentCabbageMaterial.setCurrentMaterialAmount(0L);
        currentMaterialRepository.save(currentCabbageMaterial);

        //흑마늘
        CurrentMaterial currentGarlicMaterial = new CurrentMaterial();
        currentGarlicMaterial.setCurrentMaterialName(MaterialName.GARLIC);
        currentGarlicMaterial.setCurrentMaterialAmount(0L);
        currentMaterialRepository.save(currentGarlicMaterial);

        //벌꿀
        CurrentMaterial currentHoneyMaterial = new CurrentMaterial();
        currentHoneyMaterial.setCurrentMaterialName(MaterialName.HONEY);
        currentHoneyMaterial.setCurrentMaterialAmount(0L);
        currentMaterialRepository.save(currentHoneyMaterial);

        //석류(농축액)
        CurrentMaterial currentPomegranateMaterial = new CurrentMaterial();
        currentPomegranateMaterial.setCurrentMaterialName(MaterialName.POMEGRANATE);
        currentPomegranateMaterial.setCurrentMaterialAmount(0L);
        currentMaterialRepository.save(currentPomegranateMaterial);

        //매실(농축액)
        CurrentMaterial currentPlumMaterial = new CurrentMaterial();
        currentPlumMaterial.setCurrentMaterialName(MaterialName.PLUM);
        currentPlumMaterial.setCurrentMaterialAmount(0L);
        currentMaterialRepository.save(currentPlumMaterial);

        //콜라겐
        CurrentMaterial currentCollagenMaterial = new CurrentMaterial();
        currentCollagenMaterial.setCurrentMaterialName(MaterialName.COLLAGEN);
        currentCollagenMaterial.setCurrentMaterialAmount(0L);
        currentMaterialRepository.save(currentCollagenMaterial);

        //포장지
        CurrentMaterial currentWrapperMaterial = new CurrentMaterial();
        currentWrapperMaterial.setCurrentMaterialName(MaterialName.WRAPPER);
        currentWrapperMaterial.setCurrentMaterialAmount(100000L);
        currentMaterialRepository.save(currentWrapperMaterial);

        //Box
        CurrentMaterial currentBoxMaterial = new CurrentMaterial();
        currentBoxMaterial.setCurrentMaterialName(MaterialName.BOX);
        currentBoxMaterial.setCurrentMaterialAmount(10000L);
        currentMaterialRepository.save(currentBoxMaterial);
    }
}
