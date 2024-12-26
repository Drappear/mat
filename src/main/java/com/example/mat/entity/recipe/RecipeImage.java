package com.example.mat.entity.recipe;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
public class RecipeImage extends BaseEntity {

  @Id
  @SequenceGenerator(name = "recipe_image_seq_gen", sequenceName = "recipe_image_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipe_image_seq_gen")
  private Long rInum;

  private String uuid;
  private String imgName;
  private String path;

  // TODO: rno 가져오기 -> Recipe 에서
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "rno", nullable = false)
  private Recipe recipe;
}