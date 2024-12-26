package com.example.mat.entity.recipe;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

import com.example.mat.dto.recipe.RecipeImageDto;
import com.example.mat.dto.recipe.RecipeStepDto;
import com.example.mat.entity.BaseEntity;
import com.example.mat.entity.shin.Member;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = { "member", "recipeSteps", "recipeCategories", "recipeIngredients", "recipeImages" })
@Entity
public class Recipe extends BaseEntity {

    @Id
    @SequenceGenerator(name = "recipe_seq_gen", sequenceName = "recipe_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_seq_gen")
    private Long rno; // 레시피 번호

    @Column(nullable = false)
    private String title; // 레시피 제목

    @Column(nullable = false)
    private String content; // 레시피 내용

    @Column(nullable = false)
    private String serving;

    @Column(nullable = false)
    private String time;

    @Column(nullable = false)
    private String difficulty;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private int viewCount; // 조회수

    // TODO: mid -> Member 에서 가져오기
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mid", nullable = false) // 외래 키 명시
    private Member member;

    //TODO: 레시피 스텝
    // @ManyToOne(fetch = FetchType.LAZY)
    // private RecipeStep recipeStep;
    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeStep> recipeSteps = new ArrayList<>();

    //TODO: 레시피 카테고리
    @ManyToOne(fetch = FetchType.LAZY)
    private RecipeCategory recipeCategory;
    // @Builder.Default
    // @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    // private List<RecipeCategory> recipeCategories = new ArrayList<>();


    //TODO: 레시피 재료
    // @ManyToOne(fetch = FetchType.LAZY)
    // private RecipeIngredient recipeIngredient;
    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    //TODO: 레시피 최종 이미지
    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeImage> recipeImages = new ArrayList<>();
}
