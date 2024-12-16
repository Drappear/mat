package com.example.mat.entity.recipe;

import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class RecipeIngredient {

  @Id
  @SequenceGenerator(name = "recipe_ingredient_seq_gen", sequenceName = "recipe_ingredient_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_ingredient_seq_gen")
  private Long ingId; // 레시피 번호

  // TODO: rno 가져오기 -> Recipe 에서
  @ManyToOne(fetch = FetchType.LAZY)
  private Recipe recipe;

  private String name; // 재료명

  private String quantity; // 재료 값(양)
}
