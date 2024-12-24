package com.example.mat.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import com.example.mat.dto.PageRequestDto;
import com.example.mat.dto.PageResultDto;
import com.example.mat.dto.recipe.RecipeDto;
import com.example.mat.dto.recipe.RecipeImageDto;
import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeImage;

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


  //TODO: recipe => RecipeDto
  default RecipeDto entityToDto(Recipe recipe, List<RecipeImage> recipeImages, Double en) {
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

    // RecipeImage => RecipeImageDto 변경 후 리스트 작업
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

    Recipe recipe = Recipe.builder()
        .rno(recipeDto.getRno())
        .title(recipeDto.getTitle())
        .build();
    resultMap.put("recipe", recipe);

    List<RecipeImageDto> recipeImageDtos = recipeDto.getRecipeImageDtos();
    // MovieImageDto => MovieImage 변경 후 MovieImage List 형태로 작성
    // List<MovieImage> movieImages = new ArrayList<>();
    // if (movieImageDtos != null && movieImageDtos.size() > 0) {
    // movieImageDtos.forEach(dto -> {
    // MovieImage movieImage = MovieImage.builder()
    // .uuid(dto.getUuid())
    // .imgName(dto.getImgName())
    // .path(dto.getPath())
    // .movie(movie)
    // .build();
    // movieImages.add(movieImage);
    // });
    // }

    if (recipeImageDtos != null && recipeImageDtos.size() > 0) {
      List<RecipeImage> recipeImages = recipeImageDtos.stream().map(dto -> {
        RecipeImage recipeImage = RecipeImage.builder()
            .uuid(dto.getUuid())
            .imgName(dto.getImgName())
            .path(dto.getPath())
            .recipe(recipe)
            .build();
        return recipeImage;
      }).collect(Collectors.toList());

      resultMap.put("recipeImages", recipeImages);
    }
    return resultMap;
  }

}