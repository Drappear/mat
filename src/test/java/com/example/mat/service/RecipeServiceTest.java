package com.example.mat.service;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import com.example.mat.dto.recipe.RecipeDto;
import com.example.mat.entity.recipe.Recipe;
import com.example.mat.entity.recipe.RecipeCategory;
import com.example.mat.repository.RecipeCategoryRepository;
import com.example.mat.repository.RecipeImageRepository;
import com.example.mat.repository.RecipeIngredientRepository;
import com.example.mat.repository.RecipeStepRepository;

import jakarta.transaction.Transactional;

@SpringBootTest // Spring Boot의 전체 컨텍스트를 로드
public class RecipeServiceTest {

    @Autowired
    private RecipeService recipeService;

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

        // recipeImageDtos=[RecipeImageDto(rInum=null,
        // uuid=9a615b59-1ea4-4785-8677-d64f4c165e16, imgName=oppen1.jpg,
        // path=2025/02/14, rno=null, regDate=null, updateDate=null)],
        // recipeStepDtos=[RecipeStepDto(stepNum=1, content=asd, uuid=null,
        // imgName=null, path=null, regDate=null, updateDate=null),
        // RecipeStepDto(stepNum=2, content=asdasd, uuid=null, imgName=null, path=null,
        // regDate=null, updateDate=null), RecipeStepDto(stepNum=3, content=adsd,
        // uuid=null, imgName=null, path=null, regDate=null, updateDate=null)],
        // recipeCategoryDto=null, recipeIngredientDtos=[RecipeIngredientDto(ingId=null,
        // rno=null, name=양배추, quantity=1), RecipeIngredientDto(ingId=null, rno=null,
        // name=목이버섯, quantity=1), RecipeIngredientDto(ingId=null, rno=null, name=대파,
        // quantity=1)], regDate=null, updateDate=null

        RecipeDto recipeDto = RecipeDto.builder()
                .title("sadasd")
                .content("dasdsa")
                .serving("1")
                .time("10")
                .difficulty("1")
                .mid(22L)
                .viewCount(0)
                .build();
        System.out.println(recipeService.register(recipeDto));
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
                // .name("퓨전")
                // .name("김치/젓갈/장류")
                .name("양념/소스/잼")
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
