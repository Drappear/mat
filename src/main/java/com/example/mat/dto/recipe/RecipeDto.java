package com.example.mat.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecipeDto {

    private Long rno; // 레시피 번호

    @NotBlank(message = "제목은 필수 항목입니다.")
    @Size(max = 100, message = "제목은 최대 100자까지 입력 가능합니다.")
    private String title; // 레시피 제목

    @NotBlank(message = "내용은 필수 항목입니다.")
    private String content; // 레시피 내용

    @NotBlank(message = "인분 선택은 필수 항목입니다.")
    private String serving;

    @NotBlank(message = "소요시간 선택은 필수 항목입니다.")
    private String time;

    @NotBlank(message = "난이도 선택은 필수 항목입니다.")
    private String difficulty;

    private int viewCount; // 조회수

    // TODO: mid -> Member 에서 가져오기
    private Long mid;
    private String nickname;

    // TODO: 레시피에 소속된 이미지 가져오기
    @Builder.Default
    private List<RecipeImageDto> recipeImageDtos = new ArrayList<>();

    // TODO: 레시피 스텝
    @Builder.Default
    private List<RecipeStepDto> recipeStepDtos = new ArrayList<>();

    // TODO: 레시피 카테고리
    // @Builder.Default
    // private List<RecipeCategoryDto> recipeCategoryDtos = new ArrayList<>();
    private RecipeCategoryDto recipeCategoryDto; // 단일 카테고리

    // TODO: 레시피 재료
    @Builder.Default
    private List<RecipeIngredientDto> recipeIngredientDtos = new ArrayList<>();

    private LocalDateTime regDate; // 등록일
    private LocalDateTime updateDate; // 수정일

    // 날짜 포멧팅
    public String getFormattedRegDate() {
        if (regDate == null) {
            return "";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return regDate.format(formatter);
    }
}
