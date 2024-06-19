package com.boot.mes6.service;

import com.boot.mes6.constant.FacilityName;
import com.boot.mes6.constant.ProcessCode;
import com.boot.mes6.constant.ProductType;
import com.boot.mes6.entity.Plan;
import com.boot.mes6.entity.WorkOrder;
import com.boot.mes6.repository.PlanRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class WorkOrderService {

    private final PlanRepository planRepository;

    public Long createNewWorkOrder(Long planNo, ProductType productType) {
        Plan plan = planRepository.findById(planNo)
                .orElseThrow(EntityNotFoundException::new);

        LocalDateTime planStartDate = plan.getPlanStartDate();

        WorkOrder workOrder = new WorkOrder();
        if (productType != ProductType.JELLY) {
            workOrder.setWorkOrderType(ProductType.JUICE);
            for (int i = 0; i < 7; i++) {
                switch (i){
                    case 0: //  세척
                        workOrder.setWorkOrderProcessName(ProcessCode.A1);
                        workOrder.setWorkOrderFacilityName(FacilityName.NONE);
                        workOrder.setWorkOrderInput(plan.getPlanProductionAmount());
                    case 1: //  추출
                    case 2: //  여과
                    case 3: //  살균
                    case 4: //  충진
                    case 5: //  검사
                    case 6: //  포장
                }
            }
        } else {
            workOrder.setWorkOrderType(ProductType.JELLY);
            for (int i = 0; i < 6; i++) {
                switch (i){
                    case 0: //  혼합
                    case 1: //  살균
                    case 2: //  충진
                    case 3: //  냉각
                    case 4: //  검사
                    case 5: //  포장
                }
            }
        }

        return workOrder.getWorkOrderNo();
    }
    
    //  즙 세척 용량 계산
    private Long
}
