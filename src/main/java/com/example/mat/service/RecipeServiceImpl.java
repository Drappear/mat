package com.example.mat.service;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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
    // 로그인된 사용자 정보 가져오기
    Member member = memberRepository.findById(recipeDto.getMid())
        .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));

    Map<String, Object> entityMap = dtoToEntity(recipeDto);

    Recipe recipe = (Recipe) entityMap.get("recipe");
    recipe.setMember(member); // 로그인된 사용자 매핑

    // RecipeCategory 저장 또는 검색
    RecipeCategory recipeCategory = recipe.getRecipeCategory();
    if (recipeCategory != null && recipeCategory.getRCateId() != null) {
      Long categoryId = recipeCategory.getRCateId();
      RecipeCategory existingCategory = recipeCategoryRepository.findById(categoryId)
          .orElseGet(() -> recipeCategoryRepository.save(recipeCategory));
      recipe.setRecipeCategory(existingCategory);
    } else {
      log.warn("RecipeCategory is null or RCateId is null. Skipping category save.");
    }

    // Recipe 저장
    recipeRepository.save(recipe);

    // RecipeImage 저장
    List<RecipeImage> recipeImages = (List<RecipeImage>) entityMap.get("recipeImages");
    if (recipeImages != null) {
      recipeImages.forEach(image -> image.setRecipe(recipe)); // Recipe와 연결
      recipeImageRepository.saveAll(recipeImages);
    }

    // RecipeStep 저장
    List<RecipeStep> recipeSteps = (List<RecipeStep>) entityMap.get("recipeSteps");
    if (recipeSteps != null) {
      recipeSteps.forEach(step -> step.setRecipe(recipe)); // Recipe와 연결
      recipeStepRepository.saveAll(recipeSteps);
    }

    // RecipeIngredient 저장
    List<RecipeIngredient> recipeIngredients = (List<RecipeIngredient>) entityMap.get("recipeIngredients");
    if (recipeIngredients != null) {
      recipeIngredients.forEach(ingredient -> ingredient.setRecipe(recipe)); // Recipe와 연결
      recipeIngredientRepository.saveAll(recipeIngredients);
    }

    log.info("!!!!!!!!!Recipe registered successfully: {}", recipe.getRno());
    return recipe.getRno();
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
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'get'");
  }

  // TODO: 조회수 증가
  @Override
  public RecipeDto incrementViewCount(Long rno) {
    return null;
    // // 레시피 조회 (없으면 예외 처리)
    // Optional<Recipe> recipeOptional = this.recipeRepository.findById(rno);

    // // 레시피가 존재하는 경우
    // if (recipeOptional.isPresent()) {
    // Recipe recipe = recipeOptional.get();
    // recipe.setViewCount(recipe.getViewCount() + 1); // 조회수 증가
    // this.recipeRepository.save(recipe); // 변경사항 저장

    // // Recipe 객체를 RecipeDto로 변환하여 반환
    // return this.convertToDto(recipe);
    // } else {
    // // 레시피가 존재하지 않으면 예외를 던짐
    // throw new NotFoundException("Recipe not found");
    // }
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
        // .nickname(recipe.getMember() != null ? recipe.getMember().getNickname() : null) // Member 의 nickname
        .regDate(recipe.getRegDate())
        .updateDate(recipe.getUpdateDate())
        .build();
  }

  // @Override
  // public RecipeDto get(Long rno) {
  // List<Object[]> result = recipeImageRepository.getRecipeRow(rno);

  // Recipe recipe = (Recipe) result.get(0)[0];
  // Long reviewCnt = (Long) result.get(0)[2];
  // Double avg = (Double) result.get(0)[3];

  // // 1 : 영화이미지
  // List<RecipeImage> recipeImages = new ArrayList<>();
  // result.forEach(row -> {
  // RecipeImage recipeImage = (RecipeImage) row[1];
  // recipeImage.add(recipeImage);
  // });

  // return entityToDto(recipe, recipeImages); // reviewCnt, avg
  // }

}