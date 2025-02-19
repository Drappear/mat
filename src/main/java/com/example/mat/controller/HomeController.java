package com.example.mat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.diner.DinerDto;
import com.example.mat.service.DinerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
@Controller
public class HomeController {

  private final DinerService dinerService;

  @GetMapping("/main")
  public void getMain(@ModelAttribute("requestDto") PageRequestDto pageRequestDto, Model model) {
    log.info("main 요청");
    pageRequestDto.setSize(8);
    PageResultDto<DinerDto, Object[]> dinerResult = dinerService.getDinerList(pageRequestDto);
    model.addAttribute("dinerResult", dinerResult);
  }
}
