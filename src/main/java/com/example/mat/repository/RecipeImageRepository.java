package com.example.mat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeImage;
import com.example.mat.repository.RecipeTotal.RecipeImageReviewRepository;

public interface RecipeImageRepository extends JpaRepository<RecipeImage, Long>, RecipeImageReviewRepository {

  // recipe_rno 를 이용해 recipe_image 제거 메서드 생성
  @Modifying
  @Query("DELETE FROM RecipeImage ri WHERE ri.recipe = :recipe")
  void deleteByRecipe(Recipe recipe);

  // 어제 날짜의 recipe 이미지 리스트 가져오기
  // 추출 후 스케쥴러에서 사용할 예정(Entity 사용안함)
  @Query(value = "SELECT * FROM RECIPE_IMAGE ri WHERE ri.PATH = to_char(sysdate-1,'yyyy/mm/dd')", nativeQuery = true)
  List<RecipeImage> findOldFileAll();

//   @Query(value = "SELECT * FROM RECIPE_IMAGE ri WHERE ri.PATH = :date", nativeQuery = true)
//   List<RecipeImage> findOldFileAll(@Param("date") String date);
}