package com.example.mat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.market.ProductDto;
import com.example.mat.entity.market.Product;
import com.example.mat.service.ProductService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RequestMapping("/market")
@Controller
public class MarketController {

    @Autowired
    ProductService productService;

    @GetMapping("/list")
    public void getList(PageRequestDto requestDto, Model model) {
        log.info("식자재 전체 목록 요청");

        PageResultDto<ProductDto, Product> result = productService.getList(requestDto);
        model.addAttribute("result", result);

    }

    @GetMapping("/details")
    public void getMarketRead() {
        log.info("food 상세 페이지 요청");
    }

    @GetMapping("/cart")
    public void getMarketCart() {
        log.info("cart 페이지 요청");
    }

    @GetMapping("/order")
    public void getOrder() {
        log.info("order 페이지 요청");
    }

    @GetMapping("/orderlist")
    public void getOrderList() {
        log.info("orderlist 페이지 요청");
    }

}
