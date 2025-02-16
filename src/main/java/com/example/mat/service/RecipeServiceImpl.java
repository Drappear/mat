package com.example.mat.service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.recipe.RecipeCategoryDto;
import com.example.mat.dto.recipe.RecipeDto;
import com.example.mat.dto.recipe.RecipeImageDto;
import com.example.mat.dto.recipe.RecipeStepDto;

import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeCategory;
import com.example.mat.entity.recipe.RecipeImage;
import com.example.mat.entity.recipe.RecipeIngredient;
import com.example.mat.entity.recipe.RecipeStep;
import com.example.mat.entity.shin.Member;
import com.example.mat.repository.MemberRepository;
import com.example.mat.repository.RecipeCategoryRepository;
import com.example.mat.repository.RecipeImageRepository;
import com.example.mat.repository.RecipeIngredientRepository;
import com.example.mat.repository.RecipeRepository;
import com.example.mat.repository.RecipeStepRepository;

import jakarta.transaction.Transactional;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class RecipeServiceImpl implements RecipeService {

  private final MemberRepository memberRepository;

  private final RecipeRepository recipeRepository;
  private final RecipeCategoryRepository recipeCategoryRepository;
  private final RecipeIngredientRepository recipeIngredientRepository;
  private final RecipeStepRepository recipeStepRepository;
  private final RecipeImageRepository recipeImageRepository;

  @Override
  @Transactional()
  public Page<RecipeDto> getRecipeList(String keyword, Long categoryId, String sortBy, Pageable pageable) {
    Page<Object[]> result = recipeRepository.findRecipeListWithFirstImage(keyword, categoryId, sortBy, pageable);

    return result.map(objects -> {
      Recipe recipe = (Recipe) objects[0];
      RecipeImage firstImage = objects.length > 1 ? (RecipeImage) objects[1] : null;

      return RecipeDto.builder()
          .rno(recipe.getRno())
          .title(recipe.getTitle())
          .content(recipe.getContent())
          .mid(recipe.getMember().getMid())
          .userid(recipe.getMember().getUserid())
          .nickname(recipe.getMember().getNickname())
          .viewCount(recipe.getViewCount())
          .regDate(recipe.getRegDate())
          .recipeCategoryDto(recipe.getRecipeCategory() != null ? RecipeCategoryDto.builder()
              .rCateId(recipe.getRecipeCategory().getRCateId())
              .name(recipe.getRecipeCategory().getName())
              .build() : null)
          .recipeImageDtos(firstImage != null ? List.of(RecipeImageDto.builder()
              .rInum(firstImage.getRInum())
              .uuid(firstImage.getUuid())
              .imgName(firstImage.getImgName())
              .path(firstImage.getPath())
              .build()) : new ArrayList<>())
          .build();
    });
  }

  @Override
  @Transactional()
  public Long getTotalRecipeCount() {
    return recipeRepository.getTotalRecipeCount();
  }

  @Override
  @Transactional()
  public PageResultDto<RecipeDto, Object[]> getList(PageRequestDto pageRequestDto) {
    Pageable pageable = pageRequestDto.getPageable(Sort.by("rno").descending());

    // // 검색 조건 처리
    // String keyword = pageRequestDto.getKeyword();
    // Long category = pageRequestDto.getCategory();

    // Page<Object[]> result = recipeRepository.searchByKeywordAndCategory(keyword,
    // category, pageable);

    // Function<Object[], RecipeDto> dtoFunction = (entity -> {
    // Recipe recipe = (Recipe) entity[0];
    // RecipeCategory categoryEntity = (RecipeCategory) entity[1];
    // List<RecipeImage> images = (List<RecipeImage>) entity[2];

    // return entityToDto(recipe, categoryEntity, images, null, null);
    // });

    // return new PageResultDto<>(result, dtoFunction);
    return null;
  }

  @Override
  @Transactional
  public Long register(RecipeDto recipeDto) {
    try {

      // 1. Member 설정
      Member member = memberRepository.findById(recipeDto.getMid())
          .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

      // 2. Recipe 저장
      Recipe recipe = Recipe.builder()
          .title(recipeDto.getTitle())
          .content(recipeDto.getContent())
          .serving(recipeDto.getServing())
          .time(recipeDto.getTime())
          .difficulty(recipeDto.getDifficulty())
          .viewCount(0)
          .member(member) // Member 세팅
          .build();

      System.out.println("service " + recipe);

      // 3. Category 설정
      if (recipeDto.getRecipeCategoryDto() != null) {
        RecipeCategory category = recipeCategoryRepository.findById(recipeDto.getRecipeCategoryDto().getRCateId())
            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 카테고리입니다."));
        recipe.setRecipeCategory(category);
      }
      Recipe savedRecipe = recipeRepository.save(recipe);

      // 4. RecipeIngredient 저장
      if (recipeDto.getRecipeIngredientDtos() != null) {
        List<RecipeIngredient> ingredients = recipeDto.getRecipeIngredientDtos().stream()
            .map(dto -> RecipeIngredient.builder()
                .name(dto.getName())
                .quantity(dto.getQuantity())
                .recipe(savedRecipe)
                .build())
            .collect(Collectors.toList());
        recipeIngredientRepository.saveAll(ingredients);
      }

      // 5. RecipeStep 저장
      if (recipeDto.getRecipeStepDtos() != null) {
        List<RecipeStep> steps = new ArrayList<>();
        for (int i = 0; i < recipeDto.getRecipeStepDtos().size(); i++) {
          RecipeStepDto stepDto = recipeDto.getRecipeStepDtos().get(i);
          RecipeStep step = RecipeStep.builder()
              .content(stepDto.getContent())
              .uuid(stepDto.getUuid())
              .imgName(stepDto.getImgName())
              .path(stepDto.getPath())
              .stepOrder(i + 1)
              .recipe(savedRecipe)
              .build();
          steps.add(step);
        }
        recipeStepRepository.saveAll(steps);
      }

      // 6. RecipeImage
      // RecipeImage 저장
      if (recipeDto.getRecipeImageDtos() != null && !recipeDto.getRecipeImageDtos().isEmpty()) {
        List<RecipeImage> recipeImages = recipeDto.getRecipeImageDtos().stream()
            .map(imageDto -> RecipeImage.builder()
                .uuid(imageDto.getUuid())
                .imgName(imageDto.getImgName())
                .path(imageDto.getPath())
                .recipe(savedRecipe)
                .build())
            .collect(Collectors.toList());
        recipeImageRepository.saveAll(recipeImages);
      }

      return savedRecipe.getRno();

    } catch (Exception e) {
      log.error("Recipe registration failed", e);
      throw new RuntimeException("Failed to register recipe", e);
    }
  }

  @Transactional
  @Override
  public Long modify(RecipeDto recipeDto) {

    Map<String, Object> entityMap = dtoToEntity(recipeDto);

    Recipe recipe = (Recipe) entityMap.get("recipe");
    List<RecipeImage> recipeImages = (List<RecipeImage>) entityMap.get("recipeImages");

    recipeRepository.save(recipe);

    // 기존의 recipe 이미지 제거
    recipeImageRepository.deleteByRecipe(recipe);
    // recipeImageRepository.saveAll(recipeImages);
    recipeImages.forEach(recipeImage -> recipeImageRepository.save(recipeImage));

    return recipe.getRno();
  }

  @Transactional
  @Override
  public void delete(Long rno) {
    Recipe recipe = Recipe.builder().rno(rno).build();

    recipeImageRepository.deleteByRecipe(recipe);
    // TODO: 자식 같이 지우기
    // reviewRepository.deleteByRecipe(recipe);
    recipeRepository.delete(recipe);
  }

  // TODO: 상세조회
  @Override
  public RecipeDto get(Long rno) {
    try {
      Recipe recipe = recipeRepository.findById(rno)
          .orElseThrow(() -> new IllegalArgumentException("Invalid recipe ID: " + rno));

      // 조회수 증가
      recipe.setViewCount(recipe.getViewCount() != null ? recipe.getViewCount() + 1 : 1);
      recipeRepository.save(recipe);

      // 관련 데이터 조회
      List<RecipeImage> images = recipe.getRecipeImages() != null ? recipe.getRecipeImages() : new ArrayList<>();
      List<RecipeStep> steps = recipe.getRecipeSteps() != null ? recipe.getRecipeSteps() : new ArrayList<>();
      List<RecipeIngredient> ingredients = recipe.getRecipeIngredients() != null ? recipe.getRecipeIngredients()
          : new ArrayList<>();

      return entityToDto(recipe, recipe.getRecipeCategory(), images, steps, ingredients);
    } catch (Exception e) {
      log.error("레시피 조회 중 오류: ", e);
      throw new RuntimeException("레시피 조회 실패", e);
    }
  }

  // TODO: 조회수 증가
  @Override
  public RecipeDto incrementViewCount(Long rno) {
    // 레시피 조회 (없으면 예외 처리)
    Optional<Recipe> recipeOptional = this.recipeRepository.findById(rno);

    // 레시피가 존재하는 경우
    if (recipeOptional.isPresent()) {
      Recipe recipe = recipeOptional.get();
      recipe.setViewCount(recipe.getViewCount() + 1); // 조회수 증가
      this.recipeRepository.save(recipe); // 변경사항 저장

      // Recipe 객체를 RecipeDto로 변환하여 반환
      return this.entityToDto(recipe);
    } else {
      // 레시피가 존재하지 않으면 예외를 던짐
      throw new Error("Recipe not found");
    }
  }

  @Override
  public List<RecipeCategoryDto> getAllCategories() {
    // 카테고리를 rCateId 기준으로 정렬하여 가져오기
    List<RecipeCategory> categories = recipeCategoryRepository.findAllOrderByRCateId();

    // RecipeCategory -> RecipeCategoryDto로 변환
    return categories.stream().map(category -> RecipeCategoryDto.builder()
        .rCateId(category.getRCateId())
        .name(category.getName())
        .build()).collect(Collectors.toList());
  }

  // 엔티티 -> DTO 변환 메서드
  private RecipeDto entityToDto(Recipe recipe) {
    return RecipeDto.builder()
        .rno(recipe.getRno())
        .title(recipe.getTitle())
        .content(recipe.getContent())
        .serving(recipe.getServing())
        .time(recipe.getTime())
        .difficulty(recipe.getDifficulty())
        .viewCount(recipe.getViewCount()) // 증가된 조회수를 포함
        .mid(recipe.getMember() != null ? recipe.getMember().getMid() : null) // Member의 mid 가져옴
        .userid(recipe.getMember() != null ? recipe.getMember().getUserid() : null) // Member의 userid을 가져옴
        // .nickname(recipe.getMember() != null ? recipe.getMember().getNickname() :
        // null) // Member 의 nickname
        .regDate(recipe.getRegDate())
        .updateDate(recipe.getUpdateDate())
        .build();
  }

}