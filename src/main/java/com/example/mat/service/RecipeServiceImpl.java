package com.example.mat.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.mat.repository.RecipeCategoryRepository;
import com.example.mat.repository.RecipeImageRepository;
import com.example.mat.repository.RecipeIngredientRepository;
import com.example.mat.repository.RecipeRepository;
import com.example.mat.repository.RecipeStepRepository;
import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.recipe.RecipeDto;
import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeImage;


import jakarta.transaction.Transactional;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Log4j2
@Service
public class RecipeServiceImpl implements RecipeService {

  private final RecipeImageRepository recipeImageRepository;
  private final RecipeRepository recipeRepository;
  private final RecipeIngredientRepository recipeIngredientRepository;
  private final RecipeStepRepository recipeStepRepository;
  private final RecipeCategoryRepository recipeCategoryRepository;

  // @Override
  // public PageResultDto<RecipeDto, Object[]> getList(PageRequestDto pageRequestDto) {

  //   Pageable pageable = pageRequestDto.getPageable(Sort.by("rno").descending());

  //   Page<Object[]> result = recipeImageRepository.getTotalList(pageRequestDto.getType(), pageRequestDto.getKeyword(),
  //       pageable);

  //   Function<Object[], RecipeDto> function = (en -> entityToDto((Recipe) en[0],
  //       (List<RecipeImage>) Arrays.asList((RecipeImage) en[1]),
  //       (Long) en[2], (Double) en[3]));

  //   return new PageResultDto<>(result, function);
  // }

  @Override
  public Long register(RecipeDto recipeDto) {

    Map<String, Object> entityMap = dtoToEntity(recipeDto);

    Recipe recipe = (Recipe) entityMap.get("recipe");
    List<RecipeImage> recipeImages = (List<RecipeImage>) entityMap.get("recipeImages");

    recipeRepository.save(recipe);
    recipeImages.forEach(recipeImage -> recipeImageRepository.save(recipeImage));

    return recipe.getRno();
  }

  @Transactional
  @Override
  public Long modify(RecipeDto recipeDto) {

    Map<String, Object> entityMap = dtoToEntity(recipeDto);

    Recipe recipe = (Recipe) entityMap.get("recipe");
    List<RecipeImage> recipeImages = (List<RecipeImage>) entityMap.get("recipeImages");

    recipeRepository.save(recipe);

    // 기존의 영화 이미지 제거
    recipeImageRepository.deleteByRecipe(recipe);

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

  @Override
  public PageResultDto<RecipeDto, Object[]> getList(PageRequestDto pageRequestDto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getList'");
  }

  @Override
  public RecipeDto get(Long rno) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'get'");
  }

  //TODO: 조회수 증가
  @Override
  public RecipeDto incrementViewCount(Long rno)  {
    return null;
    // // 레시피 조회 (없으면 예외 처리)
    // Optional<Recipe> recipeOptional = this.recipeRepository.findById(rno);

    // // 레시피가 존재하는 경우
    // if (recipeOptional.isPresent()) {
    //     Recipe recipe = recipeOptional.get();
    //     recipe.setViewCount(recipe.getViewCount() + 1);  // 조회수 증가
    //     this.recipeRepository.save(recipe);  // 변경사항 저장

    //     // Recipe 객체를 RecipeDto로 변환하여 반환
    //     return this.convertToDto(recipe);
    // } else {
    //     // 레시피가 존재하지 않으면 예외를 던짐
    //     throw new NotFoundException("Recipe not found");
    // }
}

// 엔티티 -> DTO 변환 메서드
private RecipeDto convertToDto(Recipe recipe) {
    return RecipeDto.builder()
            .rno(recipe.getRno())
            .title(recipe.getTitle())
            .content(recipe.getContent())
            .serving(recipe.getServing())
            .time(recipe.getTime())
            .difficulty(recipe.getDifficulty())
            .viewCount(recipe.getViewCount())  // 증가된 조회수를 포함
            .mid(recipe.getMember().getMid()) // Member의 mid를 가져옴
            .nickname(recipe.getMember().getNickname()) // Member의 nickname을 가져옴
            .regDate(recipe.getRegDate())
            .updateDate(recipe.getUpdateDate())
            .build();
}

  // @Override
  // public RecipeDto get(Long rno) {
  //   List<Object[]> result = recipeImageRepository.getRecipeRow(rno);

  //   Recipe recipe = (Recipe) result.get(0)[0];
  //   Long reviewCnt = (Long) result.get(0)[2];
  //   Double avg = (Double) result.get(0)[3];

  //   // 1 : 영화이미지
  //   List<RecipeImage> recipeImages = new ArrayList<>();
  //   result.forEach(row -> {
  //     RecipeImage recipeImage = (RecipeImage) row[1];
  //     recipeImage.add(recipeImage);
  //   });

  //   return entityToDto(recipe, recipeImages); // reviewCnt, avg
  // }

}