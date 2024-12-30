package com.example.mat.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mat.entity.recipe.RecipeStep;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

  // 특정 레시피의 모든 단계 내용 가져오기
  @Query("SELECT rs FROM RecipeStep rs WHERE rs.recipe.rno = :rno ORDER BY rs.stepNum ASC")
  List<RecipeStep> findByRecipeIdOrderByStepNum(@Param("rno") Long recipeId);
}