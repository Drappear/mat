package com.example.mat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.recipe.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

  @Query("SELECT r, r.viewCnt FROM Recipe r")
  // @Query("SELECT m, avg(r.grade), count(distinct r) FROM Movie m LEFT JOIN
  // Review r ON r.movie = m GROUP BY m")
  Page<Object[]> getListPage(Pageable pageable);
}