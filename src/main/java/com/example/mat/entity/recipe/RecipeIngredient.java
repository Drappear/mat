package com.example.mat.entity.recipe;

import jakarta.persistence.FetchType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.example.mat.entity.BaseEntity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = { "recipe" })
@Entity
public class RecipeIngredient extends BaseEntity{

  @Id
  @SequenceGenerator(name = "recipe_ingredient_seq_gen", sequenceName = "recipe_ingredient_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_ingredient_seq_gen")
  private Long ingId; // 레시피 번호

  
  private String name; // 재료명
  
  private String quantity; // 재료 값(양)

  // TODO: rno 가져오기 -> Recipe 에서
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rno", nullable = false)
  private Recipe recipe;
}
