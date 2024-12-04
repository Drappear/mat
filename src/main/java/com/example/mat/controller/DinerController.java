package com.example.mat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mat.dto.PageRequestDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/diner")
@Controller
public class DinerController {

    @GetMapping("/list")
    public void getMethodName(@ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
        log.info("리스트 폼 요청");
    }
}
