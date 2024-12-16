package com.example.mat.repository;

import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeCategory;
import com.example.mat.entity.won.Board;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.stream.LongStream;

@SpringBootTest // Spring Boot의 전체 컨텍스트를 로드
public class RecipeRepositoryTest {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private RecipeCategoryRepository recipeCategoryRepository;

    @Autowired
    private RecipeImageRepository recipeImageRepository;

    @Autowired
    private RecipeIngredientRepository recipeIngredientRepository;

    @Autowired
    private RecipeStepRepository recipeStepRepository;


    //TODO: 레시피 카테고리 추가
    // @Commit
    // @Transactional
    // @Test
    // public void testCategoryAdd(){
    //     RecipeCategory recipeCategory = RecipeCategory.builder()
    //     .name("전체")
    //     .build();
    //     recipeCategoryRepository.save(recipeCategory);
    // }

}
