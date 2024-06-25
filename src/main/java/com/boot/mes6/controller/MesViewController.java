package com.boot.mes6.controller;

import com.boot.mes6.dto.DataTableResponse;
import com.boot.mes6.dto.ResponseMaterial;
import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MesViewController {
    private final MaterialService materialService;

    // 원자재 입출고 관리 페이지
    @GetMapping("/materialinout")
    public String materialinout() {
        //List<MaterialInOut> materials = materialService.getAllMaterial();

        //model.addAttribute("materials", materials);

        return "Material_in_out";
    }

    // 원자재 입출고 관리 테이블 정보
    @GetMapping("/materialinoutTable")
    public ResponseEntity<DataTableResponse<ResponseMaterial>> materialInOutTable(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length
    ) {
        Pageable pageable = PageRequest.of(start / length, length);
        Page<MaterialInOut> materialInOutPage = materialService.getMaterialInOutPage(pageable);
        //System.out.println("이거 안나와? : " + materialInOutPage);

        List<ResponseMaterial> responseMaterials = materialInOutPage.getContent().stream()
                .map(material -> {
                    ResponseMaterial responseMaterial = new ResponseMaterial();
                    responseMaterial.setMaterialNo(material.getMaterialNo());
                    responseMaterial.setMaterialName(material.getMaterialName().getDescription());
                    responseMaterial.setMaterialInoutAmount(material.getMaterialInoutAmount());
                    responseMaterial.setMaterialSupplierName(material.getMaterialSupplierName().getDescription());
                    responseMaterial.setMaterialOrderDate(material.getMaterialOrderDate());
                    responseMaterial.setMaterialReceiptDate(material.getMaterialReceiptDate());
                    responseMaterial.setMaterialInDate(material.getMaterialInDate());
                    responseMaterial.setMaterialOutDate(material.getMaterialOutDate());
                    responseMaterial.setMaterialExpireDate(material.getMaterialExpireDate());
                    responseMaterial.setMaterialStatus(material.getMaterialStatus().getDescription());

                    return responseMaterial;
                })
                .collect(Collectors.toList());

        DataTableResponse<ResponseMaterial> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(materialInOutPage.getTotalElements());
        response.setRecordsFiltered(materialInOutPage.getTotalElements());
        response.setData(responseMaterials);

        return ResponseEntity.ok().body(response);
    }
}
