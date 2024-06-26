package com.boot.mes6.service;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.constant.MaterialStatus;
import com.boot.mes6.entity.CurrentTime;
import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.repository.MaterialRepository;
import com.boot.mes6.repository.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {
    private final TimeRepository timeRepository;
    private final MaterialRepository materialRepository;
    private final CurrentMaterialService currentMaterialService;

    //시스템 시간 가져오기
    public LocalDateTime getCurrentTime() {
        CurrentTime currentTime = timeRepository.findByCurrentTimeNo(1L);
        return currentTime.getCurrentTimeValue();
    }

    //시스템 시간 증가 시키기
    @Transactional
    public void increaseTime(int minutes){
        CurrentTime currentTime = timeRepository.findByCurrentTimeNo(1L);
        LocalDateTime currentTimeValue = currentTime.getCurrentTimeValue().plusMinutes(minutes);
        currentTime.setCurrentTimeValue(currentTimeValue);
        timeRepository.save(currentTime);
    }

    //발주한 원자재 배송 중인지, 도착했는지 원자재 상태 변경하는 메서드
    public void setMaterialStatus() {
        //원자재 발주 전인 애들 가져오기
        List<MaterialInOut> beforeShipping = materialRepository.findByMaterialStatus(MaterialStatus.PREPARING_SHIP);
        //현재 시간(시스템 시간) 가져오기
        LocalDateTime currentTime = timeRepository.findByCurrentTimeNo(1L).getCurrentTimeValue();

        //현재 시간 기준으로 원자재 발주 전인 애들의 receiptDate가 지났는지
        for(MaterialInOut materialInOut : beforeShipping){
            if(materialInOut.getMaterialReceiptDate().isBefore(currentTime)) {
                //지났으면 배송중으로 update
                materialInOut.setMaterialStatus(MaterialStatus.SHIPPING);
                materialRepository.save(materialInOut);
            }
        }

        //배송 중인 애들 가져오기
        List<MaterialInOut> Shipping = materialRepository.findByMaterialStatus(MaterialStatus.SHIPPING);
        //현재 시간 기준으로 배송 중인 애들의 material_name 확인해서
        //material_name 별로 receiptDate를 + 해서(양배추, 흑마늘, 벌꿀은 +2일, 메실,석류,콜라겐은 +3일, 포장지는 +7일, 박스는 +3일)
        for(MaterialInOut materialInOut : Shipping){
            LocalDateTime expectedInDate = calculateExpectedInDate(materialInOut.getMaterialReceiptDate(), materialInOut.getMaterialName());

            //현재 시간을 지났는지
            if(expectedInDate.isBefore(currentTime)) {
                // 지났으면 입고 완료 상태로 변경
                materialInOut.setMaterialStatus(MaterialStatus.IN);

                // 입고일 업데이트
                materialInOut.setMaterialInDate(expectedInDate.withHour(9));

                // 보관기한 업데이트 (입고일에 +2주)
                LocalDateTime expireDate = expectedInDate.plusDays(14);
                materialInOut.setMaterialExpireDate(expireDate.toLocalDate());

                materialRepository.save(materialInOut);

                //입고 완료가 되면 원자재 재고 현황에 원자재 추가
                currentMaterialService.addCurrentMaterial(materialInOut.getMaterialName(), materialInOut.getMaterialInoutAmount());
            }
        }
    }

    //입고일 계산 메서드
    private LocalDateTime calculateExpectedInDate(LocalDateTime receiptDate, MaterialName materialName) {
        if (receiptDate == null) {
            return null;
        }

        switch (materialName) {
            case CABBAGE:
            case GARLIC:
            case HONEY:
                return receiptDate.plusDays(2).withHour(9).withMinute(0).withSecond(0).withNano(0);
            case POMEGRANATE:
            case PLUM:
            case COLLAGEN:
            case BOX:
                return receiptDate.plusDays(3).withHour(9).withMinute(0).withSecond(0).withNano(0);
            case WRAPPER:
                return receiptDate.plusDays(7).withHour(9).withMinute(0).withSecond(0).withNano(0);
            default:
                return null;
        }
    }
}
