package com.example.mat.entity.won;

import jakarta.persistence.*;
import lombok.*;
import com.example.mat.entity.BaseEntity;

@Entity
@Table(name = "board_image")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "board") // Board 제외
public class BoardImage extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bImgNo; // 이미지 번호 (Primary Key)

  @Column(unique = true, nullable = false)
  private String uuid; // 파일의 고유 식별자
  private String imgName; // 파일 이름
  private String path; // 파일 저장 경로

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "board_id", nullable = false) // Board와 연관된 외래 키
  private Board board; // 연관된 게시글
}
