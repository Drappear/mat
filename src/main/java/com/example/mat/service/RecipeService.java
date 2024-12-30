package com.example.mat.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.recipe.RecipeCategoryDto;
import com.example.mat.dto.recipe.RecipeDto;
import com.example.mat.dto.recipe.RecipeImageDto;
import com.example.mat.dto.recipe.RecipeIngredientDto;
import com.example.mat.dto.recipe.RecipeStepDto;
import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeCategory;
import com.example.mat.entity.recipe.RecipeImage;
import com.example.mat.entity.recipe.RecipeIngredient;
import com.example.mat.entity.recipe.RecipeStep;

public interface RecipeService {

  // 기존 메서드는 유지하고 새로운 메서드 추가
  Page<RecipeDto> getRecipeList(String keyword, Long categoryId, String sortBy, Pageable pageable);

  Long getTotalRecipeCount();

  // 목록(페이지 나누기 + 검색)
  PageResultDto<RecipeDto, Object[]> getList(PageRequestDto pageRequestDto);

  // 등록
  Long register(RecipeDto recipeDto);

  // 수정
  Long modify(RecipeDto recipeDto);

  // 삭제
  void delete(Long rno);

  // 상세조회
  RecipeDto get(Long rno);

  // 조회수
  RecipeDto incrementViewCount(Long rno);

  // 모든 카테고리를 가져오기
  List<RecipeCategoryDto> getAllCategories();

  // entityToDto 최적화
  default RecipeDto entityToDto(Recipe recipe, RecipeCategory recipeCategory, List<RecipeImage> recipeImages,
      List<RecipeStep> recipeSteps, List<RecipeIngredient> recipeIngredients) {
    RecipeDto recipeDto = RecipeDto.builder()
        .rno(recipe.getRno())
        .title(recipe.getTitle())
        .content(recipe.getContent())
        .serving(recipe.getServing())
        .time(recipe.getTime())
        .difficulty(recipe.getDifficulty())
        .viewCount(recipe.getViewCount())
        .regDate(recipe.getRegDate())
        .updateDate(recipe.getUpdateDate())
        .build();

    // Category 변환
    Optional.ofNullable(recipeCategory).ifPresent(category -> {
      recipeDto.setRecipeCategoryDto(
          RecipeCategoryDto.builder()
              .rCateId(category.getRCateId())
              .name(category.getName())
              .build());
    });

    // 재료 변환
    recipeDto.setRecipeIngredientDtos(
        recipeIngredients.stream()
            .map(ingredient -> RecipeIngredientDto.builder()
                .ingId(ingredient.getIngId())
                .name(ingredient.getName())
                .quantity(ingredient.getQuantity())
                .build())
            .collect(Collectors.toList()));

    // Step 변환
    recipeDto.setRecipeStepDtos(
        recipeSteps.stream()
            .map(step -> RecipeStepDto.builder()
                .stepNum(step.getStepNum())
                .content(step.getContent())
                .uuid(step.getUuid())
                .imgName(step.getImgName())
                .path(step.getPath())
                .build())
            .collect(Collectors.toList()));

    // 이미지 변환
    recipeDto.setRecipeImageDtos(
        recipeImages.stream()
            .map(image -> RecipeImageDto.builder()
                .rInum(image.getRInum())
                .uuid(image.getUuid())
                .imgName(image.getImgName())
                .path(image.getPath())
                .build())
            .collect(Collectors.toList()));

    return recipeDto;
  }

  // dtoToEntity 최적화
  default Map<String, Object> dtoToEntity(RecipeDto recipeDto) {
    Map<String, Object> resultMap = new HashMap<>();

    // Recipe 생성
    Recipe recipe = Recipe.builder()
        .rno(recipeDto.getRno())
        .title(recipeDto.getTitle())
        .content(recipeDto.getContent())
        .serving(recipeDto.getServing())
        .time(recipeDto.getTime())
        .difficulty(recipeDto.getDifficulty())
        .viewCount(0) // 초기 조회수
        .build();
    resultMap.put("recipe", recipe);

    // RecipeCategory 변환
    Optional.ofNullable(recipeDto.getRecipeCategoryDto()).ifPresent(categoryDto -> {
      recipe.setRecipeCategory(
          RecipeCategory.builder()
              .rCateId(categoryDto.getRCateId())
              .name(categoryDto.getName())
              .build());
    });

    // RecipeImage 변환
    resultMap.put("recipeImages",
        recipeDto.getRecipeImageDtos().stream()
            .map(dto -> RecipeImage.builder()
                .uuid(dto.getUuid())
                .imgName(dto.getImgName())
                .path(dto.getPath())
                .recipe(recipe)
                .build())
            .collect(Collectors.toList()));

    // RecipeStep 변환
    resultMap.put("recipeSteps",
        recipeDto.getRecipeStepDtos().stream()
            .map(dto -> RecipeStep.builder()
                .stepNum(dto.getStepNum())
                .content(dto.getContent())
                .uuid(dto.getUuid())
                .imgName(dto.getImgName())
                .path(dto.getPath())
                .recipe(recipe)
                .build())
            .collect(Collectors.toList()));

    // RecipeIngredient 변환
    resultMap.put("recipeIngredients",
        recipeDto.getRecipeIngredientDtos().stream()
            .map(dto -> RecipeIngredient.builder()
                .ingId(dto.getIngId())
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .recipe(recipe)
                .build())
            .collect(Collectors.toList()));

    return resultMap;
  }
}