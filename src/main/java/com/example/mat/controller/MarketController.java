package com.example.mat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RequestMapping("/market")
@Controller
public class MarketController {

    @GetMapping("/list")
    public void getMarketList() {
        log.info("market list 페이지 요청");
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

}
