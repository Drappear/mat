package com.example.mat.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeCategory;
import com.example.mat.entity.recipe.RecipeIngredient;
import com.example.mat.entity.recipe.RecipeStep;

public interface RecipeStepRepository extends JpaRepository<RecipeStep, Long> {

  // 특정 레시피의 단계 가져오기
  @Query("SELECT rs FROM RecipeStep rs WHERE rs.recipe.rno = :rno ORDER BY rs.stepNum ASC")
  List<RecipeStep> findByRecipeIdOrderByStepNum(@Param("rno") Long recipeId);
}