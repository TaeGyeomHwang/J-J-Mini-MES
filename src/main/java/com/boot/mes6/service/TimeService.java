package com.boot.mes6.service;

import com.boot.mes6.constant.MaterialStatus;
import com.boot.mes6.entity.CurrentTime;
import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.repository.MaterialRepository;
import com.boot.mes6.repository.TimeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeService {
    private final TimeRepository timeRepository;
    private final MaterialRepository MaterialRepository;
    private final MaterialRepository materialRepository;

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

        //현재 시간 기준으로 원자재 발주 전인 애들의 receiptDate가 지났는지
        //지났으면 배송중으로 update

        //배송 중인 애들 가져오기
        //현재 시간 기준으로 배송 중인 애들의 material_name 확인해서
        //material_name 별로 receiptDate를 + 해서
        //현재 시간을 지났는지
        //지났으면 입고 완료 상태로 변경하고
        //입고일을 +된 receiptDate를 업데이트
        //+된 receiptDate에 + 2주를 해서 보관기한 업데이트

    }
}
