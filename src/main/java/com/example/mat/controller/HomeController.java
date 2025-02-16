package com.example.mat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
public class HomeController {

  @GetMapping("/main")
  public void getMain() {
    log.info("main 요청");
  }
}
