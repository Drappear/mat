package com.example.mat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeCategory;

public interface RecipeCategoryRepository extends JpaRepository<RecipeCategory, Long> {

}