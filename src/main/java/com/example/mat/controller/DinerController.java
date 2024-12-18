package com.example.mat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mat.dto.diner.DinerDto;
import com.example.mat.service.DinerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/diner")
@Controller
public class DinerController {

    private final DinerService dinerService;

    @GetMapping("/list")
    public void getDinerList() {
        log.info("get diner list 페이지 요청");
    }

    @GetMapping("/idx")
    public void getDinerIdx() {
        log.info("get diner idx 페이지 요청");
    }

    @GetMapping("/read")
    public void getDinerRead() {
        log.info("get diner read 페이지 요청");
    }

    @GetMapping("/register")
    public void getDinerRegister() {
        log.info("get diner register 페이지 요청");
    }

    @PostMapping("/create")
    public String postCreateDiner(@Valid DinerDto dinerDto, BindingResult result) {
        log.info("식당 등록 {}", dinerDto);

        if (result.hasErrors()) {
            return "/diner/register";
        }

        Long did = dinerService.createDiner(dinerDto);
        log.info("식당 등록 완료 {}", did);

        return "/diner/list";
    }

    @GetMapping("/review")
    public void getDinerReview() {
        log.info("get diner review 페이지 요청");
    }

}
