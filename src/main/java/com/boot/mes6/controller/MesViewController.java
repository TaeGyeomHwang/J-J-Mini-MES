package com.boot.mes6.controller;

import com.boot.mes6.entity.MaterialInOut;
import com.boot.mes6.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MesViewController {
    private final MaterialService materialService;

    //원자재 입출고 관리 페이지
    @GetMapping("/materialinout")
    public String materialinout(Model model) {
        List<MaterialInOut> materials = materialService.getAllMaterial();

        model.addAttribute("materials", materials);

        return "Material_in_out";
    }

    //원자재 발주 페이지
    @GetMapping("/materialAdd")
    public String materialAdd() {
        return "Material_Add";
    }
}
