package com.example.mat.entity.recipe;

import com.example.mat.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@ToString(exclude = {})
@Entity
public class RecipeCategory extends BaseEntity {

  @Id
  @SequenceGenerator(name = "recipe_cate_seq_gen", sequenceName = "recipe_cate_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_cate_seq_gen")
  private Long rCateId; // 레시피 번호

  private String name; // 레시피 카테고리 이름

}
