package com.example.mat.entity.won;

import jakarta.persistence.*;
import lombok.*;
import com.example.mat.entity.BaseEntity;

// 게시판 이미지 엔티티 클래스

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = { "board" })
@Entity
public class BoardImage extends BaseEntity {

  // 이미지 고유 번호
  @Id
  @SequenceGenerator(name = "board_image_seq_gen", sequenceName = "board_image_seq", allocationSize = 1, initialValue = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "board_image_seq_gen")
  private Long rInum;

  // 이미지 UUID
  private String uuid;

  // 이미지 파일 이름
  private String imgName;

  // 이미지 저장 경로
  private String path;

  // 해당 이미지가 속한 게시글 정보
  // Board 엔티티와 다대일 관계
  @ManyToOne(fetch = FetchType.LAZY)
  private Board board;
}
