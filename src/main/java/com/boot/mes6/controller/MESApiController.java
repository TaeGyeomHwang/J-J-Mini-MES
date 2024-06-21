package com.boot.mes6.controller;

import com.boot.mes6.constant.MaterialStatus;
import com.boot.mes6.constant.OrderStatus;
import com.boot.mes6.constant.OrderType;
import com.boot.mes6.constant.ProductName;
import com.boot.mes6.dto.AddMaterial;
import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.entity.Order;
import com.boot.mes6.repository.MaterialRepository;
import com.boot.mes6.service.MaterialService;
import com.boot.mes6.service.TimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MESApiController {
    private final TimeService timeService;
    private final MaterialService materialService;
    private final MaterialRepository materialRepository;

    //수동 원자재 발주 추가
    //아직 미완성
    @PostMapping("/AddMaterial")
    public ResponseEntity<AddMaterial> addMaterial(@RequestBody AddMaterial addMaterial) {
        //System.out.println("good");
        LocalDateTime currentTime = timeService.getCurrentTime();

        System.out.println("1 : " + addMaterial.getMaterialName());
        System.out.println("2 : " + addMaterial.getMaterialInoutAmount());
        System.out.println("3 : " + addMaterial.getMaterialSupplierName());

        MaterialInOut materialInOut = new MaterialInOut();
        materialInOut.setMaterialName(addMaterial.getMaterialName());
        materialInOut.setMaterialInoutAmount(addMaterial.getMaterialInoutAmount());
        materialInOut.setMaterialSupplierName(addMaterial.getMaterialSupplierName());
        materialInOut.setMaterialOrderDate(currentTime);
        materialInOut.setMaterialStatus(MaterialStatus.PREPARING_SHIP);

        //토요일과 일요일을 제외하고 currentTime이 오후 12시 이전 이라면 ReceiptDate를 currentTime의 해당 날짜의 12시로 설정
        //오후 12시 이후라면 currentTime의 해당 날짜에서 +1일 12시로 설정
        //주말을 제외했으니
        //materialInOut.setMaterialReceiptDate();

        materialService.addMaterial(materialInOut);

        return ResponseEntity.ok().body(addMaterial);
    }

    //원자재가 자동 발주되는지 테스트
    @PostMapping("/test")
    public ResponseEntity<Void> test() {
        LocalDateTime currentTime = timeService.getCurrentTime();

        Order order = new Order();
        order.setOrderNo(2L);
        order.setOrderType(OrderType.COMPANY);
        order.setOrderProductType(ProductName.CABBAGE_JUICE);
        order.setOrderAmount(1000L);
        order.setOrderCustomerName("John Doe");
        order.setOrderDate(currentTime);
        order.setOrderStatus(OrderStatus.BEFORE_REGISTER);

        materialService.autoAddMaterial(order);

        return null;
    }
}
