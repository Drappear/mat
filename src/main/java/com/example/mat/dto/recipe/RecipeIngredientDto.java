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
public class RecipeIngredientDto {

  private Long ingId; // 레시피 번호

  // TODO: rno -> Recipe에서 가져오기

  @NotBlank(message = "재료명은 필수 항목입니다.")
  private String name; // 재료명

  @NotBlank(message = "재료 양은 필수 항목입니다.")
  private String quantity; // 재료 값(양)
}
