package com.boot.mes6.controller;

import com.boot.mes6.dto.DataTableResponse;
import com.boot.mes6.dto.ResponseProduct;
import com.boot.mes6.entity.CurrentProduct;
import com.boot.mes6.entity.ProductInOut;
import com.boot.mes6.service.CurrentProductServiceHwang;
import com.boot.mes6.service.ProductInOutServiceHwang;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductInOutServiceHwang productInOutServiceHwang;
    private final CurrentProductServiceHwang currentProductServiceHwang;

    //완제품 입출고 관리 페이지
    @GetMapping("/productinout")
    public String productInOut() {
        return "product/Product_in_out";
    }

    //완제품 재고 현황 페이지
    @GetMapping("/currentProduct")
    public String CurrentProduct(Model model) {
        List<CurrentProduct> currentProducts = currentProductServiceHwang.getCurrentProduct();

        model.addAttribute("currentProducts", currentProducts);

        return "product/Current_Product";
    }

    // JSON 데이터를 제공하는 엔드포인트
    //완제품 입출고 테이블에 줄 데이터
    @GetMapping("/productinoutTable")
    public ResponseEntity<DataTableResponse<ResponseProduct>> productInOutTable(
            @RequestParam(defaultValue = "0") int draw,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "10") int length
    ) {
        //
        Pageable pageable = PageRequest.of(start / length, length);
        Page<ProductInOut> productInOutPage = productInOutServiceHwang.getProductInOutPage(pageable);

        List<ResponseProduct> responseProducts = productInOutPage.getContent().stream()
                .map(product -> {
                    ResponseProduct responseProduct = new ResponseProduct();
                    responseProduct.setProductNo(product.getProductNo());
                    responseProduct.setProductName(product.getProductName().getDescription());
                    responseProduct.setProductInoutAmount(product.getProductInoutAmount());
                    responseProduct.setProductInDate(product.getProductInDate());
                    responseProduct.setProductOutDate(product.getProductOutDate());

                    return responseProduct;
                })
                .collect(Collectors.toList());

        DataTableResponse<ResponseProduct> response = new DataTableResponse<>();
        response.setDraw(draw);
        response.setRecordsTotal(productInOutPage.getTotalElements());
        response.setRecordsFiltered(productInOutPage.getTotalElements());
        response.setData(responseProducts);

        return ResponseEntity.ok().body(response);
    }


}
