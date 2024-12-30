package com.example.mat.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.recipe.RecipeCategoryDto;
import com.example.mat.dto.recipe.RecipeDto;
import com.example.mat.dto.recipe.RecipeIngredientDto;
import com.example.mat.dto.recipe.RecipeStepDto;
import com.example.mat.dto.shin.AuthMemberDto;
import com.example.mat.entity.recipe.RecipeCategory;
import com.example.mat.repository.RecipeCategoryRepository;
import com.example.mat.service.RecipeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@RequestMapping("/recipe")
@Controller
public class RecipeController {

  private final RecipeService recipeService;
  private final RecipeCategoryRepository recipeCategoryRepository;

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
  public String getCreate(Model model, RecipeDto recipeDto,
      @ModelAttribute("requestDto") PageRequestDto pageRequestDto) {
    log.info("recipe 작성 폼 요청");
    // 카테고리 리스트를 가져옵니다.
    List<RecipeCategoryDto> categories = recipeCategoryRepository.findAllOrderByRCateId()
        .stream()
        .map(category -> RecipeCategoryDto.builder()
            .rCateId(category.getRCateId())
            .name(category.getName())
            .build())
        .collect(Collectors.toList());

    // 모델에 추가
    model.addAttribute("categories", categories);
    return "recipe/create";
  }

  // @PreAuthorize("hasRole('MEMBER')")
  @PostMapping("/create")
  public String postCreate(@Valid @ModelAttribute("requestDto") RecipeDto recipeDto, BindingResult result, Model model,
      @RequestParam("category") Long categoryId,
      @RequestParam(value = "ingreName", required = false) List<String> ingreNames,
      @RequestParam(value = "ingreAmount", required = false) List<String> ingreAmounts,
      @RequestParam(value = "stepText", required = false) List<String> stepTexts,
      PageRequestDto pageRequestDto, RedirectAttributes rttr, @AuthenticationPrincipal UserDetails userDetails) {

    log.info("레시피 등록 {}", recipeDto);

    if (result.hasErrors()) {
      log.error("레시피 등록 폼 검증 실패: {}", result.getAllErrors());
      model.addAttribute("errors", result.getAllErrors());
      return "recipe/create";
    }

    try {
      // 현재 로그인한 사용자 정보 가져오기
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      AuthMemberDto authMember = (AuthMemberDto) auth.getPrincipal();
      recipeDto.setMid(authMember.getMemberDto().getMid());
      recipeDto.setUserid(authMember.getMemberDto().getUserid());

      // 카테고리 설정
      RecipeCategoryDto categoryDto = new RecipeCategoryDto();
      categoryDto.setRCateId(categoryId);
      recipeDto.setRecipeCategoryDto(categoryDto);

      // 재료 정보 설정
      if (ingreNames != null && ingreAmounts != null) {
        List<RecipeIngredientDto> ingredients = new ArrayList<>();
        for (int i = 0; i < ingreNames.size(); i++) {
          if (!ingreNames.get(i).isEmpty()) {
            ingredients.add(RecipeIngredientDto.builder()
                .name(ingreNames.get(i))
                .quantity(ingreAmounts.get(i))
                .build());
          }
        }
        recipeDto.setRecipeIngredientDtos(ingredients);
      }

      // 요리 단계 설정
      if (stepTexts != null) {
        List<RecipeStepDto> steps = new ArrayList<>();
        for (int i = 0; i < stepTexts.size(); i++) {
          if (!stepTexts.get(i).isEmpty()) {
            steps.add(RecipeStepDto.builder()
                .content(stepTexts.get(i))
                .build());
          }
        }
        recipeDto.setRecipeStepDtos(steps);
      }

      Long createdRecipeRno = recipeService.register(recipeDto);

      rttr.addFlashAttribute("message", "레시피가 성공적으로 등록되었습니다.");
      rttr.addAttribute("rno", createdRecipeRno);
      rttr.addAttribute("page", 1);
      rttr.addAttribute("size", pageRequestDto.getSize());
      rttr.addAttribute("type", pageRequestDto.getType());
      rttr.addAttribute("keyword", pageRequestDto.getKeyword());

      return "redirect:/recipe/read?rno=" + createdRecipeRno;
    } catch (Exception e) {
      log.error("레시피 등록 중 오류 발생", e);
      model.addAttribute("error", "레시피 등록 중 오류가 발생했습니다. 다시 시도해 주세요.");
      return "recipe/create";
    }
  }

  @GetMapping("/modify")
  public void getModify() { // MovieDto movieDto, @ModelAttribute("requestDto") PageRequestDto
                            // pageRequestDto
    log.info("recipe 수성 폼 요청");
  }

}
