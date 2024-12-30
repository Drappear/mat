package com.example.mat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mat.entity.recipe.RecipeIngredient;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
  // 특정 레시피와 관련된 재료 가져오기
  @Query("SELECT ri FROM RecipeIngredient ri WHERE ri.recipe.rno = :rno")
  List<RecipeIngredient> findByRecipeId(@Param("rno") Long recipeId);
}