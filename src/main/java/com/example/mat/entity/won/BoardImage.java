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

<<<<<<< HEAD
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bImgNo;

    private String uuid; // 파일의 고유 식별자
    private String imgName; // 파일 이름
    private String path; // 파일 저장 경로

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "board_id")
    private Board board; // 연관된 게시글
}
=======
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long bImgNo;

  private String uuid; // 파일의 고유 식별자
  private String imgName; // 파일 이름
  private String path; // 파일 저장 경로

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "board_id")
  private Board board; // 연관된 게시글
}
>>>>>>> 86c8cc0c6022911626db3c215fc316a3a0f5ded7
