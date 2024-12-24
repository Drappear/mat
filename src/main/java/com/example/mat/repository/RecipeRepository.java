package com.example.mat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.mat.entity.recipe.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

  @Query("SELECT r, r.viewCount FROM Recipe r")
  // @Query("SELECT m, avg(r.grade), count(distinct r) FROM Movie m LEFT JOIN
  // Review r ON r.movie = m GROUP BY m")
  Page<Object[]> getListPage(Pageable pageable);

  //TODO: 레시피 리스트와 조회수를 함께 페이징 처리하는 쿼리가 포함
  @Query("SELECT r FROM Recipe r WHERE r.title LIKE %:keyword% AND r.difficulty = :difficulty")
  Page<Recipe> searchRecipes(@Param("keyword") String keyword, @Param("difficulty") String difficulty, Pageable pageable);
}