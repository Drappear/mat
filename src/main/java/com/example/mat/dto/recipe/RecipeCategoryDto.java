package com.example.mat.dto.recipe;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecipeCategoryDto {

  private Long rCateId; // 레시피 번호

  @NotBlank(message = "카테고리 선택은 필수 항목입니다.")
  private String name; // 레시피 카테고리 이름
}
