package com.example.mat.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.recipe.RecipeDto;
import com.example.mat.entity.recipe.RecipeCategory;
import com.example.mat.repository.RecipeCategoryRepository;
import com.example.mat.service.RecipeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@Log4j2
@RequestMapping("/recipe")
@Controller
public class RecipeController {

  private final RecipeService recipeService;

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
  public void getCreate(RecipeDto recipeDto, @ModelAttribute("requestDto") PageRequestDto pageRequestDto) { // MovieDto movieDto, @ModelAttribute("requestDto") PageRequestDto
                            // pageRequestDto
    log.info("recipe 작성 폼 요청");
  }
  @PostMapping("/create")
  public String postCreate(@Valid RecipeDto recipeDto, BindingResult result, @ModelAttribute("requestDto") PageRequestDto pageRequestDto, RedirectAttributes rttr) {
    log.info("레시피 등록 {}", recipeDto);
      
    if (result.hasErrors()) {
      return "/recipe/create";
    }
      // 서비스
    Long rno = recipeService.register(recipeDto);

    rttr.addAttribute("rno", rno);
    rttr.addAttribute("page", 1);
    rttr.addAttribute("size", pageRequestDto.getSize());
    rttr.addAttribute("type", pageRequestDto.getType());
    rttr.addAttribute("keyword", pageRequestDto.getKeyword());
    return "redirect:/recipe/read";
  }
  

  @GetMapping("/modify")
  public void getModify() { // MovieDto movieDto, @ModelAttribute("requestDto") PageRequestDto
                            // pageRequestDto
    log.info("recipe 수성 폼 요청");
  }

}
