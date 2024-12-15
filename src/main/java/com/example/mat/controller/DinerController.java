package com.example.mat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@RequestMapping("/diner")
@Controller
public class DinerController {

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
    @GetMapping("/review")
    public void getDinerReview() {
        log.info("get diner review 페이지 요청");
    }

}
