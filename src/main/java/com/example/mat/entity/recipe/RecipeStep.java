package com.example.mat.entity.recipe;

import com.example.mat.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"recipe"})
@Entity
public class RecipeStep extends BaseEntity {

  @Id
  @SequenceGenerator(name = "recipe_step_seq_gen", sequenceName = "recipe_step_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_step_seq_gen")
  private Long stepNum; // 레시피 번호

  private String content; // 레시피 카테고리 이름

  private String uuid;

  private String imgName;

  private String path;

  // // TODO: rno 가져오기 -> Recipe 에서
  @ManyToOne(fetch = FetchType.LAZY)
  private Recipe recipe;

}
