package com.example.mat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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


  //TODO: recipe => RecipeDto
  default RecipeDto entityToDto(Recipe recipe, RecipeCategory recipeCategory, List<RecipeImage> recipeImages, List<RecipeStep> recipeSteps, List<RecipeIngredient> recipeIngredients ) { // Double en
    RecipeDto recipeDto = RecipeDto.builder()
        .rno(recipe.getRno())
        .title(recipe.getTitle())
        .content(recipe.getContent())
        .serving(recipe.getServing())
        .time(recipe.getTime())
        .difficulty(recipe.getDifficulty())
        .viewCount(recipe.getViewCount())

        // .nickname(recipe.getMember().getNickname())
        .regDate(recipe.getRegDate())
        .updateDate(recipe.getUpdateDate())
        .build();

    //TODO: 선택한 category 값 가져오기
    if (recipe.getRecipeCategory() != null) {
      RecipeCategoryDto categoryDto = RecipeCategoryDto.builder()
          .rCateId(recipe.getRecipeCategory().getRCateId())
          .name(recipe.getRecipeCategory().getName())
          .build();
      recipeDto.setRecipeCategoryDto(categoryDto);
  }


    //TODO: 재료 dto 변경 후 리스트 작업
    List<RecipeIngredientDto> recipeIngredientDtos = recipeIngredients.stream().map(recipeIngredient ->{
      return RecipeIngredientDto.builder()
      .ingId(recipeIngredient.getIngId())
      .name(recipeIngredient.getName())
      .quantity(recipeIngredient.getQuantity())
      .build();
    }).collect(Collectors.toList());
    recipeDto.setRecipeIngredientDtos(recipeIngredientDtos);
    // return recipeDto;

    //TODO: Step => StepDto 변경 후 리스트 작업
    List<RecipeStepDto> recipeStepDtos = recipeSteps.stream().map(recipeStep -> {
      return RecipeStepDto.builder()
      .stepNum(recipeStep.getStepNum())
      .content(recipeStep.getContent())
      .uuid(recipeStep.getUuid())
      .imgName(recipeStep.getImgName())
      .path(recipeStep.getPath())
      .build();
    }).collect(Collectors.toList());
    recipeDto.setRecipeStepDtos(recipeStepDtos);
    // return recipeDto;

    //TODO: RecipeImage => RecipeImageDto 변경 후 리스트 작업
    List<RecipeImageDto> recipeImageDtos = recipeImages.stream().map(recipeImage -> {
      return RecipeImageDto.builder()
          .rInum(recipeImage.getRInum())
          .uuid(recipeImage.getUuid())
          .imgName(recipeImage.getImgName())
          .path(recipeImage.getPath())
          .build();
    }).collect(Collectors.toList());
    recipeDto.setRecipeImageDtos(recipeImageDtos);

    return recipeDto;
  }

  
  //TODO: dto => entity
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
        .viewCount(0) // 초기 조회수 설정
        .build();
    resultMap.put("recipe", recipe);

    // RecipeCategory 변환
    if (recipeDto.getRecipeCategoryDto() != null) {
      RecipeCategory category = RecipeCategory.builder()
          .rCateId(recipeDto.getRecipeCategoryDto().getRCateId())
          .name(recipeDto.getRecipeCategoryDto().getName())
          .build();
      recipe.setRecipeCategory(category);
  }

    // RecipeImage 변환
    List<RecipeImageDto> recipeImageDtos = recipeDto.getRecipeImageDtos();
    if (recipeImageDtos != null && !recipeImageDtos.isEmpty()) {
        List<RecipeImage> recipeImages = recipeImageDtos.stream().map(dto -> 
            RecipeImage.builder()
                .uuid(dto.getUuid())
                .imgName(dto.getImgName())
                .path(dto.getPath())
                .recipe(recipe)
                .build()
        ).collect(Collectors.toList());
        resultMap.put("recipeImages", recipeImages);
    }

    // RecipeStep 변환
    List<RecipeStepDto> recipeStepDtos = recipeDto.getRecipeStepDtos();
    if (recipeStepDtos != null && !recipeStepDtos.isEmpty()) {
        List<RecipeStep> recipeSteps = recipeStepDtos.stream().map(dto -> 
            RecipeStep.builder()
                .stepNum(dto.getStepNum())
                .content(dto.getContent())
                .uuid(dto.getUuid())
                .imgName(dto.getImgName())
                .path(dto.getPath())
                .recipe(recipe)
                .build()
        ).collect(Collectors.toList());
        resultMap.put("recipeSteps", recipeSteps);
    }

    // RecipeIngredient 변환
    List<RecipeIngredientDto> recipeIngredientDtos = recipeDto.getRecipeIngredientDtos();
    if (recipeIngredientDtos != null && !recipeIngredientDtos.isEmpty()) {
        List<RecipeIngredient> recipeIngredients = recipeIngredientDtos.stream().map(dto -> 
            RecipeIngredient.builder()
                .ingId(dto.getIngId())
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .recipe(recipe)
                .build()
        ).collect(Collectors.toList());
        resultMap.put("recipeIngredients", recipeIngredients);
    }

    return resultMap;
  }

}