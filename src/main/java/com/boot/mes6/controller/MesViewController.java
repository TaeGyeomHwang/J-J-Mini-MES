package com.boot.mes6.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MesViewController {

    @GetMapping("/materialinout")
    public String materialinout() {
        return "Material_in_out";
    }
}
