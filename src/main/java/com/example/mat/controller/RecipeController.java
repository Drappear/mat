package com.example.mat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@RequestMapping("/recipe")
@Controller
public class RecipeController {

  @GetMapping("/list")
  public void getRecipeList() {
    log.info("get recipe list 페이지 요청");
  }

  @GetMapping("/create")
  public void getCreate() { //MovieDto movieDto, @ModelAttribute("requestDto") PageRequestDto pageRequestDto
    log.info("recipe 작성 폼 요청");
  }

}
