package com.example.mat.repository;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeCategory;

import jakarta.transaction.Transactional;

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

    // TODO: 레시피 추가
    @Commit
    @Transactional
    @Test
    public void testRecipeAdd() {
        Recipe recipe = Recipe.builder()
                .content("")
                .serving("")
                .time("")
                .difficulty("")
                .build();
        recipeRepository.save(recipe);
    }

    // TODO: 레시피 카테고리 추가
    @Commit
    @Transactional
    @Test
    public void testCategoryAdd() {
        RecipeCategory recipeCategory = RecipeCategory.builder()
                // .name("반찬")
                // .name("국/탕/찌개")
                // .name("디저트")
                // .name("면/만두")
                // .name("밥/죽/떡")
                .name("퓨전")
                // .name("김치/젓갈/장류")
                // .name("양념/소스/잼")
                // .name("양식")
                // .name("차/음료/술")
                .build();
        recipeCategoryRepository.save(recipeCategory);
    }

    // TODO: 레시피 재료 추가

    // 레시피 전체 리스트 가져오기
    @Test
    public void testListPage() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("rno").descending());

        Page<Object[]> result = recipeRepository.getListPage(pageRequest);

        for (Object[] objects : result) {
            System.out.println(Arrays.toString(objects));
        }
    }

}
