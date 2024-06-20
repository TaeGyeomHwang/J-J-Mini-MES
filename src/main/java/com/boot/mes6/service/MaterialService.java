package com.boot.mes6.service;

import com.boot.mes6.constant.MaterialName;
import com.boot.mes6.constant.MaterialStatus;
import com.boot.mes6.constant.MaterialSupplierName;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.entity.Order;
import com.boot.mes6.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;

    //원자재 입출고 이력 가져오기
    public List<MaterialInOut> getAllMaterial() {
        return materialRepository.findAll();
    }

    //원자재 입출고 이력에 추가하기
    public void addMaterial(MaterialInOut materialInOut) {
        materialRepository.save(materialInOut);
    }

    //자동 원자재 발주
    //수주 추가가 되면 추가된 수주의 정보를 Order로 받아옴
    public void autoAddMaterial(Order order) {
        //먼저 제품명에 따른 원자재량 계산
        //어떤 제품이냐에 따라 필요 원자재 계산
        //소수점이 나오면 무조건 올림
        long needMaterialAmount = 0;
        long needSubMaterialAmount = 0;
        MaterialName materialName = MaterialName.CABBAGE;
        MaterialSupplierName materialSupplierName = MaterialSupplierName.A_FARM;

        //몇 박스 들어왔는지 가져와서 불량률을 고려해서 생산해야할 양 계산
        long productionAmount = (long) (order.getOrderAmount() + Math.ceil(order.getOrderAmount() * 0.03));

        //양배추의 경우
        if(order.getOrderProductType().equals(ProductName.CABBAGE_JUICE)) {
            //필요한 양배추 계산
            needMaterialAmount = (long) Math.ceil(productionAmount * 30 * 10 / 0.5 / 0.2 / 0.75 / 1000);

            //필요 벌꿀 계산
            needSubMaterialAmount = (long) Math.ceil((productionAmount * 30 * 5) / 1000.0);

            //
            //materialName = MaterialName.CABBAGE;

            //흑마늘의 경우
        } else if(order.getOrderProductType().equals(ProductName.GARLIC_JUICE)){
            //필요한 흑마늘 계산
            needMaterialAmount = (long) Math.ceil(productionAmount * 30 * 10 / 0.5 / 0.2 / 0.75 / 1000);

            //필요 벌꿀 계산
            needSubMaterialAmount = (long) Math.ceil((productionAmount * 30 * 5) / 1000.0);

            //
            materialName = MaterialName.GARLIC;

            //석류 젤리스틱의 경우
        } else if(order.getOrderProductType().equals(ProductName.POMEGRANATE_JELLY) || order.getOrderProductType().equals(ProductName.PLUM_JELLY)) {
            //필요한 석류 농축액 계산
            needMaterialAmount = (long) Math.ceil(productionAmount * 25 * 5 / 1000.0);

            //필요 콜라겐 계산
            needSubMaterialAmount = (long) Math.ceil((productionAmount * 25 * 2) / 1000.0);

            materialName = MaterialName.POMEGRANATE;

            materialSupplierName = MaterialSupplierName.OO_NH;

            //매실 젤리스틱의 경우
        } else {
            //필요한 매실 농축액 계산
            needMaterialAmount = (long) Math.ceil(productionAmount * 25 * 5 / 1000.0);

            //필요 콜라겐 계산
            needSubMaterialAmount = (long) Math.ceil((productionAmount * 25 * 2) / 1000.0);

            materialName = MaterialName.PLUM;

            materialSupplierName = MaterialSupplierName.OO_NH;
        }

        //repository에서 조건대로 List 가져오기
        List<MaterialInOut> materialList = getMaterialList(order.getOrderDate(), materialName);

        int count = 0;

        for(int i = 0; i < materialList.size() + 1; i++) {
            //다른 날이라 비교할 대상이 없다.
            if(materialList.isEmpty()) {
                do {
                    MaterialInOut materialInOut = new MaterialInOut();
                    materialInOut.setOrder(order); //order_no
                    materialInOut.setMaterialName(materialName); //원자재 명
                    if(needMaterialAmount > 2000) {
                        materialInOut.setMaterialInoutAmount(2000L); //수주량에 따른 발주량
                    } else if(needMaterialAmount < 0L) {
                        break;
                    } else {
                        materialInOut.setMaterialInoutAmount(needMaterialAmount); //수주량에 따른 발주량
                    }
                    materialInOut.setMaterialSupplierName(materialSupplierName); //발주 업체명
                    materialInOut.setMaterialOrderDate(order.getOrderDate()); //발주일 = 수주일

                    if(order.getOrderDate().getHour() >= 12 && order.getOrderDate().getDayOfWeek() == DayOfWeek.FRIDAY) {
                        materialInOut.setMaterialReceiptDate(order.getOrderDate().plusDays(3+count).withHour(12).withMinute(0).withSecond(0).withNano(0)); //발주 접수일
                    } else if(order.getOrderDate().getDayOfWeek() == DayOfWeek.SATURDAY){
                        materialInOut.setMaterialReceiptDate(order.getOrderDate().plusDays(2+count).withHour(12).withMinute(0).withSecond(0).withNano(0));
                    } else if(order.getOrderDate().getDayOfWeek() == DayOfWeek.SUNDAY) {
                        materialInOut.setMaterialReceiptDate(order.getOrderDate().plusDays(1+count).withHour(12).withMinute(0).withSecond(0).withNano(0));
                    } else if (order.getOrderDate().getHour() >= 12) {
                        materialInOut.setMaterialReceiptDate(order.getOrderDate().plusDays(1+count).withHour(12).withMinute(0).withSecond(0).withNano(0)); //발주 접수일
                    } else {
                        materialInOut.setMaterialReceiptDate(order.getOrderDate().plusDays(count).withHour(12).withMinute(0).withSecond(0).withNano(0));
                    }

                    materialInOut.setMaterialStatus(MaterialStatus.PREPARING_SHIP);
                    materialRepository.save(materialInOut);

                    count += 1;
                    needMaterialAmount -= 2000L;

                } while(needMaterialAmount > 0);
                break;

            } else {
                //뭐부터 검사를 해야하지?
                if(materialList.size() == 1) {
                    Long beforeAmount = materialList.get(i).getMaterialInoutAmount();
                    if(beforeAmount + needMaterialAmount > 2000) {

                    }
                }





            }
        }
    }

    public List<MaterialInOut> getMaterialList(LocalDateTime orderDate, MaterialName materialName) {
        //orderDate가 오후 12시를 넘었는가?
        if(orderDate.getHour() >= 12) {
            //찾아올 쿼리의 조건 = 해당 일 12시를 넘거나 다음 일 12시를 넘지 않는 원자재 발주 && 원자재명이 같은 원자재 발주
            LocalDateTime startTime = orderDate.withHour(12).withMinute(0).withSecond(0).withNano(0);
            LocalDateTime endTime = startTime.plusDays(1);

            return materialRepository.findByMaterialNameAndMaterialOrderDateBetweenOrderByMaterialNo(materialName, startTime, endTime);
        }
        //orderDate가 오전인가?
        else {
            LocalDateTime startTime = orderDate.minusDays(1).withHour(12).withMinute(0).withSecond(0).minusNanos(0);
            LocalDateTime endTime = startTime.plusDays(1);

            return materialRepository.findByMaterialNameAndMaterialOrderDateBetweenOrderByMaterialNo(materialName, startTime, endTime);
        }
    }

}