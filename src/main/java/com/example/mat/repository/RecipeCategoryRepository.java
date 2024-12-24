package com.example.mat.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeCategory;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {
  //TODO: 카테고리 정렬하여 가져오기
  // JPQL 쿼리 사용: RecipeCategory를 rCateId 기준으로 오름차순 정렬
  @Query("SELECT rc FROM RecipeCategory rc ORDER BY rc.rCateId ASC")
  List<RecipeCategory> findAllOrderByRCateId();
}