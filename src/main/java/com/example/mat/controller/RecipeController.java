package com.example.mat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.mat.entity.recipe.RecipeCategory;
import com.example.mat.repository.RecipeCategoryRepository;

import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@RequestMapping("/recipe")
@Controller
public class RecipeController {

  @Autowired
  RecipeCategoryRepository recipeCategoryRepository;

  @GetMapping("/list")
  public String getRecipeList(Model model) {
    log.info("get recipe list 페이지 요청");

    // RecipeCategory 데이터를 rCateId 기준으로 정렬하여 가져오기
        List<RecipeCategory> categoryList = recipeCategoryRepository.findAllOrderByRCateId();
        model.addAttribute("categoryList", categoryList);

        return "recipe/list"; // Thymeleaf의 list.html로 이동
  }

  @GetMapping("/read")
  public void getRead() { // MovieDto movieDto, @ModelAttribute("requestDto") PageRequestDto
                          // pageRequestDto
    log.info("recipe 상세 페이지 요청");
  }

  @GetMapping("/create")
  public void getCreate() { // MovieDto movieDto, @ModelAttribute("requestDto") PageRequestDto
                            // pageRequestDto
    log.info("recipe 작성 폼 요청");
  }

  @GetMapping("/modify")
  public void getModify() { // MovieDto movieDto, @ModelAttribute("requestDto") PageRequestDto
                            // pageRequestDto
    log.info("recipe 수성 폼 요청");
  }

}
