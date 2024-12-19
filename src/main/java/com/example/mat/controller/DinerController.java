package com.example.mat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.service.DinerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/diner")
@Controller
public class DinerController {

    private final DinerService dinerService;

    @GetMapping("/list")
    public void getDinerList(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, Model model) {
        log.info("get diner list 페이지 요청");
        // PageResultDto<DinerDto, Object[]> result =
        // dinerService.getList(pageRequestDto);

        // model.addAttribute("result", result);
        // return "/diner/list";
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
    public String postCreateDiner(DinerDto dinerDto,
            @ModelAttribute("requestDto") PageRequestDto pageRequestDto,
            RedirectAttributes rttr) {
        log.info("식당 등록 {}", dinerDto);

        Long did = dinerService.createDiner(dinerDto);
        log.info("식당 등록 완료 {}", did);

        rttr.addAttribute("did", did);
        rttr.addAttribute("page", 1);
        rttr.addAttribute("size", pageRequestDto.getSize());
        rttr.addAttribute("type", pageRequestDto.getType());
        rttr.addAttribute("keyword", pageRequestDto.getKeyword());

        return "/diner/list";
    }

    @GetMapping("/review")
    public void getDinerReview() {
        log.info("get diner review 페이지 요청");
    }

}
